package com.hanc.mq.core.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.aliyun.openservices.ons.api.*;
import com.google.common.collect.Maps;
import com.hanc.mq.core.consumer.base.Observer;
import com.hanc.mq.core.consumer.base.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.concurrent.ConcurrentMap;

@Component
public class OnsSubscriber implements Subscriber {

    private static final Logger LOGGER = LoggerFactory.getLogger(OnsSubscriber.class);

    private ConsumerMQClient consumerMQClient;

    private ConcurrentMap<String, Observer> observers = Maps.newConcurrentMap();

    private Consumer consumer;

    @Autowired
    public OnsSubscriber(ConsumerMQClient consumerMQClient) {
        this.consumerMQClient = consumerMQClient;
        this.consumer = ONSFactory.createConsumer(consumerMQClient.mqConsumerProperties());
    }

    @Override
    public <T> void attach(String topic, Observer<T> observer) {
        boolean replaced = observers.containsKey(topic);
        observers.put(topic, observer);
        consumer.subscribe(topic, "", new MessageListener() {
            @Override
            public Action consume(Message message, ConsumeContext context) {
                T messageBody;
                String topic = message.getTopic();
                Observer<T> observer = observers.get(topic);
                String type = message.getUserProperties("messageType");
                try {
                    String body = new String(message.getBody());
                    if ("String".equals(type)) {
                        messageBody = (T) body;
                    } else {
                        messageBody = (T) JSONObject.parseObject(body);
                    }
                    observer.onMessage(messageBody);
                    return Action.CommitMessage;
                } catch (Exception e) {
                    LOGGER.warn("attach body fail topic is [{}] ", topic, e);
                    return Action.ReconsumeLater;
                }
            }
        });
        LOGGER.info("topic[{}]  subscribe succeed{}", topic, replaced ? ", replaced which it exists yet":"");
        consumer.start();
    }
}

