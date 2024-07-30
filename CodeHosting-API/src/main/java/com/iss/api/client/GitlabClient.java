package com.iss.api.client;

import com.iss.api.domain.dto.ProjectDTO;
import com.iss.api.domain.vo.ProjectVO;
import com.iss.common.result.Result;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Branch;
import org.gitlab4j.api.models.Commit;
import org.gitlab4j.api.models.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(value = "gitlab-service", path = "/gitlab")
public interface GitlabClient {


    @PostMapping("projects/new")
    Result<ProjectVO> createProject(@RequestBody ProjectDTO projectDTO, @RequestParam String majorName, @RequestParam String courseName);

    @GetMapping("projects/{projectId}/commits/count")
    Result<Map<String, Map<String, Integer>>> getCommitsCount(@PathVariable("projectId") Long projectId);

    @GetMapping("projects/{projectId}/changed/lines")
    Result<Map<String, Map<String, Integer>>> getLinesChanged(@PathVariable("projectId") Long projectId);

    @GetMapping("projects/{projectId}")
    Result<List<Branch>> getBranches(@PathVariable("projectId") Long projectId);

    @GetMapping("projects/{projectId}/branch/commits")
    Result<List<Commit>> getBranchCommits(@PathVariable("projectId") Long projectId, @RequestParam String branchName);

    @PostMapping("teams/invite")
    Result<String> inviteUser(@RequestParam Long projectId, @RequestParam String email) throws GitLabApiException;

    @PostMapping("teams/signup")
    Result<User> signUpUser(@RequestParam String email,
                            @RequestParam String username,
                            @RequestParam String name
    ) throws GitLabApiException;

    @PostMapping("teams/remove")
    Result<String> removeUser(@RequestParam Long projectId,
                                     @RequestParam String email) throws GitLabApiException;
}
