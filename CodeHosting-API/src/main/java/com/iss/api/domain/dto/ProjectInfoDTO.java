package com.iss.api.domain.dto;

import lombok.Data;

@Data
public class ProjectInfoDTO {
    Long teamId;

    Long projectId;

    String projectUrl;

    String projectAccessToken;
}
