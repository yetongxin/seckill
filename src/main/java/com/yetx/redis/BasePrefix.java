package com.yetx.redis;

public class BasePrefix implements KeyPrefix{
    private String prefix;
    private int expireSeconds;

    public BasePrefix(String prefix, int expireSeconds) {
        this.prefix = prefix;
        this.expireSeconds = expireSeconds;
    }

    public BasePrefix(String prefix) {
        this.expireSeconds = 0;//0代表永不过期
        this.prefix = prefix;
    }

    @Override
    public String getPrefix() {
        return this.getClass().getSimpleName()+":"+prefix;
    }

    @Override
    public int getExpireSeconds() {
        return this.expireSeconds;
    }
}
