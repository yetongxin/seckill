package com.yetx.service;

import com.yetx.pojo.MiaoshaUser;
import com.yetx.pojo.OrderInfo;
import com.yetx.vo.GoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MiaoshaService {
    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;
    @Transactional
    public OrderInfo miaosha(MiaoshaUser user, GoodsVO goods) {
        //减库存 下订单 写入秒杀订单
        // 注意减库存的时候需要判断是否真正减了库存，有可能多个线程同时进行减库存了，
        // 但是被堵塞了，等堵塞结束后发现库存已经为0了，此时updateRows==0，说明这个线程秒杀失败了，不应该创建订单
        boolean ifUpdate = goodsService.reduceStock(goods);
        if(ifUpdate)
            return orderService.createOrder(user, goods);
        else
            return null;
    }
}
