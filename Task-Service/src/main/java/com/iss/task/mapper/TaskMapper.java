package com.iss.task.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iss.task.domain.po.Task;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;

public interface TaskMapper extends BaseMapper<Task> {
}
