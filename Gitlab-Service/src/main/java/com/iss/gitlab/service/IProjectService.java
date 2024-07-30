package com.iss.gitlab.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iss.gitlab.domain.dto.ProjectDTO;
import com.iss.gitlab.domain.po.GitlabProject;
import com.iss.gitlab.domain.vo.ProjectDetailVO;
import com.iss.gitlab.domain.vo.ProjectVO;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Commit;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface IProjectService extends IService<GitlabProject> {
    ProjectVO createProject(ProjectDTO projectDTO, String majorName, String courseName) throws Exception;

    void deleteProject(Long projectId) throws GitLabApiException;

    String revokeProjectAccessToken(Long projectId) throws Exception;

    ProjectDetailVO getProjectByTeamId(Long teamId) throws GitLabApiException;

    void generateScript(HttpServletResponse response, Long projectId) throws GitLabApiException;

    List<Commit> getCommitsRecord(Long projectId) throws GitLabApiException;

    Map<String, Map<String, Integer>> calculateCommitCounts(Long projectId);

    Map<String, Map<String, Integer>> calculateLinesChanged(Long projectId);

    List<Commit> getCommitsByIdAndBranchName(Long projectId, String branchName) throws GitLabApiException;

    List<ProjectVO> getHistoryProjectsByTaskId(Long taskId);
}
