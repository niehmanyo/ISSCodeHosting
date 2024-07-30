package com.iss.team.controller;


import com.iss.api.client.UserClient;
import com.iss.api.domain.vo.UserVO;
import com.iss.common.annotation.DistributedLock;
import com.iss.common.constant.AuthMessageConstant;
import com.iss.common.constant.TeamMessageConstant;
import com.iss.common.enumeration.LockType;
import com.iss.common.result.Result;
import com.iss.common.utils.BeanUtils;
import com.iss.team.domain.dto.StudentTeamDTO;
import com.iss.team.domain.dto.TeamDTO;
import com.iss.team.domain.entity.Team;
import com.iss.team.domain.vo.StudentVO;
import com.iss.team.domain.vo.TeamVO;
import com.iss.team.service.ITeamService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "Team Management Interface")
@RestController
@RequestMapping("/teams")
@RequiredArgsConstructor
public class TeamController {

    @Autowired
    private ITeamService teamService;

    private final UserClient userClient;

    /**
     *
     * 这个是获取所有的Team消息，每个Team里面的成员信息
     * courseName 是独一无二的，因此通过这个字段查询
     * */
    @ApiOperation("Get Teams Interface")
    @GetMapping("get/{courseName}")
    public Result<List<TeamVO>> getTeams(@PathVariable("courseName")String courseName ) {
        try{
            List<Team> teams = teamService.getAllTeams(courseName);
            List<TeamVO> teamVOS = BeanUtils.copyList(teams, TeamVO.class);
            List<StudentVO> users;
            for (int i = 0; i < teams.size(); i++){
                users = teamService.findUserByTeamId(teamVOS.get(i).getId());
                teamVOS.get(i).setUsers(users);
            }
            return Result.success(TeamMessageConstant.GET_TEAM_SUCCESSFUL,teamVOS);
        }catch (Exception e) {
            return Result.error(extractErrorCode(e.getMessage()), e.getMessage());
        }

    }

    @ApiOperation("Get Team Members Interface")
    @GetMapping("getMembersByTeamId/{teamId}")
    public Result<List<StudentVO>> getMembersByTeamId(@PathVariable("teamId") Long teamId){
        try{
            List<StudentVO> users = teamService.findUserByTeamId(teamId);
            return Result.success(TeamMessageConstant.GET_TEAM_SUCCESSFUL, users);
        }catch (Exception e){
            return Result.error(extractErrorCode(e.getMessage()), e.getMessage());
        }
    }

    @ApiOperation("Join Team Interface")
    @PostMapping("/join")
    @DistributedLock(lockName = "join", lockType = LockType.REENTRANT)
    public Result<String> joinTeam(@RequestBody StudentTeamDTO studentTeamDTO){
        try{
            Result<UserVO> result = userClient.getUserInfoByName(studentTeamDTO.getUsername());
            int capacity = teamService.getCapacityByTeamId(studentTeamDTO.getTeamId());
            if (result.getData() == null){
                return Result.error(500, AuthMessageConstant.ACCOUNT_NOT_FOUND);
            }else if (studentTeamDTO.getSize() == capacity){
                return Result.error(500, TeamMessageConstant.TEAM_IS_FULL);
            }
            teamService.joinTeam(result.getData().getUsername(),result.getData().getSchoolEmail(), studentTeamDTO.getTeamId());
            return Result.success(TeamMessageConstant.JOIN_SUCCESS);
        }catch (Exception e){
            return Result.error(extractErrorCode(e.getMessage()), e.getMessage());
        }
    }

    @ApiOperation("Leave Team Interface")
    @PostMapping("/leave")
    @DistributedLock(lockName = "leave", lockType = LockType.REENTRANT)
    public Result<String> leaveTeam(@RequestBody StudentTeamDTO studentTeamDTO){
       try{
           teamService.leaveTeam(studentTeamDTO);
           return Result.success(TeamMessageConstant.LEAVE_SUCCESS);
       }catch (Exception e){
           return Result.error(extractErrorCode(e.getMessage()), e.getMessage());
       }
    }

    @ApiOperation("Get Project By Team Id Interface")
    @GetMapping("project/{teamId}")
    public Result<Long> getProjectByTeamId(@PathVariable("teamId") Long teamId){
        try{
            Long projectId = teamService.getProjectByTeamId(teamId);
            if (projectId == null){
                return Result.error(500,TeamMessageConstant.TEAM_NOT_FOUND);
            }
            return Result.success(TeamMessageConstant.GET_PROJECT_SUCCESS,projectId);
        }catch (Exception e){
            return Result.error(extractErrorCode(e.getMessage()), e.getMessage());
        }
    };

    @ApiOperation("Create Team Interface")
    @PostMapping("create")
    public Result<String> createTeam(@RequestBody TeamDTO teamDTO){
        try{
            teamService.createTeam(teamDTO);
            return Result.success(TeamMessageConstant.CREATE_TEAM_SUCCESS);
        }catch (Exception e){
            return Result.error(extractErrorCode(e.getMessage()),e.getMessage());
        }
    }

    @ApiOperation("Submit Team Project")
    @PostMapping("submit")
    public Result<String> submitTeamProject(@RequestParam Integer projectId,
                                            @RequestParam String projectUrl,
                                            @RequestParam String projectAccessToken)
    {
        try{
            return Result.success(TeamMessageConstant.CREATE_TEAM_SUCCESS);
        }catch (Exception e){
            return Result.error(extractErrorCode(e.getMessage()),e.getMessage());
        }
    }

    @ApiOperation("Delete Team Interface")
    @PostMapping("delete/{teamId}")
    public Result<String> deleteTeam(@PathVariable("teamId")Long teamId){
        try{
            teamService.deleteTeam(teamId);
            return Result.success(TeamMessageConstant.DELETE_TEAM_SUCCESS);
        }catch (Exception e){
            return Result.error(extractErrorCode(e.getMessage()),e.getMessage());
        }

    }

//    @PostMapping("/switch")
//    public Result<String> switchTeam(@RequestBody StudentTeamDTO studentTeamDTO){
//        try{
//            teamService.leaveTeam(studentTeamDTO.getUsername(),studentTeamDTO.getTeamId());
//            return Result.success(TeamMessageConstant.LEAVE_SUCCESS);
//        }catch (Exception e){
//            return Result.error(extractErrorCode(e.getMessage()), e.getMessage());
//        }
//    }

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
