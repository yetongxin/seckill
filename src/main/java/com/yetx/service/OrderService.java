package com.yetx.service;

import com.yetx.constant.OrderChanelType;
import com.yetx.constant.OrderStatus;
import com.yetx.dao.MiaoshaOrderMapper;
import com.yetx.pojo.MiaoshaOrder;
import com.yetx.pojo.MiaoshaUser;
import com.yetx.pojo.OrderInfo;
import com.yetx.vo.GoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class OrderService {

    @Autowired
    MiaoshaOrderMapper orderMapper;

    public MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(long userId, long goodsId) {
        return orderMapper.getMiaoshaOrderByUserIdGoodsId(userId, goodsId);
    }

    @Transactional
    public OrderInfo createOrder(MiaoshaUser user, GoodsVO goodsVO){
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goodsVO.getId());
        orderInfo.setGoodsName(goodsVO.getGoodsName());
        orderInfo.setGoodsPrice(new BigDecimal(goodsVO.getMiaoshaPrice()));
        orderInfo.setOrderChannel(OrderChanelType.PC);
        orderInfo.setStatus(OrderStatus.NO_PAY);
        orderInfo.setUserId(user.getId());
        long orderId = orderMapper.insertOrderInfo(orderInfo);

        MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
        miaoshaOrder.setGoodsId(goodsVO.getId());
        miaoshaOrder.setOrderId(orderId);
        miaoshaOrder.setUserId(user.getId());
        orderMapper.insertMiaoshaOrder(miaoshaOrder);
        return orderInfo;

    }

}
