package com.iss.course.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.iss.common.constant.CourseMessageConstant;
import com.iss.common.constant.TaskMessageConstant;
import com.iss.common.result.Result;
import com.iss.common.utils.BeanUtils;
import com.iss.course.domain.dto.CourseDTO;
import com.iss.course.domain.po.Course;
import com.iss.course.domain.vo.CourseVO;
import com.iss.course.service.ICourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Api(tags = "Course Management Interface")
@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

    private final ICourseService courseService;

    @ApiOperation("Create Course Interface")
    @PostMapping("new")
    public Result<CourseVO> createCourse(@RequestBody CourseDTO courseDTO){
        try {
            Course course = BeanUtils.copyBean(courseDTO, Course.class);
            courseService.save(course);

            CourseVO courseVO = BeanUtils.copyBean(courseDTO, CourseVO.class);

            return Result.success(CourseMessageConstant.CREATE_COURSE_SUCCESS, courseVO);
        } catch (Exception e) {
            return Result.error(extractErrorCode(e.getMessage()), e.getMessage());
        }
    }

    @ApiOperation("Update Course Interface")
    @PutMapping("update")
    public Result<String> updateCourse(@RequestBody Course course){
        try {
            courseService.updateById(course);
            return Result.success(CourseMessageConstant.UPDATE_COURSE_SUCCESS);
        } catch (Exception e) {
            return Result.error(extractErrorCode(e.getMessage()), e.getMessage());
        }
    }

    @ApiOperation("Delete Course Interface")
    @DeleteMapping("delete/{id}")
    public Result<String> deleteCourseById(@PathVariable("id") Long id){
        try {
            courseService.removeById(id);
            return Result.success(CourseMessageConstant.DELETE_COURSE_SUCCESS);
        } catch (Exception e) {
            return Result.error(extractErrorCode(e.getMessage()), e.getMessage());
        }
    }

    @ApiOperation("Get Course Interface")
    @GetMapping("get/{id}")
    public Result<CourseVO> getCourseById(@PathVariable("id") Long id){
        try {
            CourseVO courseVO = BeanUtils.copyBean(courseService.getById(id), CourseVO.class);
            return Result.success(CourseMessageConstant.GET_COURSE_SUCCESS, courseVO);
        } catch (Exception e) {
            return Result.error(extractErrorCode(e.getMessage()), e.getMessage());
        }
    }

    @ApiOperation("Get Courses Interface")
    @GetMapping("get/lists")
    public Result<List<CourseVO>> getCoursesByIds(@RequestParam("ids") List<Long> ids){
        try {
            List<CourseVO> courseVOS = BeanUtils.copyList(courseService.listByIds(ids), CourseVO.class);
            return Result.success(CourseMessageConstant.GET_COURSE_SUCCESS, courseVOS);
        } catch (Exception e) {
            return Result.error(extractErrorCode(e.getMessage()), e.getMessage());
        }
    }

    @ApiOperation("Get All Tasks Interface")
    @GetMapping("get/all/{studentId}")
    public Result<List<CourseVO>> getTasksByStudentId(@PathVariable("studentId") Long studentId){
        try {
            QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("student_id", studentId);

            List<CourseVO> courseVOS = BeanUtils.copyList(courseService.list(queryWrapper), CourseVO.class);
            return Result.success(CourseMessageConstant.GET_COURSE_SUCCESS, courseVOS);
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
