package com.iss.course.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iss.course.domain.po.Course;
import com.iss.course.mapper.CourseMapper;
import com.iss.course.service.ICourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService{
}
