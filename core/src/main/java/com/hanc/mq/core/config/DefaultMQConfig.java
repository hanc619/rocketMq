package com.hanc.mq.core.config;

import com.aliyun.openservices.ons.api.PropertyKeyConst;

import com.hanc.mq.core.common.SpringFactory;
import com.hanc.mq.core.model.MqConfigProp;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Properties;

public abstract class DefaultMQConfig implements ApplicationContextAware{

	protected SpringFactory springFactory;


	protected MqConfigProp mqConfigProp;

	@Autowired
	public DefaultMQConfig(MqConfigProp mqConfigProp, SpringFactory springFactory) {
		this.mqConfigProp = mqConfigProp;
		this.springFactory = springFactory;
	}

	protected Properties mqProperties(){
		Properties properties = new Properties();
		properties.put(PropertyKeyConst.AccessKey, mqConfigProp.getOnsAk());
		properties.put(PropertyKeyConst.SecretKey, mqConfigProp.getOnsCk());
		return properties;
	}


	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.springFactory = applicationContext.getBean(SpringFactory.class);
	}

}
