package com.yetx;

import com.yetx.pojo.MiaoshaUser;
import com.yetx.redis.MiaoshaUserKey;
import com.yetx.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class SeckillDemoApplicationTests {

    @Autowired
    RedisService redisService;
    @Test
    public void contextLoads() {
        MiaoshaUser user = redisService.get(MiaoshaUserKey.token,"4dd8af2f5ddb46cea29eff5b180e4944",MiaoshaUser.class);
        log.info(user.toString());
    }

}
