package com.yetx.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yetx.config.MQConfig;
import com.yetx.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MQSender {

    @Autowired
    AmqpTemplate amqpTemplate ;

    	public void send(Object message) {
    	    String msg;
    	    if(message.getClass()!=String.class){
                msg = JSON.toJSONString(message);
            }else{
    	        msg = (String) message;
            }
            log.info("send message:"+msg);
            amqpTemplate.convertAndSend(MQConfig.QUEUE, msg);
	}
}
