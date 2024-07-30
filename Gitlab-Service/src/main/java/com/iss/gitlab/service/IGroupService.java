package com.iss.gitlab.service;

import com.iss.gitlab.domain.dto.GroupDTO;
import com.iss.gitlab.domain.vo.GroupVO;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Group;

import java.util.List;

public interface IGroupService {
    GroupVO createGroup(GroupDTO groupDTO) throws GitLabApiException;

    GroupVO createSubgroup(GroupDTO groupDTO) throws GitLabApiException;

    void updateGroup(Group group) throws GitLabApiException;

    void deleteGroup(Long groupId) throws GitLabApiException;

    List<GroupVO> getGroups() throws GitLabApiException;

    List<GroupVO> getSubgroups(Long parentId) throws GitLabApiException;
}
