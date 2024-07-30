package com.iss.common.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class TimeEntity {
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
