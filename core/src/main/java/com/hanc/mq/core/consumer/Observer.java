package com.hanc.mq.core.consumer;

public interface Observer<T> {

    void onMessage(T message);
}
