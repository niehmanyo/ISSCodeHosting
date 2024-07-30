package com.iss.scheduler.jobs;

import com.iss.api.client.GitlabClient;
import com.iss.api.client.TeamClient;
import com.iss.api.domain.vo.StudentVO;
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
public class TeamSchedulerJob implements Job {

    private final TeamClient teamClient;

    private final GitlabClient gitlabClient;

    @Override
    @Transactional
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Long teamId = (Long) context.getJobDetail().getJobDataMap().get("teamId");
        Long projectId = teamClient.getProjectByTeamId(teamId).getData();
        List<StudentVO> members = teamClient.getMembersByTeamId(teamId).getData();
        for (StudentVO member: members){
            try {
                gitlabClient.inviteUser(projectId, member.getEmail());
            } catch (GitLabApiException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
