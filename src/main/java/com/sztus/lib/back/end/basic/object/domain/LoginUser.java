package com.sztus.lib.back.end.basic.object.domain;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 封装登录用的信息
 *
 * @author AustinWang
 *
 **/
@Data
@NoArgsConstructor
public class LoginUser implements UserDetails {

    private String uuid;

    private String username;

    private String password;

    // 存储权限信息
    private List<String> permission;

    // 存储SpringSecurity所需要的权限信息的集合
    @JSONField(serialize = false)
    // 由于redis的原因,该类型不能序列化
    private Set<GrantedAuthority> authorities;

    public LoginUser(String username, String password, List<String> permission) {
        this.username = username;
        this.password = password;
        this.permission = permission;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (Objects.nonNull(authorities)) {
            return authorities;
        }
        // 把permissions中字符串类型的权限信息转换成GrantedAuthority对象存入authorities中
        authorities = permission.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

