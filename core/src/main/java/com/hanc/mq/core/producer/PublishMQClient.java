package com.hanc.mq.core.producer;

import com.alibaba.fastjson.JSON;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.transaction.LocalTransactionChecker;
import com.aliyun.openservices.ons.api.transaction.LocalTransactionExecuter;
import com.aliyun.openservices.ons.api.transaction.TransactionProducer;
import com.hanc.mq.core.common.SpringFactory;
import com.hanc.mq.core.config.DefaultMQConfig;
import com.hanc.mq.core.model.ConsumeTag;
import com.hanc.mq.core.model.MQSendResult;
import com.hanc.mq.core.model.MqConfigProp;
import com.hanc.mq.core.transaction.DefaultTransactionChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.Properties;
import java.util.UUID;

@Component
public class PublishMQClient extends DefaultMQConfig implements InitializingBean,DisposableBean  {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PublishMQClient.class);
	
	private Producer producer;

	private TransactionProducer transactionProducer;


	@Autowired
	public PublishMQClient(MqConfigProp mqConfigProp, SpringFactory springFactory)  {
		super(mqConfigProp, springFactory);
		Properties properties = super.mqProperties();
		properties.put(PropertyKeyConst.ProducerId, mqConfigProp.getPid());
		this.producer =ONSFactory.createProducer(properties);
		this.transactionProducer = ONSFactory.createTransactionProducer(properties,localTransactionChecker());
	}

	/**
	 * 发送普通消息
	 * @param tag
	 * @param body
	 * @param key
	 * @return
	 */
	public MQSendResult sendMessage(ConsumeTag tag, Object body, String key){
		try{
			Message message = wrapMessage(tag, body, key);
			MQSendResult result =  MQSendResult.convert(producer.send(message));
			LOGGER.info("MQ send result  {}",JSON.toJSON(result));
			return result;
		}catch (Exception e){
			LOGGER.error(e.getMessage(),e);
		}
		return null;
	}

	private LocalTransactionChecker localTransactionChecker(){
		DefaultTransactionChecker checker = new DefaultTransactionChecker();
		springFactory.initializeBean(checker);
		return checker;
	}

	/**
	 * 发送事务消息
	 * @param tag
	 * @param body
	 * @param key
	 * @param executer
	 * @param arg
	 * @return
	 */
	public MQSendResult sendTransactionMessage(ConsumeTag tag,Object body,String key,LocalTransactionExecuter executer,Object arg){
		try{
			Message message = wrapMessage(tag, body, key);
			MQSendResult result = MQSendResult.convert(transactionProducer.send(message, executer, arg));
			LOGGER.info("MQ send result  {}",JSON.toJSON(result));
			return result;
		}catch (Exception e){
			LOGGER.error(e.getMessage(),e);
		}
		return null;
	}
	
	private Message wrapMessage(ConsumeTag tag, Object body, String key){
		byte[] byteBody = null;
		String topic = tag.getTopic();
		if(body.getClass().isPrimitive() || body.getClass() == String.class) {
			byteBody = String.valueOf(body).getBytes(Charset.forName("UTF-8"));
		} else {
			byteBody = JSON.toJSONBytes(body);
		}
		key = topic + "_" + tag.name() + "_" + key;
		Message message = new Message(topic,tag.name(),key,byteBody);
		message.putUserProperties("messageType", body.getClass().getName());
		message.putUserProperties("_uuid", System.nanoTime() + UUID.randomUUID().toString().replaceAll("-", ""));
		return message;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {

		if(producer != null){
			producer.start();
		}
		
		if(transactionProducer != null){
			transactionProducer.start();
		}
	}
	
	@Override
	public void destroy() throws Exception {
		if(producer != null){
			producer.shutdown();
		}
		
		if(transactionProducer != null){
			transactionProducer.shutdown();
		}
	}
}
