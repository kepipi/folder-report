package com.sztus.lib.back.end.basic.dao.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sztus.lib.back.end.basic.dao.mapper.PermissionMapper;
import com.sztus.lib.back.end.basic.dao.mapper.UserMapper;
import com.sztus.lib.back.end.basic.object.domain.Permission;
import com.sztus.lib.back.end.basic.object.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author: Austin
 * @date: 2024/4/3 14:30
 */
@Service
public class PermissionService extends ServiceImpl<PermissionMapper, Permission> {

    public List<Permission> getPermissionsByUserId(Long userId) {
        return list(Wrappers.<Permission>lambdaQuery().eq(Permission::getUserId, userId));
    }
}
