package com.yetx.controller;

import com.yetx.common.Result;
import com.yetx.common.ResultStatus;
import com.yetx.constant.MiaoshaStatus;
import com.yetx.pojo.*;
import com.yetx.redis.MiaoshaGoodsKey;
import com.yetx.redis.MiaoshaOrderKey;
import com.yetx.redis.RedisService;
import com.yetx.service.*;
import com.yetx.vo.GoodsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/miaosha")
public class MiaoshaController {
    @Autowired
    MiaoshaUserService userService;
    @Autowired
    RedisService redisService;
    @Autowired
    GoodsService goodsService;
    @Autowired
    OrderService orderService;
    @Autowired
    MiaoshaService miaoshaService;
    @Autowired
    MQSender mqSender;

    private Map<Long, Boolean> localOverMap = new HashMap<>();

    @PostMapping("/do_miaosha")
    @ResponseBody
    public Result doMiaosha(Model model, MiaoshaUser user,
                                       @RequestParam("goodsId")long goodsId){
        if(user==null)
            return Result.error(ResultStatus.SESSION_ERROR);
//        // 查看redis是否缓存有该goods, 没必要了，直接减reids就知道存不存在了
//        GoodsVO goods = goodsService.getGoodsVoByGoodsId(goodsId);
//        if(goods==null)
//            return Result.error(ResultStatus.GOODS_NOT_EXIST);

        // 防止每次请求都要去访问redis，可以设置一个localMap,一旦无库存了直接返回
        if(localOverMap.get(goodsId)!=null){
            return Result.error(ResultStatus.MIAO_SHA_DONE);
        }
        // 判断是否为重复秒杀,使用Redis判断,先判断，防止同一个人一直减去Redis的库存导致其它人无机会
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(),goodsId);
        if(order != null) {
            return Result.error(ResultStatus.REPEATE_MIAOSHA);
        }
        // 预先减去库存,以前是查数据库，再判断库存，现在改为程序启动时，读入初始库存到缓存中，只允许修改缓存初始stock次
        long decrRes = redisService.decr(MiaoshaGoodsKey.miaoshaGoodsStockKey,""+goodsId);
        if(decrRes<0){
            localOverMap.put(goodsId,true);
            return Result.error(ResultStatus.MIAO_SHA_DONE);
        }


        // 发送消息
        mqSender.produceMiaoshaWork(new MiaoshaMessage(user,goodsId));
        return Result.success(MiaoshaStatus.PENDING); //排队中
    }
    @GetMapping("/result")
    @ResponseBody
    public Result getMiaoshaResult(MiaoshaUser user,
                            @RequestParam("goodsId")long goodsId){
        if(user==null){
            return Result.error(ResultStatus.SESSION_ERROR);
        }
        log.info("receive user:{}，gooddsId:{}",user,goodsId);
        long result = miaoshaService.getMiaoshaResult(user,goodsId);
        return Result.success(result);
    }



    @PostMapping("/reset")
    @ResponseBody
    public Result resetGoods(){
        List<GoodsVO> goodsList = goodsService.listGoodsVo();
        if(goodsList==null)
            return Result.error(ResultStatus.GOODS_NOT_EXIST);
        for(GoodsVO goodsVO : goodsList){
            goodsVO.setStockCount(10);
            redisService.set(MiaoshaGoodsKey.miaoshaGoodsStockKey,""+goodsVO.getId(), goodsVO.getStockCount());
        }
        localOverMap.clear();
        //清空redis中创建的订单与已经结束的goods
        redisService.delete(MiaoshaOrderKey.miaoshaOrderByUidGid);
        redisService.delete(MiaoshaGoodsKey.miaoshaGoodsOverKey);

        //清空数据库
        miaoshaService.resetAllOrderInDB(goodsList);
        return Result.success(true);
    }
}
