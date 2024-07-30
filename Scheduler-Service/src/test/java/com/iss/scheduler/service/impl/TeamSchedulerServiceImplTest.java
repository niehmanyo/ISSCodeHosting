package com.iss.scheduler.service.impl;


import com.iss.scheduler.jobs.TeamSchedulerJob;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class TeamSchedulerServiceImplTest {

    @InjectMocks
    private TeamSchedulerServiceImpl schedulerService;

    @Mock
    private Scheduler scheduler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testScheduleTeamJob() throws SchedulerException {
        Long teamId = 1L;
        LocalDateTime scheduleTime = LocalDateTime.now().plusDays(1);

        schedulerService.scheduleTeamJob(teamId, scheduleTime);

        ArgumentCaptor<JobDetail> jobDetailCaptor = ArgumentCaptor.forClass(JobDetail.class);
        ArgumentCaptor<Trigger> triggerCaptor = ArgumentCaptor.forClass(Trigger.class);

        verify(scheduler, times(1)).scheduleJob(jobDetailCaptor.capture(), triggerCaptor.capture());

        JobDetail capturedJobDetail = jobDetailCaptor.getValue();
        Trigger capturedTrigger = triggerCaptor.getValue();

        assertEquals("teamSchedulerJob1", capturedJobDetail.getKey().getName());
        assertEquals(TeamSchedulerJob.class, capturedJobDetail.getJobClass());
        assertEquals(teamId, capturedJobDetail.getJobDataMap().getLong("teamId"));

        assertEquals("teamSchedulerTrigger1", capturedTrigger.getKey().getName());
        assertEquals(Date.from(scheduleTime.atZone(ZoneId.systemDefault()).toInstant()), capturedTrigger.getStartTime());
    }

    @Test
    void testDeleteScheduledTeamJob() throws SchedulerException {
        Long teamId = 1L;

        schedulerService.deleteScheduledTeamJob(teamId);

        JobKey jobKey = new JobKey("teamSchedulerJob" + teamId);

        verify(scheduler, times(1)).deleteJob(jobKey);
    }

    @Test
    void testRescheduleTeamJob() throws SchedulerException {
        Long teamId = 1L;
        LocalDateTime newScheduleTime = LocalDateTime.now().plusDays(2);

        schedulerService.rescheduleTeamJob(teamId, newScheduleTime);

        TriggerKey triggerKey = new TriggerKey("teamSchedulerTrigger" + teamId);

        ArgumentCaptor<Trigger> triggerCaptor = ArgumentCaptor.forClass(Trigger.class);

        verify(scheduler, times(1)).rescheduleJob(eq(triggerKey), triggerCaptor.capture());

        Trigger capturedTrigger = triggerCaptor.getValue();

        assertEquals("teamSchedulerTrigger1", capturedTrigger.getKey().getName());
        assertEquals(Date.from(newScheduleTime.atZone(ZoneId.systemDefault()).toInstant()), capturedTrigger.getStartTime());
    }
}