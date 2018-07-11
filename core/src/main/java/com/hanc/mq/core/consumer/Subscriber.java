package com.hanc.mq.core.consumer;

public interface Subscriber {

    <T> void attach(String topic, Observer<T> observer);
}
