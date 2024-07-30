package com.iss.task.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.iss.common.constant.TaskMessageConstant;
import com.iss.common.result.Result;
import com.iss.common.utils.BeanUtils;
import com.iss.task.domain.dto.TaskDTO;
import com.iss.task.domain.po.Task;
import com.iss.task.domain.vo.TaskVO;
import com.iss.task.service.ITaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Api(tags = "Task Management Interface")
@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final ITaskService taskService;

    @ApiOperation("Create Task Interface")
    @PostMapping("new")
    public Result<TaskVO> createTask(@RequestBody TaskDTO taskDTO){
        try {
            Task task = BeanUtils.copyBean(taskDTO, Task.class);
            taskService.save(task);

            TaskVO taskVO = BeanUtils.copyBean(taskDTO, TaskVO.class);

            return Result.success(TaskMessageConstant.CREATE_TASK_SUCCESS, taskVO);
        } catch (Exception e) {
            return Result.error(extractErrorCode(e.getMessage()), e.getMessage());
        }
    }

    @ApiOperation("Update Task Interface")
    @PutMapping("update")
    public Result<String> updateTask(@RequestBody Task task){
        try {
            taskService.updateById(task);
            return Result.success(TaskMessageConstant.UPDATE_TASK_SUCCESS);
        } catch (Exception e) {
            return Result.error(extractErrorCode(e.getMessage()), e.getMessage());
        }
    }

    @ApiOperation("Delete Task Interface")
    @DeleteMapping("delete/{id}")
    public Result<String> deleteTaskById(@PathVariable("id") Long id){
        try {
            taskService.removeById(id);
            return Result.success(TaskMessageConstant.DELETE_TASK_SUCCESS);
        } catch (Exception e) {
            return Result.error(extractErrorCode(e.getMessage()), e.getMessage());
        }
    }

    @ApiOperation("Get Task Interface")
    @GetMapping("get/{id}")
    public Result<TaskVO> getTaskById(@PathVariable("id") Long id){
        try {
            TaskVO taskVO = BeanUtils.copyBean(taskService.getById(id), TaskVO.class);
            return Result.success(TaskMessageConstant.GET_TASK_SUCCESS, taskVO);
        } catch (Exception e) {
            return Result.error(extractErrorCode(e.getMessage()), e.getMessage());
        }
    }

    @ApiOperation("Get Tasks Interface")
    @GetMapping("get/lists")
    public Result<List<TaskVO>> getTasksByIds(@RequestParam("ids") List<Long> ids){
        try {
            List<TaskVO> taskVOS = BeanUtils.copyList(taskService.listByIds(ids), TaskVO.class);
            return Result.success(TaskMessageConstant.GET_TASK_SUCCESS, taskVOS);
        } catch (Exception e) {
            return Result.error(extractErrorCode(e.getMessage()), e.getMessage());
        }
    }

    @ApiOperation("Get All Tasks Interface")
    @GetMapping("get/all/{lecturerId}")
    public Result<List<TaskVO>> getTasksByLecturerId(@PathVariable("lecturerId") Long lecturerId){
        try {
            QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("lecturer_id", lecturerId);

            List<TaskVO> taskVOS = BeanUtils.copyList(taskService.list(queryWrapper), TaskVO.class);
            return Result.success(TaskMessageConstant.GET_TASK_SUCCESS, taskVOS);
        } catch (Exception e) {
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
