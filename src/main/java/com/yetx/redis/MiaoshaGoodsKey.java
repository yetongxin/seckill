package com.yetx.redis;

public class MiaoshaGoodsKey extends BasePrefix
{
    private static final int GOODSLIST_EXPIRETIME = 60;

    public MiaoshaGoodsKey(String prefix, int expireSeconds) {
        super(prefix, expireSeconds);
    }

    public MiaoshaGoodsKey(String prefix) {
        super(prefix);
    }

    public static MiaoshaGoodsKey miaoshaGoodsListKey = new MiaoshaGoodsKey("gList",GOODSLIST_EXPIRETIME);
    public static MiaoshaGoodsKey miaoshaGoodsListHTMLKey = new MiaoshaGoodsKey("gListHTML",GOODSLIST_EXPIRETIME);


}
