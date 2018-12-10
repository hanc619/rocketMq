package com.hanc.mq.consumer.model;

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

	@Override
	public String toString() {
		return "OrderPaidSucceedMessage{" +
				"orderId=" + orderId +
				'}';
	}
}
