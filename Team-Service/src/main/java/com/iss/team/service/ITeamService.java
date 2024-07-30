package com.iss.team.service;

import com.iss.team.domain.dto.StudentTeamDTO;
import com.iss.team.domain.dto.TeamDTO;
import com.iss.team.domain.entity.Team;
import com.iss.team.domain.vo.StudentVO;

import java.util.List;

public interface ITeamService  {
    List<Team> getAllTeams(String courseName);

    List<StudentVO> findUserByTeamId(Long id);

    int getCapacityByTeamId(Long teamId);

    void joinTeam(String username, String schoolEmail, Long teamId);

    void leaveTeam(StudentTeamDTO studentTeamDTO);

    Long getProjectByTeamId(Long teamId);

    void createTeam(TeamDTO teamDTO);

    void deleteTeam(Long teamId);
}
