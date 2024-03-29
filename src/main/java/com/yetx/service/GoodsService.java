package com.yetx.service;

import com.yetx.dao.GoodsMapper;
import com.yetx.dao.MiaoshaGoodsMapper;
import com.yetx.pojo.MiaoshaGoods;
import com.yetx.redis.MiaoshaGoodsKey;
import com.yetx.redis.RedisService;
import com.yetx.vo.GoodsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class GoodsService {
    @Autowired
    GoodsMapper goodsMapper;
    @Autowired
    RedisService redisService;

    public List<GoodsVO> listGoodsVo(){
        return goodsMapper.listMiaoshaGoods();
    }

    public GoodsVO getGoodsVoByGoodsId(long goodsId) {
//        return redisService.get(MiaoshaGoodsKey.miaoshaGoodsIdKey,""+goodsId,GoodsVO.class);
        return goodsMapper.getGoodsVoByGoodsId(goodsId);
    }

    public boolean reduceStock(GoodsVO goodsVO){
        MiaoshaGoods g = new MiaoshaGoods();
        g.setGoodsId(goodsVO.getId());
        int updateRows =  goodsMapper.reduceStock(g);
        return  updateRows>0;
    }

    public void resetStock(List<GoodsVO> goodsList) {
        for(GoodsVO goodsVO : goodsList ) {
            MiaoshaGoods g = new MiaoshaGoods();
            g.setStockCount(goodsVO.getStockCount());
            g.setGoodsId(goodsVO.getId());
            goodsMapper.resetStock(g);
        }
    }
}
