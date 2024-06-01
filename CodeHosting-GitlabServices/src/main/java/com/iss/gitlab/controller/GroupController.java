package com.iss.gitlab.controller;


import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.GroupApi;
import org.gitlab4j.api.models.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller("manage group")
@RequestMapping("gitlab/group")
public class GroupController {
    private final GitLabApi gitLabApi;

    @Autowired
    public GroupController(GitLabApi gitLabApi) {
        this.gitLabApi = gitLabApi;
    }
    @GetMapping("/{studentId}")
    public List<Group> getGroup(@PathVariable Integer studentId) throws GitLabApiException {


        return gitLabApi.getGroupApi().getGroups();
    }

}
