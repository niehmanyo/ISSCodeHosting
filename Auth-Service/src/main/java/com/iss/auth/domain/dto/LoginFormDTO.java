package com.iss.auth.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "登录表单实体")
public class LoginFormDTO {
    private String username;

    private String password;

    private Boolean rememberMe = false;

    private String schoolEmail;

    private String code;

    public LoginFormDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
