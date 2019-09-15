package com.yetx.controller;

import com.yetx.common.Result;
import com.yetx.common.ResultStatus;
import com.yetx.pojo.MiaoshaUser;
import com.yetx.pojo.OrderInfo;
import com.yetx.redis.RedisService;
import com.yetx.service.GoodsService;
import com.yetx.service.MiaoshaUserService;
import com.yetx.service.OrderService;
import com.yetx.vo.GoodsVO;
import com.yetx.vo.OrderDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller
@RequestMapping("/order")
public class OrderController {

	@Autowired
	MiaoshaUserService userService;
	
	@Autowired
	RedisService redisService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	GoodsService goodsService;
	
    @RequestMapping("/detail")
    @ResponseBody
    public Result<OrderDetailVO> info(Model model, MiaoshaUser user,
									  @RequestParam("orderId") long orderId) {
    	if(user == null) {
    		return Result.error(ResultStatus.SESSION_ERROR);
    	}
    	OrderInfo order = orderService.getMiaoshaOrderByOrderId(orderId);
    	if(order == null) {
    		return Result.error(ResultStatus.ORDER_NOT_EXIST);
    	}
    	long goodsId = order.getGoodsId();
    	GoodsVO goods = goodsService.getGoodsVoByGoodsId(goodsId);
    	OrderDetailVO vo = new OrderDetailVO();
    	vo.setOrder(order);
    	vo.setGoods(goods);
    	return Result.success(vo);
    }
    
}
