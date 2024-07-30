package com.iss.team.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.iss.team.domain.entity.Student;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TeamDTO {

    private String major;

    private String courseName;

    private Integer capacity;

    private Integer groupNumber;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deadline;
}
