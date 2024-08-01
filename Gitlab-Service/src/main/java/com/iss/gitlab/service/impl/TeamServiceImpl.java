package com.iss.gitlab.service.impl;

import com.iss.common.constant.GitlabMessageConstant;
import com.iss.gitlab.domain.vo.ProjectVO;
import com.iss.gitlab.service.ITeamService;
import lombok.RequiredArgsConstructor;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.AccessLevel;
import org.gitlab4j.api.models.Member;
import org.gitlab4j.api.models.ProjectGroup;
import org.gitlab4j.api.models.User;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements ITeamService {

    private final GitLabApi gitLabApi;


    @Override
    public List<Member> getTeamMembers(Long projectId) throws GitLabApiException {
        try {
            return gitLabApi.getProjectApi().getMembers(projectId);
        } catch (GitLabApiException e) {
            throw new GitLabApiException(GitlabMessageConstant.PROJECT_NOT_FOUND + ". " + e.getMessage());
        }
    }

    @Override
    public List<ProjectGroup> getGroups(Long projectId) throws GitLabApiException {
        try {
            return gitLabApi.getProjectApi().getProjectGroups(projectId);
        } catch (GitLabApiException e) {
            throw new GitLabApiException(GitlabMessageConstant.PROJECT_NOT_FOUND + ". " + e.getMessage());
        }
    }

    /**
     * When creating user, the password won't be initialized in gitlab server.
     * The server will send a confirmation to email then user can set password,
     * then the password here is ""
     * */
    @Override
    @Async
    public User createUser(String email,
                           String username,
                           String name) throws GitLabApiException{
        try {
            User user = new User()
                    .withName(name)
                    .withUsername(username)
                    .withEmail(email);

            return gitLabApi.getUserApi().createUser(user,"",true);
        } catch (GitLabApiException e) {
            throw new GitLabApiException(GitlabMessageConstant.CREAT_USER_FAILED + ". " + e.getMessage());
        }
    }

    @Override
    @Async
    public void inviteUser(Long projectId, String email) throws GitLabApiException{
        try{
            User user = gitLabApi.getUserApi().getUserByEmail(email);
            gitLabApi.getProjectApi().addMember(projectId,user.getId(), AccessLevel.DEVELOPER);
        }catch (GitLabApiException e){
            throw new GitLabApiException(GitlabMessageConstant.INVITE_USER_FAILED + ". " + e.getMessage());
        }
    }

    @Override
    @Async
    public void removeUser(Long projectId, String email) throws GitLabApiException{
        try{
            User user = gitLabApi.getUserApi().getUserByEmail(email);
            gitLabApi.getProjectApi().removeMember(projectId, user.getId());
        }catch (GitLabApiException e){
            throw new GitLabApiException(GitlabMessageConstant.REMOVE_USER_FAILED + ". " + e.getMessage());
        }
    }


}
