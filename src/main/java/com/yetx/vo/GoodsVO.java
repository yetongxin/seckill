package com.yetx.vo;

import com.yetx.pojo.Goods;
import lombok.Data;

import java.util.Date;

@Data
public class GoodsVO extends Goods {
    private Double miaoshaPrice;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
}
