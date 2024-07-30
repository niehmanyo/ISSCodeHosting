package com.iss.gitlab.domain.vo;

import lombok.Data;

import java.util.List;

@Data
public class GroupVO {

    private Long groupId;

    private Long groupParentId;

    private String groupName;

    private List<GroupVO> subgroups;
}
