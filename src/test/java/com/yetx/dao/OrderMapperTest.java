package com.yetx.dao;

import com.yetx.constant.OrderChanelType;
import com.yetx.constant.OrderStatus;
import com.yetx.pojo.MiaoshaUser;
import com.yetx.pojo.OrderInfo;
import com.yetx.service.GoodsService;
import com.yetx.service.MiaoshaService;
import com.yetx.vo.GoodsVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class OrderMapperTest {

    @Autowired
    MiaoshaOrderMapper orderMapper;
    @Autowired
    MiaoshaService service;
    @Autowired
    GoodsService goodsService;
    @Test
    public void testAddOrderInfo(){
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(1l);
        orderInfo.setGoodsName("iPhone x");
        orderInfo.setGoodsPrice(BigDecimal.valueOf(0.01));
        orderInfo.setOrderChannel(OrderChanelType.PC);
        orderInfo.setStatus(OrderStatus.NO_PAY);
        orderInfo.setUserId(123l);
        long orderId = orderMapper.insertOrderInfo(orderInfo);
        System.out.println(orderInfo.getId());

    }
    @Test
    public void testMiaosha(){
        MiaoshaUser user = new MiaoshaUser();
        user.setId(12345l);
        GoodsVO goods = goodsService.getGoodsVoByGoodsId(1l);
        Assert.assertEquals("iphoneX",goods.getGoodsName());
        Assert.assertNotEquals(0l, Optional.ofNullable(goods.getStockCount()));
        OrderInfo orderInfo = service.miaosha(user,goods);
        Assert.assertEquals( user.getId(),orderInfo.getUserId());
        Assert.assertEquals( goods.getId(),orderInfo.getGoodsId());
        log.info(orderInfo.toString());
    }

}
