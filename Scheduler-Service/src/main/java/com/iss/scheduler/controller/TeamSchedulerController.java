package com.iss.scheduler.controller;

import com.iss.common.constant.SchedulerMessageConstant;
import com.iss.common.result.Result;

import com.iss.scheduler.domain.dto.TeamScheduleRequest;
import com.iss.scheduler.service.TeamSchedulerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.quartz.SchedulerException;
import org.springframework.web.bind.annotation.*;

@Api(tags = "Team Scheduler Management Interface")
@RestController
@RequestMapping("/scheduler/teams")
@RequiredArgsConstructor
public class TeamSchedulerController {

    private final TeamSchedulerService teamSchedulerService;

    @ApiOperation("Create Team Scheduler Interface")
    @PostMapping("/create")
    public Result<String> createTeamSchedule(@RequestBody TeamScheduleRequest request) {
        try {
            teamSchedulerService.scheduleTeamJob(request.getTeamId(), request.getScheduleTime());
            return Result.success(SchedulerMessageConstant.CREATE_SCHEDULER_SUCCESS);
        } catch (SchedulerException e) {
            return Result.error(extractErrorCode(e.getMessage()), e.getMessage());
        }
    }

    @ApiOperation("Delete Team Scheduler Interface")
    @DeleteMapping("/delete/{teamId}")
    public Result<String> deleteTeamSchedule(@PathVariable Long teamId) {
        try {
            teamSchedulerService.deleteScheduledTeamJob(teamId);
            return Result.success(SchedulerMessageConstant.DELETE_SCHEDULER_SUCCESS);
        } catch (SchedulerException e) {
            return Result.error(extractErrorCode(e.getMessage()), e.getMessage());
        }
    }

    @ApiOperation("Update Team Scheduler Interface")
    @PostMapping("/reschedule")
    public Result<String> rescheduleTeam(@RequestBody TeamScheduleRequest request) {
        try {
            teamSchedulerService.rescheduleTeamJob(request.getTeamId(), request.getScheduleTime());
            return Result.success(SchedulerMessageConstant.UPDATE_SCHEDULER_SUCCESS);
        } catch (SchedulerException e) {
            return Result.error(extractErrorCode(e.getMessage()), e.getMessage());
        }
    }

    private int extractErrorCode(String errorMessage) {
        String[] parts = errorMessage.split(" ");
        for (String part : parts) {
            try {
                return Integer.parseInt(part);
            } catch (NumberFormatException ignored) {
                // Ignore parts that are not numbers
            }
        }
        return 500; // Default error code if not found
    }
}
