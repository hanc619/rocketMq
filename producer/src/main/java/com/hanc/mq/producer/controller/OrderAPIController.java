package com.hanc.mq.producer.controller;

import com.hanc.mq.core.model.MQSendResult;
import com.hanc.mq.core.producer.PublishMQClient;
import com.hanc.mq.producer.api.OrderApi;
import com.hanc.mq.producer.model.OnsTopic;
import com.hanc.mq.producer.model.OrderPaidSucceedMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Api(value = "test")
@RestController
@RequestMapping(value = "order/")
public class OrderAPIController {


	@Autowired
	private PublishMQClient publishMqClient;

	@Autowired
	private OnsTopic onsTopic;

	private AtomicInteger i = new AtomicInteger(0);



	@PostMapping("pay")
	@ApiOperation(value = "pay1")
	public String pay(Integer orderId) {
		int round = new Random().nextInt(100);
		i.addAndGet(1);
		orderId = i.intValue();
		MQSendResult result = publishMqClient.sendMessage(onsTopic.getMsgTopic(), "tag1",new OrderPaidSucceedMessage(orderId), String.valueOf(orderId));
		if(result == null){
			return "支付失败了";
		}
		return result.getMessageId();
	}

	@PostMapping("pay2")
	@ApiOperation(value = "pay2")
	public void pay2(Integer orderId) {
		i.addAndGet(1);
		orderId = i.intValue();
		publishMqClient.sendAsyncMessage(onsTopic.getMsgTopic(), "tag2",new OrderPaidSucceedMessage(orderId), String.valueOf(orderId));
	}

}
