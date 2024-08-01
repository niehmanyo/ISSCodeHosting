package com.iss.api.client;



import com.iss.api.domain.dto.ProjectInfoDTO;
import com.iss.api.domain.vo.StudentVO;
import com.iss.api.domain.vo.TeamVO;
import com.iss.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "team-service", path = "/teams")
public interface TeamClient {

    @GetMapping("getMembersByTeamId/{teamId}")
    Result<List<StudentVO>> getMembersByTeamId(@PathVariable("teamId") Long teamId);

    @GetMapping("project/{teamId}")
    Result<Long> getProjectByTeamId(@PathVariable("teamId") Long teamId);

    @GetMapping("get/{courseName}")
    Result<List<TeamVO>> getTeams(@PathVariable("courseName")String courseName);

    @PostMapping("update")
    Result<String> updateTeamInfo(@RequestBody ProjectInfoDTO projectInfoDTO);
}
