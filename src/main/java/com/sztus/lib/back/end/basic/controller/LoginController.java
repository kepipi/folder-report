package com.sztus.lib.back.end.basic.controller;

import com.sztus.lib.back.end.basic.object.domain.User;
import com.sztus.lib.back.end.basic.type.Result;
import com.sztus.lib.back.end.basic.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author AustinWang
 */
@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/user/login")
    public Result login(@RequestBody User user) {
        // 登录
        return loginService.login(user);
    }

    @GetMapping("/user/logout")
    public Result logout() {
        // 登出
        return loginService.logout();
    }
}
