package com.yetx.common;

public enum ResultStatus {
    SUCCESS(0,"成功"),
    FAILD(-1, "失败"),
    EXCEPTION(-1, "系统异常"),
    PARAM_ERROR(10000, "参数错误"),
    SYSTEM_ERROR(10001, "系统错误"),


    //登录模块 5002XX
    SESSION_ERROR(500210, "Session不存在或者已经失效"),
    PASSWORD_EMPTY(500211, "登录密码不能为空"),
    MOBILE_EMPTY(500212, "手机号不能为空"),
    MOBILE_ERROR (500213, "手机号格式错误"),
    MOBILE_NOT_EXIST(500214, "手机号不存在"),
    PASSWORD_ERROR(500215, "密码错误"),


    MIAO_SHA_DONE(500500, "商品已经秒杀完毕"),
    REPEATE_MIAOSHA(500501,"重复秒杀"),
    GOODS_NOT_EXIST(500502,"商品不存在"),


    ORDER_NOT_EXIST(500600,"订单不存在"),

    REDIS_ERROR(500701,"redis错误"),

    BIND_ERROR(500801, "参数校验错误");
    private int code;
    private String msg;

    ResultStatus(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
