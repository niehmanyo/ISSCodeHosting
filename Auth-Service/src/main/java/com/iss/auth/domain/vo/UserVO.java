package com.iss.auth.domain.vo;

import lombok.Data;

@Data
public class UserVO {
    private Long id;

    private String username;

    private String major;

    private String schoolEmail;

    private String githubAccount;
}
