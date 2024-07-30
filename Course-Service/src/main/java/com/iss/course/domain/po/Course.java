package com.iss.course.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("course")
public class Course {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long studentId;

    private Long lecturerId;

    private String courseName;

    private String courseDescription;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
