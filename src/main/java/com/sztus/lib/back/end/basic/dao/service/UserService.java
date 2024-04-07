package com.sztus.lib.back.end.basic.dao.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sztus.lib.back.end.basic.dao.mapper.UserMapper;
import com.sztus.lib.back.end.basic.object.domain.User;
import org.springframework.stereotype.Service;


/**
 * @author: Austin
 * @date: 2024/4/3 14:30
 */
@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    public User getUserByUsername(String username) {
        return getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
    }

}
