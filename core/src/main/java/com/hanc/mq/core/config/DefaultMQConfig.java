package com.hanc.mq.core.config;

import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.hanc.mq.core.model.MqConfigProp;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Properties;

public abstract class DefaultMQConfig{

	protected MqConfigProp mqConfigProp;

	@Autowired
	public DefaultMQConfig(MqConfigProp mqConfigProp) {
		this.mqConfigProp = mqConfigProp;
	}

	protected Properties mqProperties(){
		Properties properties = new Properties();
		properties.put(PropertyKeyConst.AccessKey, mqConfigProp.getAk());
		properties.put(PropertyKeyConst.SecretKey, mqConfigProp.getSk());
		return properties;
	}

}
