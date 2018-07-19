package com.hanc.mq.consumer.listener;
import com.alibaba.fastjson.JSONObject;
import com.hanc.mq.consumer.model.OnsTopic;
import com.hanc.mq.core.consumer.base.Observer;
import com.hanc.mq.core.consumer.OnsSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

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
        subscriber.attach(onsTopic.getMsgTopic(), new Observer<Map>() {
            @Override
            public void onMessage(Map message) {
                LOGGER.info("get sensitive2 ONS Msg id is [{}]：", message.get("orderId"));
            }
        });
    }
}
