package com.sztus.lib.back.end.basic.handler;

import com.alibaba.fastjson.JSON;
import com.sztus.lib.back.end.basic.utils.WebUtils;
import com.sztus.lib.back.end.basic.type.Result;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 授权失败处理器
 *
 * @author AustinWang*/
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // 处理异常
        Result result = new Result(HttpStatus.FORBIDDEN.value(), accessDeniedException.getMessage(), null);
        String json = JSON.toJSONString(result);
        WebUtils.renderString(response, json);
    }
}
