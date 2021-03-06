package com.hanc.mq.producer.api;


import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value="producer")
public interface OrderApi {
	@RequestMapping("/order/pay")
	String pay(@RequestParam("orderId") Integer orderId);

	@RequestMapping("/order/pay2")
	String pay2(@RequestParam("orderId") Integer orderId);
}
