package com.iss.gitlab.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iss.api.client.TeamClient;
import com.iss.api.client.UserClient;
import com.iss.api.domain.dto.ProjectInfoDTO;
import com.iss.api.domain.vo.UserVO;
import com.iss.common.constant.GitlabMessageConstant;
import com.iss.common.constant.GradeMessageConstant;
import com.iss.common.result.Result;
import com.iss.common.utils.BeanUtils;
import com.iss.common.utils.UserContext;
import com.iss.gitlab.domain.dto.ProjectDTO;
import com.iss.gitlab.domain.po.GitlabProject;
import com.iss.gitlab.domain.vo.ProjectDetailVO;
import com.iss.gitlab.domain.vo.ProjectVO;
import com.iss.gitlab.mapper.ProjectMapper;
import com.iss.gitlab.service.IBranchService;
import com.iss.gitlab.service.IProjectService;
import com.iss.gitlab.service.ITeamService;
import lombok.RequiredArgsConstructor;
import org.gitlab4j.api.Constants;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.Pager;
import org.gitlab4j.api.models.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * Done by CHEN WEIJIAN
 * Gitlab Service Impl
 */
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, GitlabProject> implements IProjectService {

    private final GitLabApi gitLabApi;

    private final ProjectMapper projectMapper;

    private final UserClient userClient;

    private final TeamClient teamClient;

    private final IBranchService branchService;

    private final ITeamService teamService;

    @Override
    @Transactional
    @Async
    public ProjectVO createProject(ProjectDTO projectDTO, String majorName, String courseName) throws GitLabApiException {
        try {
            // 1. Get or Create group
            Group majorGroup = getGroup(majorName);
            Group courseSubgroup = getSubgroup(majorGroup, courseName);

            // 2. Create the project in the target namespace
            Project projectSpec = new Project()
                    .withName(projectDTO.getProjectName())
                    .withDescription(projectDTO.getProjectDescription())
                    .withNamespaceId(courseSubgroup.getId());

            // 3. Check the Project Create Method
            Project project;
            if (projectDTO.getPublicProjectUrl() != null){
                project = gitLabApi.getProjectApi().createProject(projectSpec, projectDTO.getPublicProjectUrl());
            } else {
                project = gitLabApi.getProjectApi().createProject(projectSpec);
            }

            ProjectAccessToken accessToken = createProjectAccessToken(project.getId());

            // 4. Insert into gitlab database
            GitlabProject gitlabProject = BeanUtils.copyBean(projectDTO, GitlabProject.class);
            gitlabProject.setGitlabProjectId(project.getId());
            gitlabProject.setPublicProjectUrl(projectDTO.getPublicProjectUrl());
            gitlabProject.setCentralizedProjectUrl(project.getWebUrl() + ".git");
            gitlabProject.setProjectAccessToken(accessToken.getToken());
//            gitlabProject.setCreateTime(LocalDateTime.now());
//            gitlabProject.setUpdateTime(LocalDateTime.now());
            projectMapper.insert(gitlabProject);

            // 5. Create access token and return projectVO
            ProjectVO projectVO = new ProjectVO();
            projectVO.setProjectId(project.getId());
            projectVO.setTeamId(gitlabProject.getTeamId());
            projectVO.setProjectUrl(project.getWebUrl() + ".git");
            projectVO.setUserName("root");
            projectVO.setProjectAccessToken(accessToken.getToken());

            ProjectInfoDTO projectInfoDTO = new ProjectInfoDTO();
            projectInfoDTO.setProjectId(project.getId());
            projectInfoDTO.setTeamId(projectDTO.getTeamId());
            projectInfoDTO.setProjectUrl(project.getWebUrl()+".git");
            projectInfoDTO.setProjectAccessToken(accessToken.getToken());

            return projectVO;
        } catch (GitLabApiException e) {
            // Handle GitLab API exceptions
            throw new GitLabApiException(GitlabMessageConstant.CREATE_PROJECT_FAILED + ". " + e.getMessage());
        }
    }


    @Override
    public void generateScript(HttpServletResponse response, Long projectId) throws GitLabApiException {
        try {
            // 获取当前用户信息
            //User user = userMapper.selectById(UserContext.getUser());
            //TODO 目前 用户id写死方便测试
//            User user = userMapper.selectById(1L);
            Result<UserVO> result = userClient.getUserInfo(UserContext.getUser());
            UserVO usrInfo = result.getData();
            if (usrInfo == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Student not found");
                return;
            }

            // 通过数据库中的项目ID查找 GitlabProjectId
            GitlabProject gitlabProject = projectMapper.selectById(projectId);
            if (gitlabProject == null) {
                throw new GitLabApiException("Gitlab project not found");
            }

            String publicProjectUrl = gitlabProject.getPublicProjectUrl();
            StringBuilder scriptBuilder = new StringBuilder("#!/bin/bash\n")
                    .append("git config --global user.name \"").append(usrInfo.getUsername()).append("\"\n")
                    .append("git config --global user.email \"").append(usrInfo.getSchoolEmail()).append("\"\n");

            if (publicProjectUrl == null || publicProjectUrl.isEmpty()) {
                // 如果不存在 GitHub 项目 URL，则生成初始化并推送到 GitLab 的脚本
                scriptBuilder.append("mkdir repo\n")
                        .append("cd repo\n")
                        .append("git init\n")
                        .append("echo \"# Test Project\" > README.md\n")
                        .append("git add .\n")
                        .append("git commit -m \"Initial commit\"\n")
                        .append("git remote add origin ").append(gitlabProject.getCentralizedProjectUrl()).append("\n")
                        .append("git push -u origin master\n");
            } else {
                // 如果存在 GitHub 项目 URL，则生成克隆并推送到 GitLab 的脚本
                scriptBuilder.append("git clone ").append(gitlabProject.getCentralizedProjectUrl()).append("\n");
            }

            String scriptContent = scriptBuilder.toString();


            // 设置响应头和内容类型
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + projectId + "_init.sh\"");

            // 将脚本内容写入响应输出流
            response.getOutputStream().write(scriptContent.getBytes(StandardCharsets.UTF_8));
            response.flushBuffer();
        } catch (GitLabApiException e) {
            // 处理GitLab API异常
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GitLab API error: " + e.getMessage());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        } catch (IOException e) {
            // 处理IO异常
            e.printStackTrace();
        }
    }

    @Override
    public void deleteProject(Long projectId) throws GitLabApiException {
        try {
            gitLabApi.getProjectApi().deleteProject(projectId);
            projectMapper.deleteById(projectId);
        } catch (GitLabApiException e) {
            // Handle GitLab API exceptions
            throw new GitLabApiException(GitlabMessageConstant.DELETE_PROJECT_FAILED + ". " + e.getMessage());
        }
    }

    @Override
    public String revokeProjectAccessToken(Long projectId) throws Exception {
        try {
            return createProjectAccessToken(projectId).getToken();
        } catch (GitLabApiException e) {
            // Handle GitLab API exceptions
            throw new GitLabApiException(GitlabMessageConstant.GET_PROJECT_ACCESS_TOKEN_FAILED + ". " + e.getMessage());
        }
    }

    private ProjectAccessToken createProjectAccessToken(Long projectId) throws GitLabApiException {
        String name = "project-token";
        List<Constants.ProjectAccessTokenScope> scopes = new ArrayList<>();
        scopes.add(Constants.ProjectAccessTokenScope.API);
        scopes.add(Constants.ProjectAccessTokenScope.READ_REPOSITORY);
        scopes.add(Constants.ProjectAccessTokenScope.WRITE_REPOSITORY);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 1);
        Date expiresAt = calendar.getTime();
        ProjectAccessToken accessToken = gitLabApi.getProjectApi().createProjectAccessToken(
                projectId, name, scopes, expiresAt
        );

        UpdateWrapper<GitlabProject> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("gitlab_project_id", projectId);
        GitlabProject gitlabProject = new GitlabProject();
        gitlabProject.setProjectAccessToken(accessToken.getToken());
        projectMapper.update(gitlabProject, updateWrapper);
        return accessToken;
    }

    @Override
    public List<Commit> getCommitsRecord(Long projectId) throws GitLabApiException {
        return gitLabApi.getCommitsApi().getCommits(projectId);
    }

    @Override
    public List<Commit> getCommitsByIdAndBranchName(Long projectId, String branchName) throws GitLabApiException {
        List<Commit> allCommits = new ArrayList<>();
        Pager<Commit> pager = gitLabApi.getCommitsApi().getCommits(projectId, branchName, null, null, null, 100);

        do {
            allCommits.addAll(pager.current());
        } while (pager.hasNext() && pager.next() != null);

        return allCommits;
    }

    @Override
    public Map<String, Map<String, Integer>> calculateCommitCounts(Long projectId) {
        try {
            List<Branch> branches = branchService.getBranches(projectId);
            Map<String, Map<String, Integer>> commitCountsByAuthor = new ConcurrentHashMap<>();

            ExecutorService executor = Executors.newFixedThreadPool(branches.size());
            List<Future<Void>> futures = new ArrayList<>();

            for (Branch branch : branches) {
                futures.add(executor.submit(() -> {
                    List<Commit> commits = getAllCommits(projectId, branch.getName());
                    for (Commit commit : commits) {
                        String authorName = commit.getAuthorName();
                        String branchName = branch.getName();

                        commitCountsByAuthor
                                .computeIfAbsent(authorName, k -> new ConcurrentHashMap<>())
                                .merge(branchName, 1, Integer::sum);
                    }
                    return null;
                }));
            }

            for (Future<Void> future : futures) {
                future.get();
            }
            executor.shutdown();

            return commitCountsByAuthor;
        } catch (Exception e) {
            throw new RuntimeException(GradeMessageConstant.GET_COMMITS_FAILED, e);
        }
    }

    @Override
    public Map<String, Map<String, Integer>> calculateLinesChanged(Long projectId) {
        try {
            List<Branch> branches = branchService.getBranches(projectId);
            Map<String, Map<String, Integer>> linesChangedByAuthor = new ConcurrentHashMap<>();

            ExecutorService executor = Executors.newFixedThreadPool(branches.size());
            List<Future<Void>> futures = new ArrayList<>();

            for (Branch branch : branches) {
                futures.add(executor.submit(() -> {
                    List<Commit> commits = getAllCommitsWithDetails(projectId, branch.getName());
                    for (Commit commit : commits) {
                        String authorName = commit.getAuthorName();
                        String branchName = branch.getName();

                        CommitStats stats = commit.getStats();
                        if (stats != null) {
                            int linesChanged = stats.getAdditions() + stats.getDeletions();
                            linesChangedByAuthor
                                    .computeIfAbsent(authorName, k -> new ConcurrentHashMap<>())
                                    .merge(branchName, linesChanged, Integer::sum);
                        }
                    }
                    return null;
                }));
            }

            for (Future<Void> future : futures) {
                future.get();
            }
            executor.shutdown();

            return linesChangedByAuthor;
        } catch (Exception e) {
            throw new RuntimeException(GradeMessageConstant.GET_LINES_FAILED, e);
        }
    }

    @Override
    public ProjectDetailVO getProjectByTeamId(Long teamId) throws GitLabApiException {
        QueryWrapper<GitlabProject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("team_id", teamId);
        ProjectDetailVO projectDetailVO = BeanUtils.copyBean(projectMapper.selectOne(queryWrapper), ProjectDetailVO.class);
        List<Member> teamMembers = teamService.getTeamMembers(projectDetailVO.getGitlabProjectId());
        List<String> memberNames = teamMembers.stream()
                .map(Member::getUsername)
                .collect(Collectors.toList());
        projectDetailVO.setContributors(memberNames);
        return projectDetailVO;
    }

    //TODO
    @Override
    public List<ProjectVO> getHistoryProjectsByTaskId(Long taskId) {

        //gitLabApi.getProjectApi().getProject();
        return null;
    }

    public Group getGroup(String groupName) throws GitLabApiException {
        List<Group> groups = gitLabApi.getGroupApi().getGroups(groupName);

        // Check if the group already exists
        for (Group group : groups) {
            if (group.getName().equals(groupName)) {
                return group;
            }
        }

        // If the group does not exist, throw an exception
        throw new GitLabApiException(GitlabMessageConstant.GROUP_NOT_FOUND + ": " + groupName);
    }

    public Group getSubgroup(Group parentGroup, String subgroupName) throws GitLabApiException {
        List<Group> subgroups = gitLabApi.getGroupApi().getSubGroups(parentGroup.getId());

        // Check if the subgroup already exists
        for (Group subgroup : subgroups) {
            if (subgroup.getName().equals(subgroupName)) {
                return subgroup;
            }
        }

        // If the subgroup does not exist, throw an exception
        throw new GitLabApiException(GitlabMessageConstant.GROUP_NOT_FOUND + ": " + subgroupName);
    }

    private List<Commit> getAllCommits(Long projectId, String branchName) throws Exception {
        List<Commit> allCommits = new ArrayList<>();
        Pager<Commit> pager = gitLabApi.getCommitsApi().getCommits(projectId, branchName, null, null, null, 100);

        do {
            allCommits.addAll(pager.current());
        } while (pager.hasNext() && pager.next() != null);

        return allCommits;
    }

    private List<Commit> getAllCommitsWithDetails(Long projectId, String branchName) throws Exception {
        List<Commit> allCommits = new ArrayList<>();
        Pager<Commit> pager = gitLabApi.getCommitsApi().getCommits(projectId, branchName, null, null, null, 100);

        do {
            for (Commit commit : pager.current()) {
                allCommits.add(gitLabApi.getCommitsApi().getCommit(projectId, commit.getId()));
            }
        } while (pager.hasNext() && pager.next() != null);

        return allCommits;
    }
}
