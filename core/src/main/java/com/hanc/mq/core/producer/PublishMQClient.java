package com.hanc.mq.core.producer;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.ons.api.*;
import com.hanc.mq.core.config.DefaultMQConfig;
import com.hanc.mq.core.model.MQSendResult;
import com.hanc.mq.core.model.MqConfigProp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.charset.Charset;
import java.util.Properties;
import java.util.UUID;

@Component
public class PublishMQClient extends DefaultMQConfig implements InitializingBean, DisposableBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(PublishMQClient.class);

    private Producer producer;

    @Autowired
    public PublishMQClient(MqConfigProp mqConfigProp) {
        super(mqConfigProp);
        Properties properties = super.mqProperties();
        if (!StringUtils.isEmpty(mqConfigProp.getPid())) {
            properties.put(PropertyKeyConst.ProducerId, mqConfigProp.getPid());
            this.producer = ONSFactory.createProducer(properties);
        }
    }

    /**
     * 发送普通消息
     *
     * @param topic topic
     * @param body  消息体
     * @param key   一般作为订单号保证保证幂等性
     * @return
     */
    public MQSendResult sendMessage(String topic, Object body, String key) {
        return this.sendMessage(topic, "", body, key);
    }


    /**
     * 发送异步消息
     *
     * @param topic topic
     * @param body  消息体
     * @param key   一般作为订单号保证保证幂等性
     * @return
     */
    public void sendAsyncMessage(String topic, Object body, String key) {
        this.sendAsyncMessage(topic, "", body, key);
    }

    /**
     * 发送异步消息
     *
     * @param topic topic
     * @param tag   tag
     * @param body  消息体
     * @param key   一般作为订单号保证保证幂等性
     * @return
     */
    public void sendAsyncMessage(String topic, String tag, Object body, String key) {
        try {
            LOGGER.info("MQ send async message,topic is [{}], tag is [{}], key is [{}]", topic, tag, key);
            Message message = wrapMessage(topic, tag, body, key);
            // 异步发送消息, 发送结果通过callback返回给客户端。
            producer.sendAsync(message, new SendCallback() {
                @Override
                public void onSuccess(final SendResult sendResult) {
                    // 消费发送成功
                    LOGGER.info("send async message success. topic is [{}], msgId is [{}]", sendResult.getTopic(), sendResult.getMessageId());
                }
                @Override
                public void onException(OnExceptionContext context) {
                    // 消息发送失败
                    LOGGER.warn("send async message failed. topic is [{}], msgId is [{}]", context.getTopic(), context.getMessageId());
                }
            });
        } catch (Exception e) {
            LOGGER.error("send async message failed", e);
        }
    }

    /**
     * 发送普通消息
     *
     * @param topic topic
     * @param tag   tag
     * @param body  消息体
     * @param key   一般作为订单号保证保证幂等性
     * @return
     */
    public MQSendResult sendMessage(String topic, String tag, Object body, String key) {
        try {
            Message message = wrapMessage(topic, tag, body, key);
            MQSendResult result = MQSendResult.convert(producer.send(message));
            LOGGER.info("MQ send message,topic is [{}], tag is [{}], key is [{}]", topic, tag, key);
            return result;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }


    /**
     * 封装消息体
     *
     * @param topic topic
     * @param tag   tag
     * @param body  消息体
     * @param key   一般作为订单号保证保证幂等性
     * @return
     */
    private Message wrapMessage(String topic, String tag, Object body, String key) {
        byte[] byteBody;
        if (body.getClass().isPrimitive() || body.getClass() == String.class) {
            byteBody = String.valueOf(body).getBytes(Charset.forName("UTF-8"));
        } else {
            String bodyString = JSONObject.toJSONString(body);
            byteBody = String.valueOf(bodyString).getBytes(Charset.forName("UTF-8"));
        }
        key = topic + "_" + key;
        Message message = new Message(topic, tag, key, byteBody);
        message.putUserProperties("messageType", body.getClass().getSimpleName());
        message.putUserProperties("_uuid", System.nanoTime() + UUID.randomUUID().toString().replaceAll("-", ""));
        return message;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (producer != null) {
            producer.start();
        }
    }

    @Override
    public void destroy() throws Exception {
        if (producer != null) {
            producer.shutdown();
        }
    }
}
