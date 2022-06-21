package com.nice.springBoot.nicerestfulservice.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonFilter("UserInfoV2")
public class UserV2 {
    private int id;
    private String name;
    private String ssn;
    private String grade;
    private Date joinDate;
}
