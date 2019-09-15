package com.yetx.vo;


import com.yetx.pojo.OrderInfo;
import com.yetx.vo.GoodsVO;
import lombok.Data;

@Data
public class OrderDetailVO {
    private GoodsVO goods;
    private OrderInfo order;
}
