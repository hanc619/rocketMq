package com.hanc.mq.core.consumer.base;


public interface Observer<T>{

    void onMessage(T t, String tag);
}
