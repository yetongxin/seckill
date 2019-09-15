package com.yetx.controller;

import com.yetx.common.Result;
import com.yetx.common.ResultStatus;
import com.yetx.dao.GoodsMapper;
import com.yetx.pojo.MiaoshaUser;
import com.yetx.redis.MiaoshaGoodsKey;
import com.yetx.redis.RedisService;
import com.yetx.service.GoodsService;
import com.yetx.vo.GoodsDetailVO;
import com.yetx.vo.GoodsVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.context.webflux.SpringWebFluxContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


@Slf4j
@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    GoodsService goodsService;

    @Autowired
    RedisService redisService;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;
    // REST版
    @RequestMapping(value="/list2")
    @ResponseBody
    public Result<List<GoodsVO>> listGoods(Model model, MiaoshaUser user) {
        // 取缓存
        List<GoodsVO> goodsVOList = redisService.getList(MiaoshaGoodsKey.miaoshaGoodsListKey,"",GoodsVO.class);
        if(goodsVOList!=null&&goodsVOList.size()!=0){
            goodsVOList.forEach(System.out::println);
            return Result.success(goodsVOList);
        }
        // 取数据库并插入缓存
        List<GoodsVO> goodsList = goodsService.listGoodsVo();
        redisService.setList(MiaoshaGoodsKey.miaoshaGoodsListKey,"",goodsList);
        return Result.success(goodsList);
    }

    @RequestMapping(value="/to_list", produces="text/html")
    @ResponseBody
    public String list(HttpServletRequest request, HttpServletResponse response, Model model,MiaoshaUser user) {
        model.addAttribute("user", user);
//        取缓存
    	String html = redisService.get(MiaoshaGoodsKey.miaoshaGoodsListHTMLKey, "", String.class);
    	if(!StringUtils.isEmpty(html)) {
    		return html;
    	}
        List<GoodsVO> goodsList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsList);
//    	 return "goods_list";
        WebContext ctx = new WebContext(request,response,
                request.getServletContext(),request.getLocale(),model.asMap());
        //手动渲染
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", ctx);
        if(!StringUtils.isEmpty(html)) {
            redisService.set(MiaoshaGoodsKey.miaoshaGoodsListHTMLKey, "", html);
        }
        return html;
    }
    @RequestMapping("/detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVO> queryGoodsDetail(Model model, MiaoshaUser user, @PathVariable("goodsId")long goodsId, HttpServletRequest request){
        GoodsVO goods = goodsService.getGoodsVoByGoodsId(goodsId);
        if(goods==null)
            return Result.error(ResultStatus.GOODS_NOT_EXIST);
        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int miaoshaStatus = 0;
        int remainSeconds = 0;
        if(now < startAt ) {//秒杀还没开始，倒计时
            miaoshaStatus = 0;
            remainSeconds = (int)((startAt - now )/1000);
        }else  if(now > endAt){//秒杀已经结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        }else {//秒杀进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }

        GoodsDetailVO goodsDetailVO = new GoodsDetailVO();
        goodsDetailVO.setGoods(goods);
        goodsDetailVO.setUser(user);
        goodsDetailVO.setMiaoshaStatus(miaoshaStatus);
        goodsDetailVO.setRemainSeconds(remainSeconds);
        return Result.success(goodsDetailVO);
    }


}
