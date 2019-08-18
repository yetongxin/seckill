package com.yetx.redis;


public class MiaoshaUserKey extends BasePrefix{

    public final static int EXPIRE_TIME = 3600*24*2;
    private MiaoshaUserKey(String prefix, int expireSeconds) {
        super(prefix, expireSeconds);
    }


    public static MiaoshaUserKey token = new MiaoshaUserKey("tk",EXPIRE_TIME);

}
