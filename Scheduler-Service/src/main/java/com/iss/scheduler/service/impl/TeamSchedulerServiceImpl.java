package com.iss.scheduler.service.impl;

import com.iss.scheduler.jobs.TeamSchedulerJob;
import com.iss.scheduler.service.TeamSchedulerService;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Implementation of SchedulerService to manage team-related scheduled jobs using Quartz.
 */
@Service
@RequiredArgsConstructor
public class TeamSchedulerServiceImpl implements TeamSchedulerService {

    private final Scheduler scheduler;

    /**
     * Schedules a new job for a team at a specified time.
     *
     * @param teamId the ID of the team
     * @param scheduleTime the time at which the job should be executed
     * @throws SchedulerException if there is an error in scheduling the job
     */
    @Override
    public void scheduleTeamJob(Long teamId, LocalDateTime scheduleTime) throws SchedulerException {
        // Create a JobDetail for the team job with the provided teamId
        JobDetail jobDetail = JobBuilder.newJob(TeamSchedulerJob.class)
                .withIdentity("teamSchedulerJob" + teamId)
                .usingJobData("teamId", teamId)
                .storeDurably()
                .build();

        // Create a Trigger to schedule the job at the specified time
        Trigger trigger = TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity("teamSchedulerTrigger" + teamId)
                .startAt(Date.from(scheduleTime.atZone(ZoneId.systemDefault()).toInstant()))
                .build();

        // Schedule the job with the created JobDetail and Trigger
        scheduler.scheduleJob(jobDetail, trigger);
    }

    /**
     * Deletes a scheduled job for a team.
     *
     * @param teamId the ID of the team
     * @throws SchedulerException if there is an error in deleting the job
     */
    @Override
    public void deleteScheduledTeamJob(Long teamId) throws SchedulerException {
        // Create a JobKey using the teamId to identify the job
        JobKey jobKey = new JobKey("teamSchedulerJob" + teamId);

        // Delete the job from the scheduler
        scheduler.deleteJob(jobKey);
    }

    /**
     * Reschedules an existing job for a team to a new specified time.
     *
     * @param teamId the ID of the team
     * @param newScheduleTime the new time at which the job should be executed
     * @throws SchedulerException if there is an error in rescheduling the job
     */
    @Override
    public void rescheduleTeamJob(Long teamId, LocalDateTime newScheduleTime) throws SchedulerException {
        // Create a TriggerKey using the teamId to identify the trigger
        TriggerKey triggerKey = new TriggerKey("teamSchedulerTrigger" + teamId);

        // Create a new Trigger to reschedule the job at the new specified time
        Trigger newTrigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerKey)
                .startAt(Date.from(newScheduleTime.atZone(ZoneId.systemDefault()).toInstant()))
                .build();

        // Reschedule the job with the new Trigger
        scheduler.rescheduleJob(triggerKey, newTrigger);
    }
}
