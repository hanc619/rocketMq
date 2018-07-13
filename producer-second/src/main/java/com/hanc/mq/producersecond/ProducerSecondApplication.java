package com.hanc.mq.producersecond;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableEurekaClient
@ComponentScan(basePackages = {"com.hanc.mq"})
public class ProducerSecondApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProducerSecondApplication.class, args);
    }
}
