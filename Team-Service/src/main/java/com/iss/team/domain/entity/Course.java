package com.iss.team.domain.entity;

import lombok.Data;


@Data
public class Course {

    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
