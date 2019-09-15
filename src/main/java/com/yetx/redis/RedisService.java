package com.yetx.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yetx.common.ResultStatus;
import com.yetx.exception.GlobalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

@Slf4j
@Service
public class RedisService {

    @Autowired
    JedisPool jedisPool;
    /**
     * 获取单个对象
     * */
    public <T> T get(KeyPrefix keyPrefix,String key,Class<T> clazz){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = keyPrefix.getPrefix()+key;
            String str = jedis.get(realKey);
            return stringToBean(str,clazz);
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * json array to List
     * @param keyPrefix keyPrefix Obj,which with timeout and prefix
     * @param key       realkey = prefix+key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> List<T> getList(KeyPrefix keyPrefix,String key,Class<T> clazz){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = keyPrefix.getPrefix()+key;
            String str = jedis.get(realKey);
            return JSONObject.parseArray(str,clazz);
        }finally {
            returnToPool(jedis);
        }
    }
    public <T> boolean setList(KeyPrefix prefix, String key, List<T> list){
        Jedis jedis = null;
        try {
            jedis =  jedisPool.getResource();
            String str = JSON.toJSONString(list);
            if(str == null || str.length() <= 0) {
                return false;
            }
            //生成真正的key
            String realKey  = prefix.getPrefix() + key;
            int seconds =  prefix.getExpireSeconds();
            if(seconds <= 0) {
                jedis.set(realKey, str);
            }else {
                jedis.setex(realKey, seconds, str);
            }
            return true;
        }finally {
            returnToPool(jedis);
        }
    }
    /**
     * 设置对象
     * */
    public <T> boolean set(KeyPrefix prefix, String key,  T value) {
        Jedis jedis = null;
        try {
            jedis =  jedisPool.getResource();
            String str = beanToString(value);
            if(str == null || str.length() <= 0) {
                return false;
            }
            //生成真正的key
            String realKey  = prefix.getPrefix() + key;
            int seconds =  prefix.getExpireSeconds();
            if(seconds <= 0) {
                jedis.set(realKey, str);
            }else {
                jedis.setex(realKey, seconds, str);
            }
            return true;
        }finally {
            returnToPool(jedis);
        }
    }
    /**
     * 判断key是否存在
     * */
    public <T> boolean exists(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis =  jedisPool.getResource();
            //生成真正的key
            String realKey  = prefix.getPrefix() + key;
            return  jedis.exists(realKey);
        }finally {
            returnToPool(jedis);
        }
    }


    /**
     * 增加值
     * */
    public <T> Long incr(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis =  jedisPool.getResource();
            //生成真正的key
            String realKey  = prefix.getPrefix() + key;
            return  jedis.incr(realKey);
        }finally {
            returnToPool(jedis);
        }
    }


    /**
     * 减少值
     * */
    public <T> Long decr(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis =  jedisPool.getResource();
            //生成真正的key
            String realKey  = prefix.getPrefix() + key;
            return  jedis.decr(realKey);
        }finally {
            returnToPool(jedis);
        }
    }
    /**
     * 设置key的有效期，单位是秒
     * @param key
     * @param exTime
     * @return
     */
    public Long expire(String key,int exTime){
        Jedis jedis = null;
        Long result = null;
        try {
            jedis =  jedisPool.getResource();
            result = jedis.expire(key,exTime);
        } catch (Exception e) {
            log.error("expire key:{} error",key,e);
            jedisPool.returnBrokenResource(jedis);
            return result;
        }
        jedisPool.returnResource(jedis);
        return result;
    }


    private <T> String beanToString(T value) {
        if(value == null) {
            return null;
        }
        Class<?> clazz = value.getClass();
        if(clazz == int.class || clazz == Integer.class) {
            return ""+value;
        }else if(clazz == String.class) {
            return (String)value;
        }else if(clazz == long.class || clazz == Long.class) {
            return ""+value;
        }else {
            return JSON.toJSONString(value);
        }
    }

    private <T> T stringToBean(String str,Class<T> clazz){
        if(str == null||str.length()<=0||clazz==null){
            return null;
        }
        if(clazz == int.class || clazz == Integer.class)
            return (T) Integer.valueOf(str);
        else if(clazz == String.class)
            return (T) str;
        else if(clazz == long.class||clazz == Long.class)
            return (T) Long.valueOf(str);
        else
            return JSON.toJavaObject(JSON.parseObject(str),clazz);
    }
    private void returnToPool(Jedis jedis) {
        if(jedis != null) {
            jedis.close();
        }
    }
}
