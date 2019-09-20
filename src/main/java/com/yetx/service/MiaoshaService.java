package com.yetx.service;

import com.yetx.constant.MiaoshaStatus;
import com.yetx.pojo.MiaoshaGoods;
import com.yetx.pojo.MiaoshaUser;
import com.yetx.pojo.OrderInfo;
import com.yetx.redis.MiaoshaGoodsKey;
import com.yetx.redis.MiaoshaOrderKey;
import com.yetx.redis.RedisService;
import com.yetx.vo.GoodsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class MiaoshaService {
    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    RedisService redisService;

    @Transactional
    public OrderInfo miaosha(MiaoshaUser user, GoodsVO goods) {
        //减库存 下订单 写入秒杀订单
        // 注意减库存的时候需要判断是否真正减了库存，有可能多个线程同时进行减库存了，
        // 但是被堵塞了，等堵塞结束后发现库存已经为0了，此时updateRows==0，说明这个线程秒杀失败了，不应该创建订单
        boolean ifUpdate = goodsService.reduceStock(goods);
        if(ifUpdate)
            return orderService.createOrder(user, goods);
        else{
            setMiaoshaOverInRedis(goods.getId());
            return null;
        }
    }

    public long getMiaoshaResult(MiaoshaUser user,long goodsId){
        String prefix = MiaoshaOrderKey.convertOrderKey(user.getId(),goodsId);
        OrderInfo orderInfo = redisService.get(MiaoshaOrderKey.miaoshaOrderByUidGid,prefix,OrderInfo.class);
        if(orderInfo!=null){
            return orderInfo.getId();
        } else {
            if(isOverInRedis(goodsId))
                return MiaoshaStatus.FAIRED;
            else
                return MiaoshaStatus.PENDING;
        }

    }
    public void setMiaoshaOverInRedis(long goodsId){
        redisService.set(MiaoshaGoodsKey.miaoshaGoodsOverKey,""+goodsId,true);
    }
    public boolean isOverInRedis(long goodsId){
        return redisService.exists(MiaoshaGoodsKey.miaoshaGoodsOverKey,""+goodsId);
    }

    // 重置数据库
    public void resetAllOrderInDB(List<GoodsVO> goodsList){
        goodsService.resetStock(goodsList);
        orderService.deleteAllOrders();
    }

}
