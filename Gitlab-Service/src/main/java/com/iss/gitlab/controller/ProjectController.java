package com.iss.gitlab.controller;

import com.iss.common.constant.GitlabMessageConstant;
import com.iss.common.result.Result;
import com.iss.gitlab.domain.dto.ProjectDTO;
import com.iss.gitlab.domain.vo.ProjectDetailVO;
import com.iss.gitlab.domain.vo.ProjectVO;
import com.iss.gitlab.service.IProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Commit;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Api(tags =  "Gitlab Project Interface")
@RestController
@RequestMapping("/gitlab/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final IProjectService projectService;

    @ApiOperation("Create Project Interface")
    @PostMapping("new")
    public Result<ProjectVO> createProject(@RequestBody ProjectDTO projectDTO, @RequestParam String majorName, @RequestParam String courseName) throws Exception {
        ProjectVO projectVO = null;
        try {
            projectVO = projectService.createProject(projectDTO, majorName, courseName);
            return Result.success(GitlabMessageConstant.CREATE_PROJECT_SUCCESS, projectVO);
        } catch (GitLabApiException e) {
            return Result.error(extractErrorCode(e.getMessage()), e.getMessage());
        }
    }

    @ApiOperation("Generate Git Shell Script")
    @GetMapping("script/new")
    public void generateScript(HttpServletResponse response, @RequestParam Long projectId) throws GitLabApiException {
        projectService.generateScript(response, projectId);
    }

    @ApiOperation("Delete Project Interface")
    @DeleteMapping("delete/{projectId}")
    public Result<String> deleteProject(@PathVariable("projectId") Long projectId) {
        try {
            projectService.deleteProject(projectId);
            return Result.success(GitlabMessageConstant.DELETE_PROJECT_SUCCESS);
        } catch (GitLabApiException e) {
            return Result.error(extractErrorCode(e.getMessage()), e.getMessage());
        }
    }

    //TODO
    @ApiOperation("Get Project By Team Id Interface")
    @GetMapping("/get/{teamId}")
    public Result<ProjectDetailVO> getProjectByTeamId(@PathVariable("teamId") Long teamId){
        try {
            ProjectDetailVO projectDetailVO = projectService.getProjectByTeamId(teamId);
            return Result.success(GitlabMessageConstant.GET_PROJECT_DETAIL_SUCCESS, projectDetailVO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //TODO
    @ApiOperation("Retrieve History Projects Interface")
    @GetMapping("/get/history-projects/{taskId}")
    public Result<List<ProjectVO>> retrieveHistoryProjects(@PathVariable("taskId") Long taskId){
        List<ProjectVO> projects = projectService.getHistoryProjectsByTaskId(taskId);
        return Result.success();
    }

    @ApiOperation("Revoke Project Access Token")
    @GetMapping("revoke/{projectId}/project_access_token")
    public Result<String> getProjectAccessToken(@PathVariable("projectId") Long projectId) {
        try {
            String accessToken = projectService.revokeProjectAccessToken(projectId);
            return Result.success(GitlabMessageConstant.GET_PROJECT_ACCESS_TOKEN_SUCCESS, accessToken);
        } catch (Exception e) {
            return Result.error(extractErrorCode(e.getMessage()), e.getMessage());
        }
    }

    //TODO
    @ApiOperation("Get Project Commits Interface")
    @GetMapping("{projectId}/commits")
    public Result<List<Commit>> getCommitsRecord(@PathVariable("projectId") Long projectId){
        try {
             List<Commit> commitsRecord = projectService.getCommitsRecord(projectId);
            return Result.success(GitlabMessageConstant.GET_COMMITS_SUCCESS, commitsRecord);
        }catch (Exception e){
            return Result.error(extractErrorCode(e.getMessage()), e.getMessage());
        }
    }

    @ApiOperation("Get Branch Commits Interface")
    @GetMapping("{projectId}/branch/commits")
    public Result<List<Commit>> getBranchCommits(@PathVariable("projectId") Long projectId, @RequestParam String branchName){
        try {
            List<Commit> commitsRecord = projectService.getCommitsByIdAndBranchName(projectId, branchName);
            return Result.success(GitlabMessageConstant.GET_COMMITS_SUCCESS, commitsRecord);
        }catch (Exception e){
            return Result.error(extractErrorCode(e.getMessage()), e.getMessage());
        }
    }

    @ApiOperation("Get Commits Count Interface")
    @GetMapping("/{projectId}/commits/count")
    public Result<Map<String, Map<String, Integer>>> getCommitsCount(@PathVariable("projectId") Long projectId){
        try {
            Map<String, Map<String, Integer>> commitCountsByAuthor = projectService.calculateCommitCounts(projectId);
            return Result.success(GitlabMessageConstant.GET_COMMITS_SUCCESS, commitCountsByAuthor);
        } catch (Exception e) {
            return Result.error(extractErrorCode(e.getMessage()), e.getMessage());
        }
    }

    @ApiOperation("Get Lines Changed Interface")
    @GetMapping("/{projectId}/changed/lines")
    public Result<Map<String, Map<String, Integer>>> getLinesChanged(@PathVariable("projectId") Long projectId){
        try {
            Map<String, Map<String, Integer>> linesChangedByAuthor = projectService.calculateLinesChanged(projectId);
            return Result.success(GitlabMessageConstant.GET_LINES_SUCCESS, linesChangedByAuthor);
        } catch (Exception e) {
            return Result.error(extractErrorCode(e.getMessage()), e.getMessage());
        }
    }

//    @ApiOperation("Get Grade Interface")

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
