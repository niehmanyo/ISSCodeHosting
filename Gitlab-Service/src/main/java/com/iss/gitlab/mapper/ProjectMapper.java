package com.iss.gitlab.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iss.gitlab.domain.po.GitlabProject;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProjectMapper extends BaseMapper<GitlabProject> {
}
