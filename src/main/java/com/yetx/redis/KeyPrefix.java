package com.yetx.redis;

public interface KeyPrefix {
    String getPrefix();
    int getExpireSeconds();
}
