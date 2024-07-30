package com.iss.auth.service.impl;

import com.iss.auth.adapter.Login3rdAdapterManager;
import com.iss.auth.config.JwtProperties;
import com.iss.auth.domain.dto.LoginFormDTO;
import com.iss.auth.domain.dto.UserDTO;
import com.iss.auth.domain.po.AuthUser;
import com.iss.auth.domain.po.User;
import com.iss.auth.domain.vo.UserLoginVO;
import com.iss.auth.mapper.MenuMapper;
import com.iss.auth.mapper.UserMapper;
import com.iss.auth.mapper.UserRoleMapper;
import com.iss.auth.service.IMailService;
import com.iss.auth.service.impl.UserServiceImpl;
import com.iss.auth.utils.JwtTool;
import com.iss.common.constant.AuthMessageConstant;
import com.iss.common.constant.EmailConstant;
import com.iss.common.constant.RedisConstant;
import com.iss.common.utils.RedisCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @Mock
    private JwtTool jwtTool;

    @Mock
    private JwtProperties jwtProperties;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private RedisCache redisCache;

    @Mock
    private StringRedisTemplate stringRedisTemplate;

    @Mock
    private Login3rdAdapterManager login3rdAdapterManager;

    @Mock
    private UserMapper userMapper;

    @Mock
    private MenuMapper menuMapper;

    @Mock
    private UserRoleMapper userRoleMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private IMailService mailService;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock stringRedisTemplate opsForValue
        ValueOperations<String, String> valueOperations = mock(ValueOperations.class);
        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(any())).thenReturn(null);  // You may adjust this as needed
    }

    @Test
    void testLogin() {
        // Mock data
        LoginFormDTO loginDTO = new LoginFormDTO("username", "password");
        User user = new User();
        user.setId(1L);
        user.setUsername("username");

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(new AuthUser(user, Collections.emptyList()));
        when(jwtTool.createToken(anyLong(), any())).thenReturn("accessToken", "refreshToken");

        // Test method
        UserLoginVO userLoginVO = userService.login(loginDTO);

        // Assertions
        assertNotNull(userLoginVO);
        assertEquals(user.getId(), userLoginVO.getUserId());
        assertEquals(user.getUsername(), userLoginVO.getUsername());
        assertEquals("accessToken", userLoginVO.getAccessToken());
        assertEquals("refreshToken", userLoginVO.getRefreshToken());
    }

//    @Test
//    void testRegister() {
//        // Mock data
//        UserDTO userDTO = new UserDTO();
//        userDTO.setSchoolEmail("e1221487@u.nus.edu");
//        userDTO.setCode("123456");
//        userDTO.setPassword("password");
//
//        User user = new User();
//        user.setId(1L);
//        user.setUsername("username");
//
//        // Mock behavior for dependencies
//        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
//        when(userMapper.insert(any())).thenReturn(1);
//        when(userMapper.selectOne(any())).thenReturn(user);
//        when(jwtTool.createToken(anyLong(), any())).thenReturn("accessToken", "refreshToken");
//        when(menuMapper.selectPermsByUserId(anyLong())).thenReturn(Collections.singletonList("PERMISSION"));
//
//        // Test method
//        UserLoginVO userLoginVO = userService.register(userDTO);
//
//        // Assertions
//        assertNotNull(userLoginVO);
//        assertEquals(user.getId(), userLoginVO.getUserId());
//        assertEquals(user.getUsername(), userLoginVO.getUsername());
//        assertEquals("accessToken", userLoginVO.getAccessToken());
//        assertEquals("refreshToken", userLoginVO.getRefreshToken());
//
//        // Verify interactions
//        verify(passwordEncoder, times(1)).encode("password");
//        verify(userMapper, times(1)).insert(any());
//        verify(userMapper, times(1)).selectOne(any());
//        verify(jwtTool, times(2)).createToken(anyLong(), any());
//        verify(menuMapper, times(1)).selectPermsByUserId(anyLong());
//    }

    @Test
    void testSendEmailCode() {
        // Mock data
        String nusEmail = "e1221487@u.nus.edu";
        ValueOperations<String, String> valueOperations = mock(ValueOperations.class);
        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(any())).thenReturn(null);

        // Test method
        String result = userService.sendEmailCode(nusEmail);

        // Assertions
        assertEquals(AuthMessageConstant.SEND_CODE_SUCCESSFULLY, result);
        verify(mailService, times(1)).sendHtmlMail(eq(nusEmail), eq(EmailConstant.SUBJECT), anyString());
    }

}
