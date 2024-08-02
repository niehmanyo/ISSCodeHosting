package com.iss.gitlab.controller;


import com.iss.api.client.TeamClient;
import com.iss.api.domain.vo.StudentVO;
import com.iss.common.constant.GitlabMessageConstant;
import com.iss.common.result.Result;
import com.iss.gitlab.service.ITeamService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Member;
import org.gitlab4j.api.models.ProjectGroup;
import org.gitlab4j.api.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "Team Interface")
@RestController
@RequestMapping("/gitlab/teams")
@RequiredArgsConstructor
public class TeamController {

    @Autowired
    private ITeamService teamService;


    private final TeamClient teamClient;

    @ApiOperation("Get Team Members Interface")
    @GetMapping("/getTeamMember/{projectId}")
    public Result<List<Member>> getTeamMembers(@PathVariable("projectId") Long projectId) throws GitLabApiException {
        try {
            List<Member> members = teamService.getTeamMembers(projectId);
            return Result.success(GitlabMessageConstant.GET_MEMBERS_SUCCESS, members);
        } catch (GitLabApiException e) {
            return Result.error(extractErrorCode(e.getMessage()), e.getMessage());
        }

    }
    @ApiOperation("Just for test")
    @GetMapping("/getGroups/{projectId}")
    public Result<List<ProjectGroup>> getProjectGroup(@PathVariable("projectId") Long projectId) throws GitLabApiException {
        try {
            List<ProjectGroup> projectGroups = teamService.getGroups(projectId);
            return Result.success(GitlabMessageConstant.GET_MEMBERS_SUCCESS, projectGroups);
        } catch (GitLabApiException e) {
            return Result.error(extractErrorCode(e.getMessage()), e.getMessage());
        }
    }

    @ApiOperation("Sign up User")
    @PostMapping("/signup")
    public Result<User> signUpUser(@RequestParam String email,
                                   @RequestParam String username,
                                   @RequestParam String name
    ) throws GitLabApiException {
        try {
            User user = teamService.createUser(email, username, name);
            return Result.success(GitlabMessageConstant.CREAT_USER_SUCCESSFUL, user);
        } catch (GitLabApiException e) {
            return Result.error(extractErrorCode(e.getMessage()), e.getMessage());
        }
    }

    @ApiOperation("Remove User From Project")
    @PostMapping("/remove")
    public Result<String> removeUser(@RequestParam Long projectId,
                                     @RequestParam String email) throws GitLabApiException {
        try {
            teamService.removeUser(projectId, email);
            return Result.success(GitlabMessageConstant.REMOVE_USER_SUCCESS);
        } catch (GitLabApiException e) {
            return Result.error(extractErrorCode(e.getMessage()), e.getMessage());
        }
    }

    @ApiOperation("Invite User To Project")
    @PostMapping("/invite")
    public Result<String> inviteUser(@RequestParam Long projectId,
                                     @RequestParam String email) throws GitLabApiException {
        try {

            teamService.inviteUser(projectId, email);
            return Result.success(GitlabMessageConstant.INVITE_USER_SUCCESS);
        } catch (GitLabApiException e) {
            return Result.error(extractErrorCode(e.getMessage()), e.getMessage());
        }
    }

    /**
     * 这个接口是获取组里成员的username和email
     * **/
    @ApiOperation("Get Team Member By Team id")
    @GetMapping("members/{teamId}")
    public Result<List<StudentVO>> getMembersByTeamId(@PathVariable("teamId") Long teamId){
        try {
            Result<List<StudentVO>> result = teamClient.getMembersByTeamId(teamId);
            return Result.success(GitlabMessageConstant.GET_MEMBERS_SUCCESS,result.getData());
        } catch (Exception e) {
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
