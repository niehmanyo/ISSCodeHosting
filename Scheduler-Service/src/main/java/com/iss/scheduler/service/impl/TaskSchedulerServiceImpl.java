package com.iss.scheduler.service.impl;

import com.iss.scheduler.jobs.TaskSchedulerJob;
import com.iss.scheduler.service.TaskSchedulerService;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class TaskSchedulerServiceImpl implements TaskSchedulerService {

    private final Scheduler scheduler;

    /**
     * Schedules a new job for a task at a specified time.
     *
     * @param courseName       the ID of the task
     * @param scheduleTime the time at which the job should be executed
     * @throws SchedulerException if there is an error in scheduling the job
     */
    @Override
    public void scheduleTaskJob(String courseName, LocalDateTime scheduleTime) throws SchedulerException {
        // Create a JobDetail for the task job with the provided taskId
        JobDetail jobDetail = JobBuilder.newJob(TaskSchedulerJob.class)
                .withIdentity("taskSchedulerJob" + courseName)
                .usingJobData("courseName", courseName)
                .storeDurably()
                .build();

        // Create a Trigger to schedule the job at the specified time
        Trigger trigger = TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity("taskSchedulerTrigger" + courseName)
                .startAt(Date.from(scheduleTime.atZone(ZoneId.systemDefault()).toInstant()))
                .build();

        // Schedule the job with the created JobDetail and Trigger
        scheduler.scheduleJob(jobDetail, trigger);
    }

    /**
     * Deletes a scheduled job for a task.
     *
     * @param taskId the ID of the task
     * @throws SchedulerException if there is an error in deleting the job
     */
    @Override
    public void deleteScheduledTaskJob(Long taskId) throws SchedulerException {
        // Create a JobKey using the teamId to identify the job
        JobKey jobKey = new JobKey("taskSchedulerJob" + taskId);

        // Delete the job from the scheduler
        scheduler.deleteJob(jobKey);
    }

    /**
     * Reschedules an existing job for a team to a new specified time.
     *
     * @param taskId the ID of the task
     * @param newScheduleTime the new time at which the job should be executed
     * @throws SchedulerException if there is an error in rescheduling the job
     */
    @Override
    public void rescheduleTaskJob(Long taskId, LocalDateTime newScheduleTime) throws SchedulerException {
        // Create a TriggerKey using the taskId to identify the trigger
        TriggerKey triggerKey = new TriggerKey("taskSchedulerTrigger" + taskId);

        // Create a new Trigger to reschedule the job at the new specified time
        Trigger newTrigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerKey)
                .startAt(Date.from(newScheduleTime.atZone(ZoneId.systemDefault()).toInstant()))
                .build();

        // Reschedule the job with the new Trigger
        scheduler.rescheduleJob(triggerKey, newTrigger);
    }
}
