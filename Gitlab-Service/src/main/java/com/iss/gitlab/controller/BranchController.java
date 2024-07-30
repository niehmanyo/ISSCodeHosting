package com.iss.gitlab.controller;

import com.iss.common.constant.GitlabMessageConstant;
import com.iss.common.result.Result;
import com.iss.gitlab.domain.dto.GroupDTO;
import com.iss.gitlab.domain.vo.BranchVO;
import com.iss.gitlab.service.IBranchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Branch;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "Gitlab Branch Interface")
@RestController
@RequestMapping("gitlab/branches")
@RequiredArgsConstructor
public class BranchController {

    private final IBranchService branchService;

    @ApiOperation("Create Branch Interface")
    @PostMapping("new")
    public Result<BranchVO> createBranch(@RequestBody GroupDTO groupDTO) {
//        try {
//            BranchVO branchVO = branchService.createBranch(groupDTO);
//            return Result.success(GitlabMessageConstant.CREATE_GROUP_SUCCESS, branchVO);
//        } catch (GitLabApiException e) {
//            return Result.error(extractErrorCode(e.getMessage()), e.getMessage());
//        }
        return Result.success();
    }

    @ApiOperation("Get Branches Interface")
    @GetMapping("{projectId}")
    public Result<List<Branch>> getBranches(@PathVariable("projectId") Long projectId){
        try {
            List<Branch> branches = branchService.getBranches(projectId);
            return Result.success(GitlabMessageConstant.GET_BRANCHES_SUCCESS, branches);
        }catch (GitLabApiException e) {
            throw new RuntimeException(e);
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
