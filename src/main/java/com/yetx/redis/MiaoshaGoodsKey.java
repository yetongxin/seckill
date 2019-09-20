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

    // MiaoshaGoodsKey:gList => 正在秒杀的商品json (REST)
    public static MiaoshaGoodsKey miaoshaGoodsListKey = new MiaoshaGoodsKey("gList",GOODSLIST_EXPIRETIME);

    // MiaoshaGoodsKey:glistHTML => 展示正在秒杀的商品HTML (后台渲染)
    public static MiaoshaGoodsKey miaoshaGoodsListHTMLKey = new MiaoshaGoodsKey("gListHTML",GOODSLIST_EXPIRETIME);

    // MiaoshaGoodsKey:gid12312312312 => GoodsVo
    public static MiaoshaGoodsKey miaoshaGoodsIdKey = new MiaoshaGoodsKey("gid",0);

    // MiaoshaGoodsKey:gstock12321321321  => stock
    public static MiaoshaGoodsKey miaoshaGoodsStockKey = new MiaoshaGoodsKey("gstock",0);

    // MiaoshaGoodsKey:gover12321321321  => true or false
    public static MiaoshaGoodsKey miaoshaGoodsOverKey = new MiaoshaGoodsKey("gover",0);


}
