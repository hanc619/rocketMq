package com.hanc.mq.core.consumer;

import com.alibaba.fastjson.JSON;
import com.aliyun.openservices.ons.api.*;
import com.google.common.collect.Maps;
import com.hanc.mq.core.consumer.base.Observer;
import com.hanc.mq.core.consumer.base.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
                Class<?> messageType;
                String topic = message.getTopic();
                Observer<T> _observer = observers.get(topic);
                try {
                    messageType = Class.forName(message.getUserProperties("messageType"));
                    if (messageType.isPrimitive() || messageType == String.class) {
                        messageBody = (T) new String(message.getBody());
                    } else {
                        messageBody = JSON.parseObject(message.getBody(), messageType);
                    }
                    _observer.onMessage(messageBody);
                    return Action.CommitMessage;
                } catch (Exception e) {
                    return Action.ReconsumeLater;
                }
            }
        });
        LOGGER.info("topic[{}]  subscribe succeed{}", topic, replaced?", replaced which it exists yet":"");
        consumer.start();
    }
}

