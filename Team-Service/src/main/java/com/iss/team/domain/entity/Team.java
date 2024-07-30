package com.iss.team.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@TableName("iss_team")
public class Team {

    @Id
    private Long id;

    @Column(name = "project_url")
    private String project_url;

    @Column(name = "team_name")
    private String team_name;

    @Column(name = "project_id")
    private Long project_id;

    @Column(name = "major_name")
    private String major_name;

    @Column(name = "course_name")
    private String course_name;

    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "project_access_token")
    private String project_access_token;

    @Column(name = "deadline")
    private LocalDateTime deadline;



}
