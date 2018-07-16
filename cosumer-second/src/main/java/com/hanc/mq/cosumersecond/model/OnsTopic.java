package com.hanc.mq.cosumersecond.model;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OnsTopic {

    @Value("${ons.topic.msg}")
    public String msgTopic;

    @Value("${ons.topic.switch}")
    public Boolean onsSwitch;

    public String getMsgTopic() {
        return msgTopic;
    }

    public void setMsgTopic(String msgTopic) {
        this.msgTopic = msgTopic;
    }

    public Boolean getOnsSwitch() {
        return onsSwitch;
    }

    public void setOnsSwitch(Boolean onsSwitch) {
        this.onsSwitch = onsSwitch;
    }
}
