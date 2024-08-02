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

    @Column(name = "team_name")
    private Integer team_name;

    @Column(name = "major")
    private String major;

    @Column(name = "course_name")
    private String course_name;

    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "deadline")
    private LocalDateTime deadline;



}
