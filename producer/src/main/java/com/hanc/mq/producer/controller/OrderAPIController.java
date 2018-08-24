package com.hanc.mq.producer.controller;

import com.hanc.mq.core.model.MQSendResult;
import com.hanc.mq.core.producer.PublishMQClient;
import com.hanc.mq.producer.api.OrderApi;
import com.hanc.mq.producer.model.OnsTopic;
import com.hanc.mq.producer.model.OrderPaidSucceedMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class OrderAPIController implements OrderApi {


	@Autowired
	private PublishMQClient publishMqClient;

	@Autowired
	private OnsTopic onsTopic;

	private AtomicInteger i = new AtomicInteger(0);



	@Override
	public String pay(Integer orderId) {
		int round = new Random().nextInt(100);
		i.addAndGet(1);
		orderId = i.intValue();
		MQSendResult result = publishMqClient.sendMessage(onsTopic.getMsgTopic(), new OrderPaidSucceedMessage(orderId), String.valueOf(orderId));
		if(result == null){
			return "支付失败了";
		}
		return result.getMessageId();
	}

}
