package com.iss.gitlab.service;

import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Member;
import org.gitlab4j.api.models.ProjectGroup;
import org.gitlab4j.api.models.User;

import java.util.List;

public interface ITeamService {
    List<Member> getTeamMembers(Long projectId) throws GitLabApiException;


    List<ProjectGroup> getGroups(Long projectId) throws GitLabApiException;

    User createUser(String email,
                    String username,
                    String name) throws GitLabApiException;

    void inviteUser(Long teamId, String email) throws GitLabApiException;

    void removeUser(Long projectId, String email) throws GitLabApiException;
}
