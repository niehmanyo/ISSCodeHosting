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

    private Integer team_name;

    private String major;

    private Integer capacity;

    private String course_name;

    private LocalDateTime deadline;

    private List<StudentVO> users;
}
