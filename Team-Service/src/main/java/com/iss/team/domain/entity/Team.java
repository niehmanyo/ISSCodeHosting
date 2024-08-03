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

    private Integer team_name;

    private String major;

    private String course_name;

    private Integer capacity;

    private LocalDateTime deadline;

    private String project_url;

}
