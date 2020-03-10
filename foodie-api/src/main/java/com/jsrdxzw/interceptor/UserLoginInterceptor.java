package com.jsrdxzw.interceptor;

import com.jsrdxzw.controller.BaseController;
import com.jsrdxzw.utils.JSONResult;
import com.jsrdxzw.utils.JsonUtils;
import com.jsrdxzw.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class UserLoginInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisOperator redisOperator;
    private static Logger logger = LoggerFactory.getLogger(UserLoginInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String headerUserId = request.getHeader("headerUserId");
        String headerUserToken = request.getHeader("headerUserToken");
        if (StringUtils.isNotBlank(headerUserId) && StringUtils.isNotBlank(headerUserToken)) {
            // 检查前端传来的token是否等于redis中保存的token
            String redisUserToken = redisOperator.get(BaseController.REDIS_USER_TOKEN + ":" + headerUserId);
            if (StringUtils.isBlank(redisUserToken)) {
                returnErrorResponse(response, JSONResult.errorMsg("请登录..."));
                return false;
            } else if (!headerUserToken.equals(redisUserToken)) {
                returnErrorResponse(response, JSONResult.errorMsg("账号在异地登录，请重新登录..."));
                // 这个情况下账号可能在异地登录。
                return false;
            } else {
                return true;
            }
        }
        returnErrorResponse(response, JSONResult.errorMsg("请登录..."));
        return false;
    }

    private void returnErrorResponse(HttpServletResponse response, JSONResult result) {
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/json");
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            outputStream.write(Objects.requireNonNull(JsonUtils.objectToJson(result)).getBytes());
            outputStream.flush();
        } catch (IOException e) {
            logger.error("returnErrorResponse error, result is :{}", result);
        }
    }
}
