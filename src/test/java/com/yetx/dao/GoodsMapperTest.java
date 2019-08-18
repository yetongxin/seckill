package com.yetx.dao;

import com.yetx.pojo.MiaoshaUser;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GoodsMapperTest {

    @Autowired
    MiaoshaUserMapper userMapper;
    Logger logger = LoggerFactory.getLogger(GoodsMapperTest.class);

    @Test
    public void selectByPrimaryKey() {
        logger.info(String.valueOf(userMapper==null));
        MiaoshaUser miaoshaUser = userMapper.selectByPrimaryKey(13000000000l);
        Assert.assertEquals("user0",miaoshaUser.getNickname());
        logger.info(miaoshaUser.getPassword());
    }
}