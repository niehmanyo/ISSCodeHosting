package com.iss.auth.controller;

import com.iss.auth.domain.vo.UserLoginVO;
import com.iss.auth.service.IUserService;
import com.iss.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags = "Third Party Login Interface")
@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class OAuthController {

    private final IUserService userService;

    @ApiOperation("Third Party Login Interface")
    @PostMapping("redirect")
    public Result<UserLoginVO> loginWith3rdParty(@RequestParam String type, @RequestParam String code) {
        UserLoginVO userLoginVO = userService.loginWith3rdParty(code, type);
        System.out.println("!111111");
        return Result.success(userLoginVO);
    }
}
