package com.yetx.redis;

public class MiaoshaOrderKey extends BasePrefix {
    public MiaoshaOrderKey(String prefix, int expireSeconds) {
        super(prefix, expireSeconds);
    }

    public MiaoshaOrderKey(String prefix) {
        super(prefix);
    }
    public static String convertOrderKey(long userId,long goodsId){
        return userId+"-"+goodsId;
    }
    // miaoshaOrderByUidGid:orderSuccess123-456 : order
    public static MiaoshaOrderKey miaoshaOrderByUidGid = new MiaoshaOrderKey("orderSuccess",0);
}
