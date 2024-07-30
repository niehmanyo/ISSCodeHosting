package com.iss.api.domain.dto;

import lombok.Data;

@Data
public class ProjectDTO {

    private Long projectId;

    private Long teamId;

    private Long studentId;

    private String projectName;

    private String projectDescription;

    private String publicProjectUrl;
}
