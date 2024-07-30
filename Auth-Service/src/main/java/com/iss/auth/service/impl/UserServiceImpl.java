package com.iss.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iss.api.client.GitlabClient;
import com.iss.auth.adapter.Login3rdAdapterManager;
import com.iss.auth.domain.dto.UserDTO;
import com.iss.auth.domain.po.AuthUser;
import com.iss.auth.config.JwtProperties;
import com.iss.auth.domain.dto.LoginFormDTO;
import com.iss.auth.domain.po.User;
import com.iss.auth.domain.po.UserRole;
import com.iss.auth.domain.vo.UserLoginVO;
import com.iss.auth.mapper.MenuMapper;
import com.iss.auth.mapper.UserMapper;
import com.iss.auth.mapper.UserRoleMapper;
import com.iss.auth.service.IMailService;
import com.iss.auth.service.IUserService;
import com.iss.auth.utils.JwtTool;
import com.iss.common.constant.AuthMessageConstant;
import com.iss.common.constant.EmailConstant;
import com.iss.common.constant.RedisConstant;
import com.iss.common.utils.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.GitLabApiException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private final JwtTool jwtTool;

    private final JwtProperties jwtProperties;

    private final AuthenticationManager authenticationManager;

    private final RedisCache redisCache;

    private final StringRedisTemplate stringRedisTemplate;

    private final Login3rdAdapterManager login3rdAdapterManager;

    private final UserMapper userMapper;

    private final MenuMapper menuMapper;

    private final UserRoleMapper userRoleMapper;

    private final PasswordEncoder passwordEncoder;

    private final IMailService mailService;

    private final GitlabClient gitlabClient;

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
        String accessToken = jwtTool.createToken(user.getId(), jwtProperties.getAccessTokenTTL());
        String refreshToken = jwtTool.createToken(user.getId(), jwtProperties.getRefreshTokenTTL());
        // 5.存储权限信息到redis
        redisCache.setCacheObject(RedisConstant.PERMISSION_KEY + user.getId(), authUser.getAuthorities());
        // 5.封装VO返回
        UserLoginVO userLoginVO = new UserLoginVO();
        userLoginVO.setUserId(user.getId());
        userLoginVO.setUsername(user.getUsername());
        userLoginVO.setAccessToken(accessToken);
        userLoginVO.setRefreshToken(refreshToken);
        return userLoginVO;
    }

    @Override
    public UserLoginVO loginByNUSEmail(LoginFormDTO loginFormDTO) {
        // 1. Check Email Code
        String cacheCode = stringRedisTemplate.opsForValue().get(RedisConstant.EMAIL_CODE_KEY + loginFormDTO.getSchoolEmail());
        String code = loginFormDTO.getCode();
        if (cacheCode == null || !cacheCode.equals(code)){
            throw new RuntimeException(AuthMessageConstant.INVALID_CODE);
        }
        // 2. Find User by NUS Email
        QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("school_email", loginFormDTO.getSchoolEmail());
        User user = userMapper.selectOne(wrapper);
        if (user == null) {
            throw new RuntimeException(AuthMessageConstant.ACCOUNT_NOT_FOUND);
        }
        // 3. Get Permissions
        List<String> permissions = menuMapper.selectPermsByUserId(user.getId());
        List<GrantedAuthority> authorities = permissions.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        // 4. Generate JWT Token
        String accessToken = jwtTool.createToken(user.getId(), jwtProperties.getAccessTokenTTL());
        String refreshToken = jwtTool.createToken(user.getId(), jwtProperties.getRefreshTokenTTL());
        // 5. Save to Redis
        redisCache.setCacheObject(RedisConstant.PERMISSION_KEY + user.getId(), authorities);
        // 6. Set UserLoginVO
        UserLoginVO userLoginVO = new UserLoginVO();
        userLoginVO.setUserId(user.getId());
        userLoginVO.setUsername(user.getUsername());
        userLoginVO.setAccessToken(accessToken);
        userLoginVO.setRefreshToken(refreshToken);
        return userLoginVO;
    }

    @Override
    public UserLoginVO loginWith3rdParty(String authCode, String type) {
//        Login3rdAdapter login3rdAdapter = login3rdAdapterManager.getAdapter(type);
//        User user = login3rdAdapter.login(authCode);
//        QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("username", user.getUsername());
//        User existingUser = userMapper.selectOne(wrapper);
//        if (existingUser == null) {
//            // 第一次第三方登录，注册用户并返回VO，引导用户填写专业和GitHub账号信息
//            return createUserLoginVO(user, null, null);
//        } else {
//            // 用户已存在，生成token并缓存用户权限
//            return getUserLoginVOAndStoreRedis(existingUser);
//        }
        return null;
    }

    @Override
    @Transactional
    public UserLoginVO register(UserDTO userDTO) throws GitLabApiException {
        // 1. Copy Properties
        User user = BeanUtils.copyBean(userDTO, User.class);
        // 2. Encrypt Password
        String password = user.getPassword();
//        if(password == null){
//            PasswordGenerator.generateRandomPassword(12);
//        }
        user.setPassword(passwordEncoder.encode(password));
        // 3. Valid Email Code
        String cacheCode = stringRedisTemplate.opsForValue().get(RedisConstant.EMAIL_CODE_KEY + userDTO.getSchoolEmail());
        String code = userDTO.getCode();
        if (cacheCode == null || !cacheCode.equals(code)){
            throw new RuntimeException(AuthMessageConstant.INVALID_CODE);
        }
        // 4. Set Status
        user.setStatus("1");
        // 5. Insert into Database if not exist
        try {
            userMapper.insert(user);
            Long userId = user.getId();
            userRoleMapper.insert(new UserRole(userId, AuthMessageConstant.DEFAULT_ROLE_ID));
        } catch (DuplicateKeyException e) {
            throw new RuntimeException(AuthMessageConstant.ALREADY_EXISTS);
        }
        // 6. Get UserId
        //LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<User>().eq(User::getSchoolEmail, user.getSchoolEmail());
        QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("school_email", user.getSchoolEmail());
        user = userMapper.selectOne(wrapper);
        // 7. Generate JWT Token
        String accessToken = jwtTool.createToken(user.getId(), jwtProperties.getAccessTokenTTL());
        String refreshToken = jwtTool.createToken(user.getId(), jwtProperties.getRefreshTokenTTL());
        // 8. Save Permissions to Redis
        List<String> permissions = menuMapper.selectPermsByUserId(user.getId());
        redisCache.setCacheObject(RedisConstant.PERMISSION_KEY + user.getId(), permissions);
        // 9. Create UserLoginVO
        UserLoginVO userLoginVO = new UserLoginVO();
        userLoginVO.setUserId(user.getId());
        userLoginVO.setUsername(user.getUsername());
        userLoginVO.setAccessToken(accessToken);
        userLoginVO.setRefreshToken(refreshToken);
        // 10. Create Related User in Gitlab
        gitlabClient.signUpUser(user.getSchoolEmail(), user.getUsername(), user.getUsername());

        return userLoginVO;
    }

    @Override
    public String sendEmailCode(String nusEmail) {
        // 1. Verify NUS Email
        if (!RegexUtils.isNusEmail(nusEmail)){
            // Invalid NUS Email then return
            return AuthMessageConstant.INVALID_EMAIL;
        }
        // 2. Generate Random Code
        String code = RandomUtils.generateAlphaNumericCode(6);
        // 3. Save Code to Redis
        stringRedisTemplate.opsForValue().set(RedisConstant.EMAIL_CODE_KEY + nusEmail, code, RedisConstant.EMAIL_CODE_TTL, TimeUnit.MINUTES);
        // 4. Send COde to Email
        String htmlContent = "<html>" +
                "<body style=\"font-family: Arial, sans-serif;\">" +
                "<div style=\"max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ddd; border-radius: 5px;\">" +
                "<h2 style=\"color: #333;\">Your Verification Code</h2>" +
                "<p style=\"font-size: 16px;\">Dear User,</p>" +
                "<p style=\"font-size: 16px;\">Your verification code is:</p>" +
                "<div style=\"font-size: 24px; font-weight: bold; color: #007BFF; margin: 10px 0;\">" + code + "</div>" +
                "<p style=\"font-size: 16px;\">Please use this code to verify your email address.</p>" +
                "<p style=\"font-size: 16px;\">Thank you,</p>" +
                "<p style=\"font-size: 16px;\">ISS Code Hosting Development Team</p>" +
                "</div>" +
                "</body>" +
                "</html>";
        mailService.sendHtmlMail(nusEmail, EmailConstant.SUBJECT, htmlContent);
        return AuthMessageConstant.SEND_CODE_SUCCESSFULLY;
    }

    @Override
    public void logout() {
        redisCache.deleteObject(RedisConstant.PERMISSION_KEY + UserContext.getUser());
    }

    @Override
    public String getUsernameByGithubAccount(String githubAccount) {
        return userMapper.getUsernameByGithubAccount(githubAccount);
    }

    @Override
    public User getUserByName(String username){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        return this.getOne(queryWrapper);
    }
    @Override
    public User updateUserProfile(UserDTO userDTO) {
        User user = getById(userDTO.getId());

        // 如果用户传递了密码，则对密码进行加密
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        } else {
            // 保留原来的密码
            userDTO.setPassword(user.getPassword());
        }

        BeanUtils.copyProperties(userDTO, user);
        updateById(user);
        return user;
    }

    //    private UserLoginVO getUserLoginVOAndStoreRedis(User user) {
//        String accessToken = jwtTool.createToken(user.getId(), jwtProperties.getAccessTokenTTL());
//        String refreshToken = jwtTool.createToken(user.getId(), jwtProperties.getRefreshTokenTTL());
//        List<String> permissions = menuMapper.selectPermsByUserId(user.getId());
//        redisCache.setCacheObject("login:" + user.getId(), permissions);
//
//        // 封装VO返回
//        return createUserLoginVO(user, accessToken, refreshToken);
//    }

//    private UserLoginVO createUserLoginVO(User user, String token, String refreshToken) {
//        UserLoginVO userLoginVO = new UserLoginVO();
//        userLoginVO.setUserId(user.getId());
//        userLoginVO.setUsername(user.getUsername());
//        userLoginVO.setAccessToken(token);
//        userLoginVO.setRefreshToken(refreshToken);
//        return userLoginVO;
//    }
}
