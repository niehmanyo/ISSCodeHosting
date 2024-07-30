package com.iss.scheduler.controller;

import com.iss.common.constant.SchedulerMessageConstant;
import com.iss.common.result.Result;
import com.iss.scheduler.domain.dto.TaskScheduleRequest;
import com.iss.scheduler.service.TaskSchedulerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.quartz.SchedulerException;
import org.springframework.web.bind.annotation.*;

@Api(tags = "Task Scheduler Management Interface")
@RestController
@RequestMapping("/scheduler/tasks")
@RequiredArgsConstructor
public class TaskSchedulerController {

    private final TaskSchedulerService taskSchedulerService;

    @ApiOperation("Create Team Scheduler Interface")
    @PostMapping("/create")
    public Result<String> createTaskSchedule(@RequestBody TaskScheduleRequest request) {
        try {
            taskSchedulerService.scheduleTaskJob(request.getCourseName(), request.getScheduleTime());
            return Result.success(SchedulerMessageConstant.CREATE_SCHEDULER_SUCCESS);
        } catch (SchedulerException e) {
            return Result.error(extractErrorCode(e.getMessage()), e.getMessage());
        }
    }

    @ApiOperation("Delete Team Scheduler Interface")
    @DeleteMapping("/delete/{taskId}")
    public Result<String> deleteTaskSchedule(@PathVariable Long taskId) {
        try {
            taskSchedulerService.deleteScheduledTaskJob(taskId);
            return Result.success(SchedulerMessageConstant.DELETE_SCHEDULER_SUCCESS);
        } catch (SchedulerException e) {
            return Result.error(extractErrorCode(e.getMessage()), e.getMessage());
        }
    }

    @ApiOperation("Update Team Scheduler Interface")
    @PostMapping("/reschedule")
    public Result<String> rescheduleTask(@RequestBody TaskScheduleRequest request) {
        try {
            taskSchedulerService.rescheduleTaskJob(request.getTaskId(), request.getScheduleTime());
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
