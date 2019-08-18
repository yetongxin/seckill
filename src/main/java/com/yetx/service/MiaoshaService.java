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
        goodsService.reduceStock(goods);
        return orderService.createOrder(user, goods);
    }
}
