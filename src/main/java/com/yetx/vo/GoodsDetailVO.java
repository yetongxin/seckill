package com.yetx.vo;

import com.yetx.pojo.MiaoshaUser;
import lombok.Data;

@Data
public class GoodsDetailVO {
    private GoodsVO goods;
    private MiaoshaUser user;
    private int miaoshaStatus = 0;
    private int remainSeconds = 0;
}
