package com.iss.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iss.auth.domain.dto.LoginFormDTO;
import com.iss.auth.domain.dto.UserDTO;
import com.iss.auth.domain.po.User;
import com.iss.auth.domain.vo.UserLoginVO;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author 虎哥
 * @since 2023-05-05
 */
public interface IUserService extends IService<User> {

    UserLoginVO login(LoginFormDTO loginFormDTO);

    UserLoginVO loginWith3rdParty(String authCode, String loginType);

    UserLoginVO register(UserDTO userDTO);

    void logout();
}
