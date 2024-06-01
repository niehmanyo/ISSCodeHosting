package com.iss.cicd.controller;

import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Pipeline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cicd/pipelines")
public class CicdController {

    @Autowired
    private GitLabApi gitLabApi;
    
    @GetMapping("/{projectId}")
    public List<Pipeline> getPipelines(@PathVariable Integer projectId) throws GitLabApiException {
        return gitLabApi.getPipelineApi().getPipelines(projectId);
    }
}
