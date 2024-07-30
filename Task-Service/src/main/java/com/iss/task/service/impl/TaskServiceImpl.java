package com.iss.task.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iss.task.domain.po.Task;
import com.iss.task.mapper.TaskMapper;
import com.iss.task.service.ITaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements ITaskService {
}
