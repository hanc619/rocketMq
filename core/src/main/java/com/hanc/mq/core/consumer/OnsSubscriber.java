package com.hanc.mq.core.consumer;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.ons.api.*;
import com.google.common.collect.Maps;
import com.hanc.mq.core.consumer.base.Observer;
import com.hanc.mq.core.consumer.base.Subscriber;
import com.hanc.mq.core.model.TopicForTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;

@Component
public class OnsSubscriber implements Subscriber {

    private static final Logger LOGGER = LoggerFactory.getLogger(OnsSubscriber.class);

    private ConsumerMQClient consumerMQClient;

    private ConcurrentMap<TopicForTag, Observer> observers = Maps.newConcurrentMap();

    private Consumer consumer;

    @Autowired
    public OnsSubscriber(ConsumerMQClient consumerMQClient) {
        this.consumerMQClient = consumerMQClient;
        this.consumer = ONSFactory.createConsumer(consumerMQClient.mqConsumerProperties());
        consumer.start();
    }


    @Override
    public <T> void attach(String topic, String tag, Class<T> strategy, Observer observer) {

        boolean replaced = observers.containsKey(topic);
        observers.put(new TopicForTag(topic, tag), observer);
        consumer.subscribe(topic, tag, new MessageListener() {
            @Override
            public Action consume(Message message, ConsumeContext context) {
                String topic = message.getTopic();
                String tag = message.getTag();
                try {
                    String body = new String(message.getBody(), "utf-8");
                    T t = JSONObject.parseObject(body, strategy);
                    observer.onMessage(t, tag);
                    return Action.CommitMessage;
                }
                catch (Exception e) {
                    LOGGER.warn("attach body fail topic is [{}] ", topic, e);
                    return Action.CommitMessage;
                }
            }
        });
        LOGGER.info("topic[{}]  subscribe succeed{}", topic, replaced ? ", replaced which it exists yet":"");
    }
}

