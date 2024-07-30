package com.iss.gitlab.service.impl;

import com.iss.gitlab.domain.dto.GroupDTO;
import com.iss.gitlab.domain.vo.BranchVO;
import com.iss.gitlab.service.IBranchService;
import lombok.RequiredArgsConstructor;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Branch;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BranchServiceImpl implements IBranchService {

    private final GitLabApi gitLabApi;

    @Override
    public BranchVO createBranch(GroupDTO groupDTO) {
        return null;
    }

    @Override
    public List<Branch> getBranches(Long projectId) throws GitLabApiException {
        return gitLabApi.getRepositoryApi().getBranches(projectId);
    }
}
