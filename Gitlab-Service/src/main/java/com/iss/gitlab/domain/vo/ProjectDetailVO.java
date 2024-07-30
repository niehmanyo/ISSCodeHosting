package com.iss.gitlab.domain.vo;

import lombok.Data;

import java.util.List;

@Data
public class ProjectDetailVO {

    private Long gitlabProjectId;

    private Long teamId;

    private String projectName;

    private String projectDescription;

    private String centralizedProjectUrl;

    private String projectAccessToken;

    private List<String> contributors;
}
