package com.iss.task.controller;

import com.iss.common.constant.TaskMessageConstant;
import com.iss.common.result.Result;
import com.iss.task.domain.dto.TaskDTO;
import com.iss.task.domain.po.Task;
import com.iss.task.domain.vo.TaskVO;
import com.iss.task.service.ITaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TaskControllerTest {

    @Mock
    private ITaskService taskService;

    @InjectMocks
    private TaskController taskController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTask() {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTaskName("Test Task");
        Task task = new Task();
        task.setTaskName("Test Task");

        when(taskService.save(any(Task.class))).thenReturn(true);

        Result<TaskVO> result = taskController.createTask(taskDTO);

        assertEquals(HttpStatus.OK.value(), result.getCode());
        assertEquals(TaskMessageConstant.CREATE_TASK_SUCCESS, result.getMsg());
        assertEquals(taskDTO.getTaskName(), result.getData().getTaskName());
        verify(taskService, times(1)).save(any(Task.class));
    }

    @Test
    void testUpdateTask() {
        Task task = new Task();
        task.setId(1L);
        task.setTaskName("Updated Task");

        when(taskService.updateById(any(Task.class))).thenReturn(true);

        Result<String> result = taskController.updateTask(task);

        assertEquals(HttpStatus.OK.value(), result.getCode());
        assertEquals(TaskMessageConstant.UPDATE_TASK_SUCCESS, result.getData());
        verify(taskService, times(1)).updateById(any(Task.class));
    }

    @Test
    void testDeleteTask() {
        Long taskId = 1L;

        when(taskService.removeById(taskId)).thenReturn(true);

        Result<String> result = taskController.deleteTaskById(taskId);

        assertEquals(HttpStatus.OK.value(), result.getCode());
        assertEquals(TaskMessageConstant.DELETE_TASK_SUCCESS, result.getData());
        verify(taskService, times(1)).removeById(taskId);
    }

    @Test
    void testGetTaskById() {
        Long taskId = 1L;
        Task task = new Task();
        task.setId(taskId);
        task.setTaskName("Test Task");

        when(taskService.getById(taskId)).thenReturn(task);

        Result<TaskVO> result = taskController.getTaskById(taskId);

        assertEquals(HttpStatus.OK.value(), result.getCode());
        assertEquals(TaskMessageConstant.GET_TASK_SUCCESS, result.getMsg());
        assertNotNull(result.getData());
        assertEquals(taskId, result.getData().getId());
        verify(taskService, times(1)).getById(taskId);
    }

    @Test
    void testGetTasksByIds() {
        List<Long> ids = Arrays.asList(1L, 2L);
        Task task1 = new Task();
        task1.setId(1L);
        Task task2 = new Task();
        task2.setId(2L);
        List<Task> tasks = Arrays.asList(task1, task2);

        when(taskService.listByIds(ids)).thenReturn(tasks);

        Result<List<TaskVO>> result = taskController.getTasksByIds(ids);

        assertEquals(HttpStatus.OK.value(), result.getCode());
        assertEquals(TaskMessageConstant.GET_TASK_SUCCESS, result.getMsg());
        assertNotNull(result.getData());
        assertEquals(2, result.getData().size());
        verify(taskService, times(1)).listByIds(ids);
    }

    @Test
    void testGetTasksByLecturerId() {
        Long lecturerId = 1L;
        Task task1 = new Task();
        task1.setLecturerId(lecturerId);
        Task task2 = new Task();
        task2.setLecturerId(lecturerId);
        List<Task> tasks = Arrays.asList(task1, task2);

        when(taskService.list(any())).thenReturn(tasks);

        Result<List<TaskVO>> result = taskController.getTasksByLecturerId(lecturerId);

        assertEquals(HttpStatus.OK.value(), result.getCode());
        assertEquals(TaskMessageConstant.GET_TASK_SUCCESS, result.getMsg());
        assertNotNull(result.getData());
        assertEquals(2, result.getData().size());
        verify(taskService, times(1)).list(any());
    }
}
