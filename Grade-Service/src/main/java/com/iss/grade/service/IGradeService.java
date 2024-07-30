package com.iss.grade.service;

import com.iss.grade.domain.vo.GradeResult;

import java.util.List;
import java.util.Map;

public interface IGradeService {
    List<GradeResult> calculateGrades(Long projectId);

    Map<String, Map<String, Integer>> calculateLinesChanged(Long projectId);

    Map<String, Map<String, Integer>> calculateCommitCounts(Long projectId);

    Map<String, Integer> calculateAllLinesChanged(Long projectId);

    Map<String, Integer> calculateAllCommitCounts(Long projectId);
}
