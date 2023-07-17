package com.imooc.api.interceptors;

import com.imooc.exception.GraceException;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.utils.IPUtil;
import com.imooc.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.imooc.grace.result.ResponseStatusEnum.SMS_NEED_WAIT_ERROR;

public class PassportInterceptor implements HandlerInterceptor {
    @Autowired
    private RedisTemplate redisTemplate;

    public static final String MOBILE_SMSCODE = "mobile:smscode";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip = IPUtil.getRequestIp(request);
        if (redisTemplate.hasKey(MOBILE_SMSCODE+":"+ip)){
            GraceException.display(SMS_NEED_WAIT_ERROR);
            return false;
        };
        return true;
    }


}
