package com.iss.auth.domain.dto;

import lombok.Data;

@Data
public class UserDTO {

    private Long id;

    private String username;

    private String password;

    private String major;

    private String schoolEmail;

    private String code;

    private String githubAccount;
}
