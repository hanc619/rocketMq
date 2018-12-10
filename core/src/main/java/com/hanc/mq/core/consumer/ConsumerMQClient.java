package com.hanc.mq.core.consumer;

import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.PropertyValueConst;
import com.hanc.mq.core.config.DefaultMQConfig;
import com.hanc.mq.core.model.MqConfigProp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Properties;

@Component
public class ConsumerMQClient extends DefaultMQConfig {


    @Autowired
    public ConsumerMQClient(MqConfigProp mqConfigProp) {
        super(mqConfigProp);
    }

    public Properties mqConsumerProperties (){
        Properties properties = mqProperties();
        properties.put(PropertyKeyConst.ConsumerId, mqConfigProp.getCid());
        properties.put(PropertyKeyConst.MessageModel, PropertyValueConst.BROADCASTING);
        return properties;
    }
}
