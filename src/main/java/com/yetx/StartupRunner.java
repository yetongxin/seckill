package com.yetx;

import com.yetx.pojo.MiaoshaGoods;
import com.yetx.redis.MiaoshaGoodsKey;
import com.yetx.redis.RedisService;
import com.yetx.service.GoodsService;
import com.yetx.vo.GoodsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class StartupRunner implements CommandLineRunner {


    @Autowired
    RedisService redisService;
    @Autowired
    GoodsService goodsService;

    @Override
    public void run(String... args) throws Exception {
        log.info("Initializing system.....");
        // 将秒杀中的商品库存写入
        List<GoodsVO> goodsList = goodsService.listGoodsVo();
        if(goodsList==null)
            return;
        for(GoodsVO goodsVO : goodsList){
            redisService.set(MiaoshaGoodsKey.miaoshaGoodsStockKey,""+goodsVO.getId(), goodsVO.getStockCount());
        }

        log.info("Initializing system done....");
    }
}
