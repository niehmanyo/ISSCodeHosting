package com.iss.team.mapper;

import com.iss.team.domain.entity.Team;
import com.iss.team.domain.vo.StudentVO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface TeamMapper {

    List<Team> getAllTeams(String courseName);

    List<StudentVO> findUserByTeamId(Long id);

    void joinTeam(String studentName,String email, Long teamId);

    void leaveTeam(String username, Long teamId);

    int getCapacityByTeamId(Long teamId);

    Long getProjectByTeamId(Long teamId);

    Long createTeam(Integer teamName, String courseName, String major, Integer capacity, LocalDateTime deadline);

    void deleteTeam(Long teamId);

    int checkIfExist(String username,Long teamId);

    Long checkTeamId(int teamName,String courseName);

    void updateTeamInfo(Long teamId, Long projectId, String projectUrl, String projectAccessToken);
}
