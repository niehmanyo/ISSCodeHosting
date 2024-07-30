package com.iss.scheduler.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TeamScheduleRequest {

    private Long teamId;

    private LocalDateTime scheduleTime;
}
