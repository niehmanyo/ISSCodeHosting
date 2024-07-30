package com.iss.auth.domain.vo;

import lombok.Data;

@Data
public class UserLoginVO {
    private Long userId;
    private String username;
    private String accessToken;
    private String refreshToken;
}
