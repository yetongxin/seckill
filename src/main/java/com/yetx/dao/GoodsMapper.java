package com.yetx.dao;

import com.yetx.pojo.Goods;
import com.yetx.pojo.MiaoshaGoods;
import com.yetx.vo.GoodsVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoodsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Goods record);

    int insertSelective(Goods record);

    Goods selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Goods record);

    int updateByPrimaryKeyWithBLOBs(Goods record);

    int updateByPrimaryKey(Goods record);

    @Select("select g.*,mg.stock_count, mg.start_date, mg.end_date,mg.miaosha_price from goods g left join miaosha_goods mg on mg.goods_id = g.id")
    List<GoodsVO> listMiaoshaGoods();

    //TODO 检查有无问题，以及会出现库存为负的情况
    @Select("select g.*,mg.stock_count, mg.start_date, mg.end_date,mg.miaosha_price from miaosha_goods mg left join goods g on mg.goods_id = g.id where g.id = #{goodsId}")
    public GoodsVO getGoodsVoByGoodsId(@Param("goodsId")long goodsId);

    //加上判断stock_count防止卖超
    @Update("update miaosha_goods set stock_count = stock_count - 1 where goods_id = #{goodsId} and stock_count>0")
    public int reduceStock(MiaoshaGoods goods);


}