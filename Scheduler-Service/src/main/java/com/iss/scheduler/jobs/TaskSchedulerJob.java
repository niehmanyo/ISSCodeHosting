package com.iss.scheduler.jobs;

import com.iss.api.client.GitlabClient;
import com.iss.api.client.TeamClient;
import com.iss.api.domain.vo.StudentVO;
import com.iss.api.domain.vo.TeamVO;
import com.iss.common.result.Result;
import lombok.RequiredArgsConstructor;
import org.gitlab4j.api.GitLabApiException;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TaskSchedulerJob implements Job {

    private final TeamClient teamClient;

    private final GitlabClient gitlabClient;

    @Override
    @Transactional
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //TODO Testing
        String courseName = (String) context.getJobDetail().getJobDataMap().get("courseName");
        List<TeamVO> teams = teamClient.getTeams(courseName).getData();
        for (TeamVO team : teams){
            try {
                for (StudentVO student : team.getUsers()){
                    gitlabClient.removeUser(team.getProject_id(), student.getEmail());
                }
            } catch (GitLabApiException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
