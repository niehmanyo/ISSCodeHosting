package com.iss.task.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskVO {

    private Long id;

    private Long LecturerId;

    private String majorName;

    private String moduleName;

    private String taskName;

    private String taskDescription;

    private Integer allowedAttempts;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deadline;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime availableTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime unavailableTime;
}
