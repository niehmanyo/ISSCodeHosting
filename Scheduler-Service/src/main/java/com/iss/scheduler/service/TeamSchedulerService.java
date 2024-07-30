package com.iss.scheduler.service;

import org.quartz.SchedulerException;

import java.time.LocalDateTime;

public interface TeamSchedulerService {
    void scheduleTeamJob(Long teamId, LocalDateTime scheduleTime) throws SchedulerException;

    void deleteScheduledTeamJob(Long teamId) throws SchedulerException;

    void rescheduleTeamJob(Long teamId, LocalDateTime newScheduleTime) throws SchedulerException;
}
