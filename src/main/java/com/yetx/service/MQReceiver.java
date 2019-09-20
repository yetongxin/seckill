package com.yetx.service;

import com.alibaba.fastjson.JSON;
import com.yetx.config.MQConfig;
import com.yetx.pojo.MiaoshaMessage;
import com.yetx.pojo.MiaoshaOrder;
import com.yetx.pojo.MiaoshaUser;
import com.yetx.vo.GoodsVO;
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
    @Autowired
    MiaoshaService miaoshaService;
    @Autowired
    GoodsService goodsService;
    @Autowired
    OrderService orderService;

    @RabbitListener(queues=MQConfig.QUEUE)
    public void receive(String message) {
        log.info("receive msg:{}",message);
    }

    @RabbitListener(queues = MQConfig.MIAOSHA_QUEUE)
    public void receiveMiaoshaWork(String message){
        try {
            MiaoshaMessage miaoshaMessage = JSON.parseObject(message,MiaoshaMessage.class);
            log.info("receive miaoshawork:{}",miaoshaMessage);

            GoodsVO miaoshaGoods = goodsService.getGoodsVoByGoodsId(miaoshaMessage.getGoodsID());
            MiaoshaUser user = miaoshaMessage.getUser();

            int stock = miaoshaGoods.getStockCount();
            if(stock<=0){
                return;
            }
            // 已存在用户订单则不进行处理
            MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(),miaoshaMessage.getGoodsID());
            if(order!=null){
                return;
            }
            // bug : 数据库存在该订单，但是redis不存在时，会一直不设置
            // 可以考虑将result:goodsid-userid放入redis，消费者正确消费信息后给这个key设置值
            try {
                miaoshaService.miaosha(user,miaoshaGoods);
            }catch (Exception e){
                return;
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
