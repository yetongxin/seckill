package com.yetx.service;

import com.alibaba.fastjson.JSON;
import com.yetx.config.MQConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MQReceiver {
    @Autowired
    AmqpTemplate amqpTemplate ;

    @RabbitListener(queues=MQConfig.QUEUE)
    public void receive(String message) {
        log.info("receive msg:{}",message);
    }
}
