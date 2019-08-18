package com.yetx.service;

import com.yetx.common.ResultStatus;
import com.yetx.dao.MiaoshaUserMapper;
import com.yetx.exception.GlobalException;
import com.yetx.pojo.MiaoshaUser;
import com.yetx.redis.MiaoshaUserKey;
import com.yetx.redis.RedisService;
import com.yetx.utils.MD5Util;
import com.yetx.utils.UUIDUtil;
import com.yetx.vo.LoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Service
public class MiaoshaUserService {
    @Autowired
    MiaoshaUserMapper miaoshaUserMapper;
    @Autowired
    RedisService redisService;

    public static final String COOKIE_NAME_TOKEN = "token";


    public MiaoshaUser getByMobie(String mobile) {
//        取缓存
//        MiaoshaUser user = redisService.get(MiaoShaUserKey.getByNickName, ""+nickName, MiaoshaUser.class);
//        if(user != null) {
//            return user;
//        }
        //取数据库
        MiaoshaUser user = miaoshaUserMapper.selectByPrimaryKey(Long.parseLong(mobile));
//        if(user != null) {
//            redisService.set(MiaoShaUserKey.getByNickName, ""+nickName, user);
//        }
        return user;
    }
    public String login(HttpServletResponse response, LoginVO loginVO){
        if(loginVO == null)
            throw new GlobalException(ResultStatus.SYSTEM_ERROR);
        String mobile = loginVO.getMobile();
        String formPass = loginVO.getPassword();
        MiaoshaUser user = getByMobie(mobile);
        log.info(user.toString());
        //验证密码
        String dbPass = user.getPassword();
        String saltDB = user.getSalt();
        String calcPass = MD5Util.formPassToDBPass(formPass, saltDB);
        if(!calcPass.equals(dbPass)) {
            throw new GlobalException(ResultStatus.PASSWORD_ERROR);
        }

        String token = UUIDUtil.uuid();
        addCookie(response,token,user);
        return token;
    }
    public MiaoshaUser getUserByToken(HttpServletResponse response,String token){
        if(StringUtils.isEmpty(token))
            return null;
        MiaoshaUser user = redisService.get(MiaoshaUserKey.token,token,MiaoshaUser.class);
        if(user!=null)
            addCookie(response,token,user);
        return user;
    }
    private void addCookie(HttpServletResponse response, String token, MiaoshaUser user) {
        redisService.set(MiaoshaUserKey.token, token, user);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(MiaoshaUserKey.token.getExpireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
