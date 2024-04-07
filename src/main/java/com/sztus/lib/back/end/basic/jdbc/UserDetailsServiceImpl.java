package com.sztus.lib.back.end.basic.jdbc;

import com.sztus.lib.back.end.basic.dao.service.PermissionService;
import com.sztus.lib.back.end.basic.dao.service.UserService;
import com.sztus.lib.back.end.basic.object.domain.LoginUser;
import com.sztus.lib.back.end.basic.object.domain.Permission;
import com.sztus.lib.back.end.basic.object.domain.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 认证
 *
 * @Author zwp
 * @Date 2022/9/12 16:53
 **/
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private UserService userService;

    @Resource
    private PermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getUserByUsername(username);
        // 查询用户信息
        if (Objects.isNull(user)) {
            throw new RuntimeException("用户名错误！！！");
        }
        // 查询对应的权限信息
        List<Permission> permissionList = permissionService.getPermissionsByUserId(user.getId());

        // 把数据封装成LoginUser对象返回
        return new LoginUser(username, user.getPassword(), permissionList.stream().map(Permission::getSn).collect(Collectors.toList()));
    }
}
