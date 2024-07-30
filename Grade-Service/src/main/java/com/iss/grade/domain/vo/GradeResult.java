package com.iss.grade.domain.vo;

import lombok.Data;

@Data
public class GradeResult {

    private Long id;

    private String studentName;

    private String repositoryUrl;

    private String branch;

    private int totalScore;
}