package com.iss.team.service.impl;

import com.iss.api.client.GitlabClient;
import com.iss.api.domain.dto.ProjectDTO;
import com.iss.team.domain.dto.StudentTeamDTO;
import com.iss.team.domain.dto.TeamDTO;
import com.iss.team.domain.entity.Team;
import com.iss.team.domain.vo.StudentVO;
import com.iss.team.mapper.TeamMapper;

import com.iss.team.service.ITeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements ITeamService {

    @Autowired
    private TeamMapper teamMapper;

    private final GitlabClient gitlabClient;

    @Override
    public List<Team> getAllTeams(String courseName) {
        return teamMapper.getAllTeams(courseName);
    }

    @Override
    public List<StudentVO> findUserByTeamId(Long id) {
        return teamMapper.findUserByTeamId(id);
    }

    @Override
    public void joinTeam(String studentName, String email, Long teamId) {
        int count = teamMapper.checkIfExist(studentName, teamId);
        if (count == 0) {
            teamMapper.joinTeam(studentName, email, teamId);
        }
    }

    @Override
    public int getCapacityByTeamId(Long teamId) {
        return teamMapper.getCapacityByTeamId(teamId);
    }

    @Override
    public void leaveTeam(StudentTeamDTO studentTeamDTO) {
        try {
            if (studentTeamDTO.getSize() > 0) {
                teamMapper.leaveTeam(studentTeamDTO.getUsername(), studentTeamDTO.getTeamId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Long getProjectByTeamId(Long teamId) {
        return teamMapper.getProjectByTeamId(teamId);
    }



    @Override
    @Transactional

    public void createTeam(TeamDTO teamDTO) {
        try {
            int groupNumber = teamDTO.getGroupNumber();
            for (int teamName = 1; teamName <= groupNumber; teamName++) {
                teamMapper.createTeam(
                        teamName,
                        teamDTO.getCourseName(),
                        teamDTO.getMajor(),
                        teamDTO.getCapacity(),
                        teamDTO.getDeadline()
                );
                Long teamId = teamMapper.checkTeamId(teamName, teamDTO.getCourseName());
                ProjectDTO projectDTO = new ProjectDTO();
                projectDTO.setProjectName(teamDTO.getMajor() + "-" + teamDTO.getCourseName() + "-team" + teamName);
                projectDTO.setTeamId(teamId);
                gitlabClient.createProject(projectDTO, teamDTO.getMajor(), teamDTO.getCourseName());
                Thread.sleep(5000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void deleteTeam(Long teamId) {
        teamMapper.deleteTeam(teamId);
    }
}
