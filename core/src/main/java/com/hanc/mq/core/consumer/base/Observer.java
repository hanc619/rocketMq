package com.hanc.mq.core.consumer.base;


public interface Observer{

    void onMessage(String message, String tag);
}
