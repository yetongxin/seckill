package com.yetx.controller;

import com.yetx.common.Result;
import com.yetx.common.ResultStatus;
import com.yetx.pojo.MiaoshaOrder;
import com.yetx.pojo.MiaoshaUser;
import com.yetx.pojo.OrderInfo;
import com.yetx.redis.RedisService;
import com.yetx.service.GoodsService;
import com.yetx.service.MiaoshaService;
import com.yetx.service.MiaoshaUserService;
import com.yetx.service.OrderService;
import com.yetx.vo.GoodsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
    @RequestMapping("/do_miaosha")
    @ResponseBody
    public Result<OrderInfo> doMiaosha(Model model, MiaoshaUser user,
                                       @RequestParam("goodsId")long goodsId){
//        model.addAttribute("user",user);
        if(user==null)
            return Result.error(ResultStatus.SESSION_ERROR);
        GoodsVO goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if(stock<=0){
            model.addAttribute("errmsg", ResultStatus.MIAO_SHA_DONE.getMsg());
            return Result.error(ResultStatus.MIAO_SHA_DONE);
        }

        //判断是否为重复秒杀
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(),goodsId);
        if(order != null) {
            return Result.error(ResultStatus.REPEATE_MIAOSHA);
        }
        //创建订单,以下应为事务性操作
        OrderInfo orderInfo = miaoshaService.miaosha(user,goods);
//        model.addAttribute("orderInfo",orderInfo);
//        model.addAttribute("goods",goods);
//        return "order_detail";
        return Result.success(orderInfo);
    }

}
