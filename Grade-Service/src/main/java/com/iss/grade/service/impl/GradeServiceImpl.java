package com.iss.grade.service.impl;

import com.iss.api.client.GitlabClient;
import com.iss.api.client.UserClient;
import com.iss.common.constant.GradeMessageConstant;
import com.iss.common.result.Result;
import com.iss.grade.domain.po.Contribution;
import com.iss.grade.domain.vo.GradeResult;
import com.iss.grade.service.IGradeService;
import lombok.RequiredArgsConstructor;
import org.gitlab4j.api.models.Branch;
import org.gitlab4j.api.models.Commit;
import org.gitlab4j.api.models.CommitStats;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GradeServiceImpl implements IGradeService {

    private final GitlabClient gitlabClient;
    private final UserClient userClient;

    @Override
    public List<GradeResult> calculateGrades(Long projectId) {
        // Retrieve contributions and convert them to grade results
        List<Contribution> contributions = getContributions(projectId);
        return contributions.stream().map(this::convertToGradeResult).collect(Collectors.toList());
    }

    @Override
    public Map<String, Map<String, Integer>> calculateCommitCounts(Long projectId) {
        // Retrieve commit counts from GitLab
        Result<Map<String, Map<String, Integer>>> commitsCountResult = gitlabClient.getCommitsCount(projectId);
        Map<String, Map<String, Integer>> commitsCountData = commitsCountResult.getData();

        // Replace GitHub account with username
        Map<String, Map<String, Integer>> commitsCount = new HashMap<>();
        for (Map.Entry<String, Map<String, Integer>> entry : commitsCountData.entrySet()) {
            String githubAccount = entry.getKey();
            String username = userClient.searchUsernameByGithubAccount(githubAccount).getData();
            if (username == null || username.isEmpty()) {
                username = githubAccount;
            }
            commitsCount.put(username, entry.getValue());
        }
        return commitsCount;
    }

    @Override
    public Map<String, Integer> calculateAllCommitCounts(Long projectId) {
        // Retrieve commit counts from GitLab
        Result<Map<String, Map<String, Integer>>> commitsCountResult = gitlabClient.getCommitsCount(projectId);
        Map<String, Map<String, Integer>> commitsCountData = commitsCountResult.getData();

        // Replace GitHub account with username and sum commit counts
        Map<String, Integer> commitsCount = new HashMap<>();
        for (Map.Entry<String, Map<String, Integer>> entry : commitsCountData.entrySet()) {
            String githubAccount = entry.getKey();
            String username = userClient.searchUsernameByGithubAccount(githubAccount).getData();
            if (username == null || username.isEmpty()) {
                username = githubAccount;
            }
            int totalCommitsCount = entry.getValue().values().stream().mapToInt(Integer::intValue).sum();
            commitsCount.put(username, totalCommitsCount);
        }
        return commitsCount;
    }

    @Override
    public Map<String, Map<String, Integer>> calculateLinesChanged(Long projectId) {
        // Retrieve lines changed data from GitLab
        Result<Map<String, Map<String, Integer>>> linesChangedResult = gitlabClient.getLinesChanged(projectId);
        Map<String, Map<String, Integer>> linesChangedResultData = linesChangedResult.getData();

        // Replace GitHub account with username
        Map<String, Map<String, Integer>> linesChanged = new HashMap<>();
        for (Map.Entry<String, Map<String, Integer>> entry : linesChangedResultData.entrySet()) {
            String githubAccount = entry.getKey();
            String username = userClient.searchUsernameByGithubAccount(githubAccount).getData();
            if (username == null || username.isEmpty()) {
                username = githubAccount;
            }
            linesChanged.put(username, entry.getValue());
        }
        return linesChanged;
    }

    @Override
    public Map<String, Integer> calculateAllLinesChanged(Long projectId) {
        // Retrieve lines changed data from GitLab
        Result<Map<String, Map<String, Integer>>> linesChangedResult = gitlabClient.getLinesChanged(projectId);
        Map<String, Map<String, Integer>> linesChangedResultData = linesChangedResult.getData();

        // Replace GitHub account with username and sum lines changed
        Map<String, Integer> linesChanged = new HashMap<>();
        for (Map.Entry<String, Map<String, Integer>> entry : linesChangedResultData.entrySet()) {
            String githubAccount = entry.getKey();
            String username = userClient.searchUsernameByGithubAccount(githubAccount).getData();
            if (username == null || username.isEmpty()) {
                username = githubAccount;
            }
            int totalLinesChanged = entry.getValue().values().stream().mapToInt(Integer::intValue).sum();
            linesChanged.put(username, totalLinesChanged);
        }
        return linesChanged;
    }

    private List<Contribution> getContributions(Long projectId) {
        try {
            // Retrieve branches from GitLab
            List<Branch> branches = gitlabClient.getBranches(projectId).getData();
            Map<String, List<Commit>> commitsByAuthor = new HashMap<>();

            // Retrieve commits for each branch and group by author
            for (Branch branch : branches) {
                List<Commit> commits = gitlabClient.getBranchCommits(projectId, branch.getName()).getData();
                for (Commit commit : commits) {
                    commitsByAuthor.computeIfAbsent(commit.getAuthorName(), k -> new ArrayList<>()).add(commit);
                }
            }

            // Create contributions list from commits grouped by author
            return commitsByAuthor.entrySet().stream()
                    .map(entry -> new Contribution(entry.getKey(), entry.getValue().size(), calculateLinesChanged(entry.getValue())))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(GradeMessageConstant.GET_GRADES_SUCCESS, e);
        }
    }

    private int calculateLinesChanged(List<Commit> commits) {
        // Calculate total lines changed from a list of commits
        int totalLinesChanged = 0;
        for (Commit commit : commits) {
            CommitStats stats = commit.getStats();
            if (stats != null) {
                int additions = stats.getAdditions();
                int deletions = stats.getDeletions();
                totalLinesChanged += (additions + deletions);
            }
        }
        return totalLinesChanged;
    }

    private GradeResult convertToGradeResult(Contribution contribution) {
        // Convert Contribution to GradeResult
        GradeResult gradeResult = new GradeResult();
        gradeResult.setStudentName(contribution.getStudentName());
        gradeResult.setRepositoryUrl("YOUR_REPOSITORY_URL");
        gradeResult.setBranch("main");
        gradeResult.setTotalScore(calculateContributionScore(contribution));
        return gradeResult;
    }

    private int calculateContributionScore(Contribution contribution) {
        // Calculate contribution score based on commit count and lines of code
        return contribution.getCommitCount() * 10 + contribution.getLinesOfCode() / 10;
    }
}
