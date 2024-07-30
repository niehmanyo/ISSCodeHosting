package com.iss.gitlab.domain.dto;

import lombok.Data;

@Data
public class GroupDTO {

    private Long groupId;

    private Long groupParentId;

    private String groupName;
}
