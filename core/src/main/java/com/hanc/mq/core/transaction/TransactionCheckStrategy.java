package com.hanc.mq.core.transaction;

import com.alibaba.fastjson.JSON;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.transaction.LocalTransactionExecuter;
import com.aliyun.openservices.ons.api.transaction.TransactionStatus;
import com.hanc.mq.core.model.ConsumeTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class TransactionCheckStrategy<T> implements LocalTransactionExecuter {
	private static final Logger LOGGER = LoggerFactory.getLogger(TransactionCheckStrategy.class);
	@SuppressWarnings("unchecked")
	public TransactionStatus check(Message message){
		T messageBody = null;
		Class<?> messageType;
		try {
			messageType = Class.forName(message.getUserProperties("messageType"));
			if(messageType.isPrimitive() || messageType == String.class) {
				messageBody = (T)new String(message.getBody());
			} else {
				messageBody = JSON.parseObject(message.getBody(), messageType);
			}
			return checkLocalTransaction(messageBody);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
			return TransactionStatus.Unknow;
		}
	}

	public abstract boolean support(ConsumeTag tag);
	
	public abstract TransactionStatus checkLocalTransaction(T message);
	
}
