package com.iss.team.service;


import com.iss.team.domain.dto.StudentTeamDTO;
import com.iss.team.domain.dto.TeamDTO;
import com.iss.team.domain.entity.Team;
import com.iss.team.domain.vo.StudentVO;
import org.gitlab4j.api.GitLabApiException;

import javax.transaction.Transactional;
import java.util.List;

public interface ITeamService  {
    List<Team> getAllTeams(String courseName);

    List<StudentVO> findUserByTeamId(Long id);

    int getCapacityByTeamId(Long teamId);

    void joinTeam(String username, String schoolEmail, Long teamId) throws GitLabApiException;

    void leaveTeam(StudentTeamDTO studentTeamDTO);

    Long getProjectByTeamId(Long teamId);

    void createTeam(TeamDTO teamDTO);

    void deleteTeam(Long teamId);
}
