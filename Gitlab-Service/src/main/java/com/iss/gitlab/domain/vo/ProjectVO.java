package com.iss.gitlab.domain.vo;

import lombok.Data;

@Data
public class ProjectVO {

    private Long projectId;

    private Long teamId;

    private String projectUrl;

    private String userName;

    private String projectAccessToken;

    private String major;

    private String courseName;
}
