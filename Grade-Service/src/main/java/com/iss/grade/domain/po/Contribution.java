package com.iss.grade.domain.po;

import lombok.Data;

@Data
public class Contribution {

    private String studentName;

    private int commitCount;

    private int linesOfCode;

    public Contribution(String studentName, int commitCount, int linesOfCode) {
        this.studentName = studentName;
        this.commitCount = commitCount;
        this.linesOfCode = linesOfCode;
    }
}
