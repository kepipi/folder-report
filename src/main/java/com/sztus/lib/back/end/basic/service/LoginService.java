package com.sztus.lib.back.end.basic.service;

import com.sztus.lib.back.end.basic.object.domain.User;
import com.sztus.lib.back.end.basic.type.Result;

/**
 * service接口
 * @author AustinWang
 */
public interface LoginService {
    /**
     * 登录
     * @param user 用户信息
     * @return ResponseResult 响应结果
     */
    Result login(User user);

    /**
     * 登出
     * @return ResponseResult 响应结果
     */
    Result logout();
}
