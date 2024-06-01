package com.iss.gitlab.controller;

import org.apache.catalina.LifecycleState;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("gitlab/user")
public class UserController {
    private final GitLabApi gitLabApi;

    @Autowired
    public UserController(GitLabApi gitLabApi) {
        this.gitLabApi = gitLabApi;
    }

    @GetMapping("/{studentName}")
    public User getUser(@PathVariable String studentName) throws GitLabApiException {
        return gitLabApi.getUserApi().getUser(studentName);
    }

    @GetMapping("/")
    public List<User> getUsers() throws GitLabApiException {
        List<User> users = gitLabApi.getUserApi().getUsers();
        List<String> usernames = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            usernames.add(users.get(i).getUsername());
        }
        return users;
    }
}
