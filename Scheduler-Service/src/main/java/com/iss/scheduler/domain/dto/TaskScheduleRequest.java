package com.iss.scheduler.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskScheduleRequest {

    private Long taskId;

    private String courseName;

    private LocalDateTime scheduleTime;
}
