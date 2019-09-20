package com.yetx.pojo;

import lombok.Data;

@Data
public class MiaoshaMessage {
    public MiaoshaUser user;
    public long goodsID;

    public MiaoshaMessage(MiaoshaUser user, long goodsID) {
        this.user = user;
        this.goodsID = goodsID;
    }
}
