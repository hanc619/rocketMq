package com.hanc.mq.core.consumer.base;

public interface Subscriber {

    <T> void attach(String topic, String tag, Class<T> strategy, Observer observer);
}
