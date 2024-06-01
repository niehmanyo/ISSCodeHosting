package com.iss.gitlab.controller;

import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.DeployKey;
import org.gitlab4j.api.models.Key;
import org.gitlab4j.api.models.Project;
import org.gitlab4j.api.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("Manage project")
@RequestMapping("/gitlab/project")
public class ProjectController {
    private final GitLabApi gitLabApi;

    @Autowired
    public ProjectController(GitLabApi gitLabApi) {
        this.gitLabApi = gitLabApi;
    }

    @GetMapping("/getProjects")
    public List<Project> getProjects() throws GitLabApiException {
        return gitLabApi.getProjectApi().getProjects();
    }

    @PostMapping("/addAdmin")
    public String addAdmin() throws GitLabApiException {
        User user = new User()
                .withEmail("niehwenyu@163.com")
                .withUsername("niehmanyo")
                .withName("NIE WENYU");
        gitLabApi.getUserApi()
                .createUser(user,"nwy0522.",true)
                .setIsAdmin(true);
        return "true";
    }

    @GetMapping("/getDeployKeys")
    public List<DeployKey> getDeployKeys() throws GitLabApiException {
        return gitLabApi.getDeployKeysApi().getDeployKeys();
    }
    @PostMapping("/create-project")
    public Project createProject() throws GitLabApiException, GitLabApiException {
        Project project = new Project()
                .withName("default-project-name2")
                .withDescription("Default project description");
        return gitLabApi.getProjectApi().createProject(project);
    }

    @PostMapping("/addSsh")
    public String addSsh() throws GitLabApiException {
        gitLabApi.getDeployKeysApi().addDeployKey(1L,"groupName","ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABgQDGOff6m3aVNpXkOxJlm61pQI/oYI+gCpbi97AL/SO7AqadjaGstinpA5HNEZ1s3CRFzdeMDgomgSKGWLkWxgbRg63arAJIz6Cm3OGys2BIecgbjbAwnKwpxErUEat53Sw1YxiCV6Qhp9DKTCSqStdTfN6MCs11gZdBT5xw1LgRW86wRUf+XOvDwapSQ8p6185HGR2t5i9Pa3Hd70G+XGtyNe+W69bjN1BTYy2pWzv6/2lpwSxiDt8lqewH+RxxVjJic6tj7BG4hDMxnt74G01csSedhEdBZ96PYS+fDD6tNNgWAxLQy55KCWEzmF8q9ChFwA3yqoaBSoId80ZO6Ys2ihf0l+khs5W2euFIrJhy/x+4Li+PyAyW9YPoR9tiggCs3T98pMWBv2sC/eya7FLyQy1Ft6jYG0lL/A4mjFixlCQ45oJN5hFgdRawVMSgAeeK1ALWsa6L5WYRbOl8/M8msgSNwMUZ+pvWBF4TABmkMnuOyINnkJDluA0N37pr+20= niehwenyu@163.com",true);

        return "";
    }
}
