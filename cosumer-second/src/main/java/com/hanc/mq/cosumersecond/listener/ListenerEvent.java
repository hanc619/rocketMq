package com.hanc.mq.cosumersecond.listener;

import com.hanc.mq.core.consumer.OnsSubscriber;
import com.hanc.mq.cosumersecond.model.OnsTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;

@Component
public class ListenerEvent {

    private static final Logger LOGGER = LoggerFactory.getLogger(ListenerEvent.class);

    @Autowired
    private OnsSubscriber subscriber;

    @Autowired
    private OnsTopic onsTopic;

    @PostConstruct
    public void topicMsgConsumers() {
        if (!onsTopic.getOnsSwitch()) {
            LOGGER.info("the onsSwitch2 is set off, consumer not subscribe.");
            return;
        }
        subscriber.attach(onsTopic.getMsgTopic(), "*", String.class, (message, tag) -> {
            LOGGER.info("consumer first get sensitive2 ONS Msg id is [{}], tag is [{}]：", message, tag);
        });
    }
}
