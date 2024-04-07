package com.sztus.lib.back.end.basic.service.impl;

import com.alibaba.fastjson.JSON;
import com.sztus.lib.back.end.basic.config.RedisCache;
import com.sztus.lib.back.end.basic.object.domain.LoginUser;
import com.sztus.lib.back.end.basic.object.domain.User;
import com.sztus.lib.back.end.basic.type.Result;
import com.sztus.lib.back.end.basic.utils.JwtUtil;
import com.sztus.lib.back.end.basic.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class LoginUserImpl implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCache redisCache;

    @Override
    public Result login(User user) {
        // AuthenticationManager的authenticate方法进行用户认证
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        // 如果认证没通过，给出对应提示
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("认证失败！");
        }
        // 如果认证通过了，使用UUID(用户ID)生成JWT
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        // 把完整的用户信息存入redis
        String uuid = JwtUtil.getUUID();
        String jwt = JwtUtil.createJWT(uuid);
        loginUser.setUuid(uuid);
        redisCache.setCacheObject("login_" + uuid, JSON.toJSONString(loginUser));
        // 把token响应给前端
        Map<String, String> map = new HashMap<>();
        map.put("token", jwt);
        return Result.ok(map);
    }

    @Override
    public Result logout() {
        // 获取SecurityContextHolder中的用户ID
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String uuid = loginUser.getUuid();
        // 删除redis中的值
        redisCache.deleteObject("login_" + uuid);
        return Result.ok();
    }

}
