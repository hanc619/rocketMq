package com.hanc.mq.producer.model;

import java.io.Serializable;

public class OrderPaidSucceedMessage implements Serializable{
	
	private static final long serialVersionUID = 8673315516088031608L;

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
