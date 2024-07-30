package com.iss.course.domain.dto;

import lombok.Data;

@Data
public class CourseDTO {
    private Long id;

    private Long studentId;

    private Long LecturerId;

    private String courseName;

    private String courseDescription;
}
