package com.iss.gitlab.service.impl;

import com.iss.common.constant.GitlabMessageConstant;
import com.iss.gitlab.domain.dto.GroupDTO;
import com.iss.gitlab.domain.vo.GroupVO;
import com.iss.gitlab.service.IGroupService;
import lombok.RequiredArgsConstructor;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Group;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements IGroupService {

    private final GitLabApi gitLabApi;
    @Override
    public GroupVO createGroup(GroupDTO groupDTO) throws GitLabApiException {
        try {
            return getGroupVO(groupDTO);
        } catch (GitLabApiException e) {
            throw new GitLabApiException(GitlabMessageConstant.CREATE_GROUP_FAILED + ". " + e.getMessage());
        }
    }

    @Override
    public GroupVO createSubgroup(GroupDTO groupDTO) throws GitLabApiException {
        try {
            Group parentGroup = gitLabApi.getGroupApi().getGroup(groupDTO.getGroupParentId());
            if (parentGroup == null){
                throw new RuntimeException(GitlabMessageConstant.PARENT_GROUP_NOT_EXIST);
            } else {
                return getGroupVO(groupDTO);
            }
        } catch (GitLabApiException e) {
            throw new GitLabApiException(GitlabMessageConstant.CREATE_GROUP_FAILED + ". " + e.getMessage());
        }
    }

    @Override
    public void updateGroup(Group group) throws GitLabApiException {
        try {
            gitLabApi.getGroupApi().updateGroup(group);
        } catch (GitLabApiException e) {
            throw new GitLabApiException(GitlabMessageConstant.UPDATE_GROUP_FAILED + ". " + e.getMessage());
        }
    }

    @Override
    public void deleteGroup(Long groupId) throws GitLabApiException {
        try {
            gitLabApi.getGroupApi().deleteGroup(groupId);
        } catch (GitLabApiException e) {
            throw new GitLabApiException(GitlabMessageConstant.DELETE_GROUP_FAILED + ". " + e.getMessage());
        }
    }

    @Override
    public List<GroupVO> getGroups() throws GitLabApiException {
        try {
            List<Group> groups = gitLabApi.getGroupApi().getGroups();
            return convertToGroupVOListWithSubgroups(groups);
        } catch (GitLabApiException e) {
            throw new GitLabApiException(GitlabMessageConstant.GET_GROUPS_FAILED + ". " + e.getMessage());
        }
    }

    @Override
    public List<GroupVO> getSubgroups(Long parentId) throws GitLabApiException {
        try {
            List<Group> subgroups = gitLabApi.getGroupApi().getSubGroups(parentId);
            return convertToGroupVOListWithSubgroups(subgroups);
        } catch (GitLabApiException e) {
            throw new GitLabApiException(GitlabMessageConstant.GET_GROUPS_FAILED + ". " + e.getMessage());
        }
    }

    public GroupVO getGroupVO(GroupDTO groupDTO) throws GitLabApiException {
        Group newGroup = new Group()
                .withParentId(groupDTO.getGroupParentId())
                .withName(groupDTO.getGroupName())
                .withPath(groupDTO.getGroupName().toLowerCase().replace(" ", "-")); // Ensure the path is valid
        Group group = gitLabApi.getGroupApi().addGroup(newGroup);

        GroupVO groupVO = new GroupVO();
        groupVO.setGroupId(group.getId());
        groupVO.setGroupParentId(group.getParentId());
        groupVO.setGroupName(group.getName());

        return groupVO;
    }

    private List<GroupVO> convertToGroupVOListWithSubgroups(List<Group> groups) throws GitLabApiException {
        List<GroupVO> groupVOList = new ArrayList<>();
        for (Group group : groups) {
            GroupVO groupVO = convertToGroupVO(group);
            groupVOList.add(groupVO);
        }
        return groupVOList;
    }

    private GroupVO convertToGroupVO(Group group) throws GitLabApiException {
        GroupVO groupVO = new GroupVO();
        groupVO.setGroupId(group.getId());
        groupVO.setGroupParentId(group.getParentId());
        groupVO.setGroupName(group.getName());

        // Get subgroups and add them to the GroupVO
        List<Group> subgroups = gitLabApi.getGroupApi().getSubGroups(group.getId());
        List<GroupVO> subgroupVOList = new ArrayList<>();
        for (Group subgroup : subgroups) {
            subgroupVOList.add(convertToGroupVO(subgroup));
        }
        groupVO.setSubgroups(subgroupVOList);

        return groupVO;
    }

}
