package com.iss.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iss.auth.adapter.Login3rdAdapter;
import com.iss.auth.adapter.Login3rdAdapterManager;
import com.iss.auth.domain.dto.UserDTO;
import com.iss.auth.domain.po.AuthUser;
import com.iss.auth.config.JwtProperties;
import com.iss.auth.domain.dto.LoginFormDTO;
import com.iss.auth.domain.po.User;
import com.iss.auth.domain.po.UserRole;
import com.iss.auth.domain.vo.UserLoginVO;
import com.iss.auth.encoder.PasswordGenerator;
import com.iss.auth.mapper.MenuMapper;
import com.iss.auth.mapper.UserMapper;
import com.iss.auth.mapper.UserRoleMapper;
import com.iss.auth.service.IUserService;
import com.iss.auth.utils.JwtTool;
import com.iss.common.utils.BeanUtils;
import com.iss.common.utils.RedisCache;
import com.iss.common.utils.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private final JwtTool jwtTool;

    private final JwtProperties jwtProperties;

    private final AuthenticationManager authenticationManager;

    private final RedisCache redisCache;

    private final Login3rdAdapterManager login3rdAdapterManager;

    private final UserMapper userMapper;

    private final MenuMapper menuMapper;

    private final UserRoleMapper userRoleMapper;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserLoginVO login(LoginFormDTO loginDTO) {
        // 1.封装Authentication对象
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());
        // 2.通过AuthenticationManager的authenticate方法来进行用户认证
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        // 3.在Authentication中获取用户信息
        AuthUser authUser = (AuthUser) authentication.getPrincipal();
        User user = authUser.getUser();
        // 4.认证通过生成token
        String token = jwtTool.createToken(user.getId(), jwtProperties.getTokenTTL());
        redisCache.setCacheObject("login:" + user.getId(), authUser.getAuthorities());
        // 5.封装VO返回
        return createUserLoginVO(user, token);
    }

    @Override
    public UserLoginVO loginWith3rdParty(String authCode, String type) {
        Login3rdAdapter login3rdAdapter = login3rdAdapterManager.getAdapter(type);
        User user = login3rdAdapter.login(authCode);
        QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("username", user.getUsername());
        User existingUser = userMapper.selectOne(wrapper);
        if (existingUser == null) {
            // 第一次第三方登录，注册用户并返回VO，引导用户填写专业和GitHub账号信息
            return createUserLoginVO(user, null);
        } else {
            // 用户已存在，生成token并缓存用户权限
            return getUserLoginVOAndStoreRedis(existingUser);
        }
    }

    @Override
    public UserLoginVO register(UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        String password = user.getPassword();
        if(password == null){
            PasswordGenerator.generateRandomPassword(12);
        }
        user.setPassword(passwordEncoder.encode(password));
        user.setStatus("1");
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        try {
            userMapper.insert(user);
            Long userId = user.getId();
            Long defaultRoleId = 1L;
            userRoleMapper.insert(new UserRole(userId, defaultRoleId));
        } catch (DuplicateKeyException e) {
            throw new RuntimeException("Account existed!");
        }
        return getUserLoginVOAndStoreRedis(user);
    }

    @Override
    public void logout() {
        redisCache.deleteObject("login:" + UserContext.getUser());
    }

    private UserLoginVO getUserLoginVOAndStoreRedis(User user) {
        String token = jwtTool.createToken(user.getId(), jwtProperties.getTokenTTL());
        List<String> permissions = menuMapper.selectPermsByUserId(user.getId());
        redisCache.setCacheObject("login:" + user.getId(), permissions);

        // 封装VO返回
        return createUserLoginVO(user, token);
    }

    private UserLoginVO createUserLoginVO(User user, String token) {
        UserLoginVO userLoginVO = new UserLoginVO();
        userLoginVO.setUserId(user.getId());
        userLoginVO.setUsername(user.getUsername());
        userLoginVO.setToken(token);
        return userLoginVO;
    }
}
