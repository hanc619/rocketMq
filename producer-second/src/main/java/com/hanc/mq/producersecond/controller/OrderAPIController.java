package com.hanc.mq.producersecond.controller;

import com.hanc.mq.core.model.MQSendResult;
import com.hanc.mq.core.producer.PublishMQClient;
import com.hanc.mq.producer.api.OrderApi;
import com.hanc.mq.producer.model.OrderPaidSucceedMessage;
import com.hanc.mq.producersecond.model.OnsTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderAPIController implements OrderApi {


	@Autowired
	private PublishMQClient publishMqClient;

	@Autowired
	private OnsTopic onsTopic;



	@Override
	public String pay(Integer orderId) {
		MQSendResult result = publishMqClient.sendMessage(onsTopic.getMsgTopic(), new OrderPaidSucceedMessage(orderId), String.valueOf(orderId));
		if(result == null){
			return "支付失败了";
		}
		return result.getMessageId();
	}

	@Override
	public String pay2(Integer orderId) {
		return null;
	}

}
