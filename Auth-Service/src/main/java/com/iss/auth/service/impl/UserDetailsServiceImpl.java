package com.iss.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.iss.auth.domain.po.AuthUser;
import com.iss.auth.domain.po.User;
import com.iss.auth.mapper.MenuMapper;
import com.iss.auth.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    MenuMapper menuMapper;
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户名查询用户信息
//        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>().eq(User::getUsername, username);
        QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("username", username);
        User user = userMapper.selectOne(wrapper);

        //如果没有该用户就抛出异常
        if (Objects.isNull(user)) {
            throw new RuntimeException("用户名或密码错误");
        }

        //查询权限信息封装到AuthUser中
        List<String> permissions = menuMapper.selectPermsByUserId(user.getId());

        // 将用户信息封装到UserDetails实现类中
        return new AuthUser(user,permissions);
    }
}
