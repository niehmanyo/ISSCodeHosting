package com.iss.team.domain.vo;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class TeamVO {
    @Id
    private Long id;

    private String project_url;

    private String team_name;

    private Long project_id;

    private String major_name;

    private Integer capacity;

    private String course_name;

    private LocalDateTime deadline;

    private String project_access_token;

    private List<StudentVO> users;
}
