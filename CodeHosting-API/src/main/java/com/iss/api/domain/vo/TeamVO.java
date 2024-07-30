package com.iss.api.domain.vo;

import lombok.Data;

import java.util.List;

@Data
public class TeamVO {

    private Long id;

    private String project_url;

    private String team_name;

    private Long project_id;

    private String major_name;

    private Integer capacity;

    private String course_name;

    private List<StudentVO> users;
}
