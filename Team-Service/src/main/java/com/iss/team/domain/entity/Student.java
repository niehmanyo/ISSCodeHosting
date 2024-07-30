package com.iss.team.domain.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Column;

@Data
@TableName("iss_student")
public class Student {

    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "majorId")
    private Long majorId;

    @Column(name = "gitAccount")
    private String gitAccount;
}
