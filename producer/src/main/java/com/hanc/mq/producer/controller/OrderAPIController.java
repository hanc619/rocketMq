package com.hanc.mq.producer.controller;

import com.hanc.mq.core.model.ConsumeTag;
import com.hanc.mq.core.model.MQSendResult;
import com.hanc.mq.core.producer.PublishMQClient;
import com.hanc.mq.producer.api.OrderApi;
import com.hanc.mq.producer.model.OrderPaidSucceedMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderAPIController implements OrderApi {


	@Autowired
	private PublishMQClient publishMqClient;


	@Override
	public String pay(Integer orderId) {
		MQSendResult result = publishMqClient.sendMessage(ConsumeTag.ORDER_PAID_SUCCEED, new OrderPaidSucceedMessage(orderId), String.valueOf(orderId));
		if(result == null){
			return "支付失败了";
		}
		return result.getMessageId();
	}

}