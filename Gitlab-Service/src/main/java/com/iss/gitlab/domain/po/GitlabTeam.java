package com.iss.gitlab.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 这个类是为了教师端能够获取所有课程下，所有组的信息，
 * 目前组和项目是一一绑定的概念，加入同一个项目的学生则被视为同一组里。
 * */
@Data
@TableName("team")
public class GitlabTeam implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long projectId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
