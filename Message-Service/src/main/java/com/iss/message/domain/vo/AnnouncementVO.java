package com.iss.message.domain.vo;

import lombok.Data;

@Data
public class AnnouncementVO {

    private Long id;

    private String title;

    private String content;

    private Long teacherId;

    private Long studentId;

    private Long taskId;
}
