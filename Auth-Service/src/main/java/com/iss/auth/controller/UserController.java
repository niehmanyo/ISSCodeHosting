package com.iss.auth.controller;

import com.iss.auth.domain.dto.LoginFormDTO;
import com.iss.auth.domain.dto.UserDTO;
import com.iss.auth.domain.vo.UserLoginVO;
import com.iss.auth.service.IUserService;
import com.iss.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = "User Interface")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @ApiOperation("User Login Interface")
    @PostMapping("login")
    public Result<UserLoginVO> login(@RequestBody @Validated LoginFormDTO loginFormDTO){
        UserLoginVO userLoginVO = userService.login(loginFormDTO);
        return Result.success(userLoginVO);
    }

    @ApiOperation("Third Party Login Interface")
    @PostMapping("login/third-party")
    public Result<UserLoginVO> loginWith3rdParty(@RequestParam String authCode, @RequestParam String loginType) {
        UserLoginVO userLoginVO = userService.loginWith3rdParty(authCode, loginType);
        return Result.success(userLoginVO);
    }

    @ApiOperation("User Register Interface")
    @PostMapping("register")
    public Result<UserLoginVO> register(@RequestBody UserDTO userDTO){
        UserLoginVO userLoginVO = userService.register(userDTO);
        return Result.success(userLoginVO);
    }

    @ApiOperation("User Logout Interface")
    @PostMapping("logout")
    public Result<String> logout(){
        userService.logout();
        return Result.success();
    }
}

