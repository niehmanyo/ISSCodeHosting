package com.iss.grade.controller;

import com.iss.common.constant.GradeMessageConstant;
import com.iss.common.result.Result;
import com.iss.grade.domain.vo.GradeResult;
import com.iss.grade.service.IGradeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Api(tags = "Grade System Interface")
@RestController
@RequestMapping("project/grades")
@RequiredArgsConstructor
public class GradeController {

    private final IGradeService gradeService;

    @ApiOperation("Get Grades Interface")
    @GetMapping("/{projectId}/grades")
    public Result<List<GradeResult>> getGrades(@PathVariable("projectId") Long projectId){
        try {
            List<GradeResult> gradeResults = gradeService.calculateGrades(projectId);
            return Result.success(GradeMessageConstant.GET_GRADES_SUCCESS, gradeResults);
        } catch (Exception e) {
            return Result.error(extractErrorCode(e.getMessage()), e.getMessage());
        }
    }

    @ApiOperation("Get Each Branch Commits Count Interface")
    @GetMapping("/{projectId}/commits/count")
    public Result<Map<String, Map<String, Integer>>> getCommitsCount(@PathVariable("projectId") Long projectId){
        try {
            Map<String, Map<String, Integer>> commitCountsByAuthor = gradeService.calculateCommitCounts(projectId);
            return Result.success(GradeMessageConstant.GET_COMMITS_SUCCESS, commitCountsByAuthor);
        } catch (Exception e) {
            return Result.error(extractErrorCode(e.getMessage()), e.getMessage());
        }
    }

    @ApiOperation("Get All Commits Count Interface")
    @GetMapping("/{projectId}/commits/count/all")
    public Result<Map<String, Integer>> getAllCommitsCount(@PathVariable("projectId") Long projectId){
        try {
            Map<String, Integer> commitCountsByAuthor = gradeService.calculateAllCommitCounts(projectId);
            return Result.success(GradeMessageConstant.GET_COMMITS_SUCCESS, commitCountsByAuthor);
        } catch (Exception e) {
            return Result.error(extractErrorCode(e.getMessage()), e.getMessage());
        }
    }

    @ApiOperation("Get Each Branch Lines Changed Interface")
    @GetMapping("/{projectId}/changed/lines")
    public Result<Map<String, Map<String, Integer>>> getLinesChanged(@PathVariable("projectId") Long projectId){
        try {
            Map<String, Map<String, Integer>> linesChangedByAuthor = gradeService.calculateLinesChanged(projectId);
            return Result.success(GradeMessageConstant.GET_LINES_SUCCESS, linesChangedByAuthor);
        } catch (Exception e) {
            return Result.error(extractErrorCode(e.getMessage()), e.getMessage());
        }
    }

    @ApiOperation("Get All Lines Changed Interface")
    @GetMapping("/{projectId}/changed/lines/all")
    public Result<Map<String, Integer>> getAllLinesChanged(@PathVariable("projectId") Long projectId){
        try {
            Map<String, Integer> linesChangedByAuthor = gradeService.calculateAllLinesChanged(projectId);
            return Result.success(GradeMessageConstant.GET_LINES_SUCCESS, linesChangedByAuthor);
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
