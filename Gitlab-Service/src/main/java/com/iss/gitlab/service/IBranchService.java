package com.iss.gitlab.service;

import com.iss.gitlab.domain.dto.GroupDTO;
import com.iss.gitlab.domain.vo.BranchVO;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Branch;

import java.util.List;

public interface IBranchService {
    BranchVO createBranch(GroupDTO groupDTO);

    List<Branch> getBranches(Long projectId) throws GitLabApiException;
}
