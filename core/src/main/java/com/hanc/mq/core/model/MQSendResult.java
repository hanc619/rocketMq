package com.hanc.mq.core.model;

import com.aliyun.openservices.ons.api.SendResult;

public class MQSendResult extends SendResult{
	
	private boolean success;
	
	public MQSendResult() {

	}
	
	public boolean isSuccess() {
		return success;
	}
	
	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	public static MQSendResult convert(SendResult result){
		MQSendResult mqSendResult = new MQSendResult();
		mqSendResult.setSuccess(true);
		mqSendResult.setMessageId(result.getMessageId());
		mqSendResult.setTopic(result.getTopic());
		return mqSendResult;
	}

}
