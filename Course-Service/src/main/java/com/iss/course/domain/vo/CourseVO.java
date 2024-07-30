package com.iss.course.domain.vo;

import lombok.Data;

@Data
public class CourseVO {
    private Long id;

    private Long studentId;

    private Long LecturerId;

    private String courseName;

    private String courseDescription;
}
