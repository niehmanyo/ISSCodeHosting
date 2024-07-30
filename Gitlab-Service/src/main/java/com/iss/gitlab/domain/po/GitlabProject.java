package com.iss.gitlab.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("project")
public class GitlabProject implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long teamId;

    private Long studentId;

    private Long gitlabProjectId;

    private String projectName;

    private String projectDescription;

    private String publicProjectUrl;

    private String centralizedProjectUrl;

    private String projectAccessToken;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
