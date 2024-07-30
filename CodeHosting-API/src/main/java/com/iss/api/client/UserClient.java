package com.iss.api.client;

import com.iss.api.domain.vo.UserVO;
import com.iss.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "auth-service", path = "/users")
public interface UserClient {
    @GetMapping("{userId}")
    Result<UserVO> getUserInfo(@PathVariable("userId") Long userId);

    @GetMapping("mapping")
    Result<String> searchUsernameByGithubAccount(@RequestParam String githubAccount);

    @GetMapping("/name/{username}")
    Result<UserVO> getUserInfoByName(@PathVariable("username") String username);
}
