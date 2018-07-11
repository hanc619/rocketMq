package com.hanc.mq.core.producer;

import com.alibaba.fastjson.JSON;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.hanc.mq.core.config.DefaultMQConfig;
import com.hanc.mq.core.model.MQSendResult;
import com.hanc.mq.core.model.MqConfigProp;
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


	@Autowired
	public PublishMQClient(MqConfigProp mqConfigProp)  {
		super(mqConfigProp);
		Properties properties = super.mqProperties();
		properties.put(PropertyKeyConst.ProducerId, mqConfigProp.getPid());
		this.producer =ONSFactory.createProducer(properties);
	}

	/**
	 * 发送普通消息
	 * @param tag
	 * @param body
	 * @param key
	 * @return
	 */
	public MQSendResult sendMessage(String tag, Object body, String key){
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

	
	private Message wrapMessage(String topic, Object body, String key){
		byte[] byteBody ;
		if(body.getClass().isPrimitive() || body.getClass() == String.class) {
			byteBody = String.valueOf(body).getBytes(Charset.forName("UTF-8"));
		} else {
			byteBody = JSON.toJSONBytes(body);
		}
		key = topic + "_" + key;
		Message message = new Message(topic,topic,key,byteBody);
		message.putUserProperties("messageType", body.getClass().getName());
		message.putUserProperties("_uuid", System.nanoTime() + UUID.randomUUID().toString().replaceAll("-", ""));
		return message;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		if(producer != null){
			producer.start();
		}
	}
	
	@Override
	public void destroy() throws Exception {
		if(producer != null){
			producer.shutdown();
		}
	}
}
