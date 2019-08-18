package com.yetx.service;

import com.yetx.dao.GoodsMapper;
import com.yetx.dao.MiaoshaGoodsMapper;
import com.yetx.pojo.MiaoshaGoods;
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

    public List<GoodsVO> listGoodsVo(){
        return goodsMapper.listMiaoshaGoods();
    }

    public GoodsVO getGoodsVoByGoodsId(long goodsId) {
        return goodsMapper.getGoodsVoByGoodsId(goodsId);
    }

    public void reduceStock(GoodsVO goodsVO){
        goodsMapper.reduceStock(goodsVO.getId());
    }
}
