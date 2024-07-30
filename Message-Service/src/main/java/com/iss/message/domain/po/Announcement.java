package com.iss.message.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("announcement")
public class Announcement {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long teacherId;

    private Long studentId;

    private String title;

    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}