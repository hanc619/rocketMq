package com.hanc.mq.core.model;

public enum ConsumeTag {

	ORDER_PAID_SUCCEED("PUSH_MESSAGAE_DEV_CLL");
	
	private String topic;
	ConsumeTag(String topic) {
		this.topic = topic;
	}
	
	public String getTopic() {
		return topic;
	}
}
