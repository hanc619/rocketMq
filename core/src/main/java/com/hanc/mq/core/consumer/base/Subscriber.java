package com.hanc.mq.core.consumer.base;

import java.io.Serializable;

public interface Subscriber {

    <T> void attach(String topic, String tag,  Observer observer);
}
