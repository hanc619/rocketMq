package com.hanc.mq.producer.model;

public class OrderPaidSucceedMessage {
	
//	private static final long serialVersionUID = 8673315516088031608L;

	private Integer orderId;
	
	public OrderPaidSucceedMessage() {
	
	}

	public OrderPaidSucceedMessage(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
}
