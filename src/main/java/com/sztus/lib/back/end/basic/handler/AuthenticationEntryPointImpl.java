package com.sztus.lib.back.end.basic.handler;

import com.alibaba.fastjson.JSON;
import com.sztus.lib.back.end.basic.utils.WebUtils;
import com.sztus.lib.back.end.basic.type.Result;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证失败处理器
 *
 * @author AustinWang*/
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // 处理异常
        Result result = new Result(HttpStatus.UNAUTHORIZED.value(), authException.getMessage(), null);
        String json = JSON.toJSONString(result);
        WebUtils.renderString(response, json);
    }
}
