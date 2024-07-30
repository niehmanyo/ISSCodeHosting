package com.iss.auth.controller;

import com.iss.auth.domain.dto.LoginFormDTO;
import com.iss.auth.domain.dto.UserDTO;
import com.iss.auth.domain.po.User;
import com.iss.auth.domain.vo.UserLoginVO;
import com.iss.auth.domain.vo.UserVO;
import com.iss.auth.service.IUserService;
import com.iss.common.constant.AuthMessageConstant;
import com.iss.common.result.Result;
import com.iss.common.utils.BeanUtils;
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

    @ApiOperation("User Login By NUS Email Interface")
    @PostMapping("login/email")
    public Result<UserLoginVO> loginByNUSEmail(@RequestBody @Validated LoginFormDTO loginFormDTO){
        UserLoginVO userLoginVO = userService.loginByNUSEmail(loginFormDTO);
        return Result.success(userLoginVO);
    }

    @ApiOperation("Email Code Interface")
    @PostMapping("code")
    public Result<String> sendEmailCode(@RequestParam("nusEmail") String nusEmail){
        String result = userService.sendEmailCode(nusEmail);
        if (!result.equals(AuthMessageConstant.SEND_CODE_SUCCESSFULLY)){
            return Result.badRequest();
        }
        return Result.success(AuthMessageConstant.SEND_CODE_SUCCESSFULLY);
    }

    @ApiOperation("User Register Interface")
    @PostMapping("register")
    public Result<UserLoginVO> register(@RequestBody UserDTO userDTO){
        try {
            UserLoginVO userLoginVO = userService.register(userDTO);
            return Result.success(userLoginVO);
        } catch (Exception e) {
            return Result.error(extractErrorCode(e.getMessage()), e.getMessage());
        }
    }

    @ApiOperation("User Logout Interface")
    @PostMapping("logout")
    public Result<String> logout(){
        userService.logout();
        return Result.success();
    }

    @ApiOperation("Get User Interface")
    @GetMapping("{userId}")
    public Result<UserVO> getUserInfo(@PathVariable Long userId){
        User user = userService.getById(userId);
        UserVO userVO = BeanUtils.copyBean(user, UserVO.class);
        return Result.success(userVO);
    }

    @ApiOperation("Get Username By Github Account Interface")
    @GetMapping("mapping")
    public Result<String> searchUsernameByGithubAccount(@RequestParam String githubAccount){
        String username = userService.getUsernameByGithubAccount(githubAccount);
        return Result.success(username);
    }

    @ApiOperation("Edit User's Profile Interface")
    @PutMapping("edit/profile")
    public Result<UserVO> editUserProfile(@RequestBody UserDTO userDTO){
        try {
            UserVO userVO = BeanUtils.copyBean(userService.updateUserProfile(userDTO), UserVO.class);
            return Result.success("Edit Profile Successful", userVO);
        } catch (Exception e) {
            return Result.error(extractErrorCode(e.getMessage()), e.getMessage());
        }
    }

    @ApiOperation("Get User Interface By username")
    @GetMapping("name/{username}")
    public Result<UserVO> getUserInfoByName(@PathVariable String username){
        User user = userService.getUserByName(username);
        UserVO userVO = BeanUtils.copyBean(user, UserVO.class);
        return Result.success(userVO);
    }

    private int extractErrorCode(String errorMessage) {
        String[] parts = errorMessage.split(" ");
        for (String part : parts) {
            try {
                return Integer.parseInt(part);
            } catch (NumberFormatException ignored) {
                // Ignore parts that are not numbers
            }
        }
        return 500; // Default error code if not found
    }
}

