package com.iss.grade.service.impl;

import com.iss.api.client.GitlabClient;
import com.iss.api.client.UserClient;
import com.iss.common.result.Result;
import com.iss.grade.domain.po.Contribution;
import com.iss.grade.domain.vo.GradeResult;
import org.gitlab4j.api.models.Branch;
import org.gitlab4j.api.models.Commit;
import org.gitlab4j.api.models.CommitStats;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class GradeServiceImplTest {

    @Mock
    private GitlabClient gitlabClient;

    @Mock
    private UserClient userClient;

    @InjectMocks
    private GradeServiceImpl gradeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCalculateGrades() {
        Long projectId = 1L;
        List<Branch> branches = List.of(new Branch().withName("main"));
        List<Commit> commits = List.of(
                createCommit("user1", 10, 5),
                createCommit("user1", 3, 2)
        );
        when(gitlabClient.getBranches(projectId)).thenReturn(Result.success(branches));
        when(gitlabClient.getBranchCommits(projectId, "main")).thenReturn(Result.success(commits));

        List<GradeResult> gradeResults = gradeService.calculateGrades(projectId);

        assertEquals(1, gradeResults.size());
        GradeResult result = gradeResults.get(0);
        assertEquals("user1", result.getStudentName());
        assertEquals(22, result.getTotalScore()); // 2 commits * 10 + 20 lines of code / 10
    }

    @Test
    void testCalculateCommitCounts() {
        Long projectId = 1L;
        Map<String, Map<String, Integer>> commitsCountData = Map.of(
                "githubUser1", Map.of("main", 5, "dev", 3)
        );
        when(gitlabClient.getCommitsCount(projectId)).thenReturn(Result.success(commitsCountData));
        when(userClient.searchUsernameByGithubAccount("githubUser1")).thenReturn(Result.success("user1"));

        Map<String, Map<String, Integer>> commitCounts = gradeService.calculateCommitCounts(projectId);

        assertEquals(1, commitCounts.size());
        assertEquals(Map.of("main", 5, "dev", 3), commitCounts.get("user1"));
    }

    @Test
    void testCalculateAllCommitCounts() {
        Long projectId = 1L;
        Map<String, Map<String, Integer>> commitsCountData = Map.of(
                "githubUser1", Map.of("main", 5, "dev", 3)
        );
        when(gitlabClient.getCommitsCount(projectId)).thenReturn(Result.success(commitsCountData));
        when(userClient.searchUsernameByGithubAccount("githubUser1")).thenReturn(Result.success("user1"));

        Map<String, Integer> commitCounts = gradeService.calculateAllCommitCounts(projectId);

        assertEquals(1, commitCounts.size());
        assertEquals(8, commitCounts.get("user1"));
    }

    @Test
    void testCalculateLinesChanged() {
        Long projectId = 1L;
        Map<String, Map<String, Integer>> linesChangedData = Map.of(
                "githubUser1", Map.of("main", 100, "dev", 50)
        );
        when(gitlabClient.getLinesChanged(projectId)).thenReturn(Result.success(linesChangedData));
        when(userClient.searchUsernameByGithubAccount("githubUser1")).thenReturn(Result.success("user1"));

        Map<String, Map<String, Integer>> linesChanged = gradeService.calculateLinesChanged(projectId);

        assertEquals(1, linesChanged.size());
        assertEquals(Map.of("main", 100, "dev", 50), linesChanged.get("user1"));
    }

    @Test
    void testCalculateAllLinesChanged() {
        Long projectId = 1L;
        Map<String, Map<String, Integer>> linesChangedData = Map.of(
                "githubUser1", Map.of("main", 100, "dev", 50)
        );
        when(gitlabClient.getLinesChanged(projectId)).thenReturn(Result.success(linesChangedData));
        when(userClient.searchUsernameByGithubAccount("githubUser1")).thenReturn(Result.success("user1"));

        Map<String, Integer> linesChanged = gradeService.calculateAllLinesChanged(projectId);

        assertEquals(1, linesChanged.size());
        assertEquals(150, linesChanged.get("user1"));
    }

    private Commit createCommit(String authorName, int additions, int deletions) {
        Commit commit = new Commit();
        commit.setAuthorName(authorName);

        CommitStats stats = new CommitStats();
        stats.setAdditions(additions);
        stats.setDeletions(deletions);

        commit.setStats(stats);

        return commit;
    }
}
