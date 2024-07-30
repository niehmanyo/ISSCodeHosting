package com.iss.gitlab.domain.vo;

import lombok.Data;

import java.util.List;

@Data
public class BranchVO {

    private Long branchId;

    private Long branchParentId;

    private String branchName;

    private List<BranchVO> subbranches;
}
