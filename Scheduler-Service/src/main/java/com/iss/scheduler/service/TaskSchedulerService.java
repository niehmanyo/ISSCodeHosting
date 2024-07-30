package com.iss.scheduler.service;

import org.quartz.SchedulerException;

import java.time.LocalDateTime;

public interface TaskSchedulerService {
    void scheduleTaskJob(String courseName, LocalDateTime scheduleTime) throws SchedulerException;

    void deleteScheduledTaskJob(Long teamId) throws SchedulerException;

    void rescheduleTaskJob(Long teamId, LocalDateTime newScheduleTime) throws SchedulerException;
}
