package com.yetx.service;

import com.yetx.constant.OrderChanelType;
import com.yetx.constant.OrderStatus;
import com.yetx.dao.MiaoshaOrderMapper;
import com.yetx.dao.OrderInfoMapper;
import com.yetx.pojo.MiaoshaOrder;
import com.yetx.pojo.MiaoshaUser;
import com.yetx.pojo.OrderInfo;
import com.yetx.redis.MiaoshaOrderKey;
import com.yetx.redis.RedisService;
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
    @Autowired
    OrderInfoMapper orderInfoMapper;
    @Autowired
    RedisService redisService;


    public MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(long userId, long goodsId) {
//        return orderMapper.getMiaoshaOrderByUserIdGoodsId(userId, goodsId);
        return redisService.get(MiaoshaOrderKey.miaoshaOrderByUidGid,MiaoshaOrderKey.convertOrderKey(userId,goodsId),MiaoshaOrder.class);
    }
    public boolean setMiaoshaOrderByUserIdGoodsId(long userId, long goodsId, OrderInfo orderInfo) {
        return redisService.set(MiaoshaOrderKey.miaoshaOrderByUidGid,MiaoshaOrderKey.convertOrderKey(userId,goodsId), orderInfo);
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
        orderMapper.insertOrderInfo(orderInfo);

        MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
        miaoshaOrder.setGoodsId(goodsVO.getId());
        miaoshaOrder.setOrderId(orderInfo.getId());
        miaoshaOrder.setUserId(user.getId());
        orderMapper.insertMiaoshaOrder(miaoshaOrder);

        // 将完成的订单写入redis
        setMiaoshaOrderByUserIdGoodsId(user.getId(),goodsVO.getId(),orderInfo);

        return orderInfo;

    }

    public OrderInfo getMiaoshaOrderByOrderId(long orderId){
        return orderInfoMapper.selectByPrimaryKey(orderId);
    }

    public void deleteAllOrders(){

        orderMapper.deleteAllMiaoshaOrder();
        orderMapper.deleteAllOrderInfo();

    }
}
