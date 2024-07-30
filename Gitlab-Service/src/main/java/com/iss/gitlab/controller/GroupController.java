package com.iss.gitlab.controller;

import com.iss.common.constant.GitlabMessageConstant;
import com.iss.common.result.Result;
import com.iss.gitlab.domain.dto.GroupDTO;
import com.iss.gitlab.domain.vo.GroupVO;
import com.iss.gitlab.service.IGroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Group;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "Gitlab Group Interface")
@RestController
@RequestMapping("gitlab/groups")
@RequiredArgsConstructor
public class GroupController {

    private final IGroupService groupService;

    @ApiOperation("Create Group Interface")
    @PostMapping("new")
    public Result<GroupVO> createGroup(@RequestBody GroupDTO groupDTO) {
        try {
            GroupVO groupVO = groupService.createGroup(groupDTO);
            return Result.success(GitlabMessageConstant.CREATE_GROUP_SUCCESS, groupVO);
        } catch (GitLabApiException e) {
            return Result.error(extractErrorCode(e.getMessage()), e.getMessage());
        }
    }

    @ApiOperation("Create Subgroup Interface")
    @PostMapping("new/subgroup")
    public Result<GroupVO> createSubgroup(@RequestBody GroupDTO groupDTO){
        try {
            GroupVO groupVO = groupService.createSubgroup(groupDTO);
            return Result.success(GitlabMessageConstant.CREATE_GROUP_SUCCESS, groupVO);
        } catch (GitLabApiException e) {
            return Result.error(extractErrorCode(e.getMessage()), e.getMessage());
        }
    }

    @ApiOperation("Modify Group Interface")
    @PutMapping("modify")
    public Result<String> updateGroup(Group group){
        try {
            groupService.updateGroup(group);
            return Result.success(GitlabMessageConstant.UPDATE_GROUP_SUCCESS);
        } catch (GitLabApiException e) {
            return Result.error(extractErrorCode(e.getMessage()), e.getMessage());
        }
    }

    @ApiOperation("Delete Group Interface")
    @DeleteMapping("delete/{groupId}")
    public Result<String> deleteGroup(@PathVariable("groupId") Long groupId){
        try {
            groupService.deleteGroup(groupId);
            return Result.success(GitlabMessageConstant.DELETE_GROUP_SUCCESS);
        } catch (GitLabApiException e) {
            return Result.error(extractErrorCode(e.getMessage()), e.getMessage());
        }
    }

    @ApiOperation("Get Groups Interface")
    @GetMapping("get")
    public Result<List<GroupVO>> getGroups(){
        try {
            List<GroupVO> groups = groupService.getGroups();
            return Result.success(GitlabMessageConstant.GET_GROUPS_SUCCESS, groups);
        } catch (GitLabApiException e) {
            return Result.error(extractErrorCode(e.getMessage()), e.getMessage());
        }
    }

    @ApiOperation("Get Subgroups Interface")
    @GetMapping("get/{parentId}")
    public Result<List<GroupVO>> getSubgroups(@PathVariable("parentId") Long parentId){
        try {
            List<GroupVO> groups = groupService.getSubgroups(parentId);
            return Result.success(GitlabMessageConstant.GET_GROUPS_SUCCESS, groups);
        } catch (GitLabApiException e) {
            return Result.error(extractErrorCode(e.getMessage()), e.getMessage());
        }
    }

    private int extractErrorCode(String errorMessage) {
        String[] parts = errorMessage.split(" ");
        for (String part : parts) {
            try {
                return Integer.parseInt(part);
            } catch (NumberFormatException ignored) {
                // Ignore parts that are not numbers
            }
        }
        return 500; // Default error code if not found
    }
}
