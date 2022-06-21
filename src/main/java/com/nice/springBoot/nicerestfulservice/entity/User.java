package com.nice.springBoot.nicerestfulservice.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
//@JsonIgnoreProperties(value = {"password","ssn"})
@JsonFilter("UserInfo")
public class User {
    private Integer id;
    @NotNull
    @Size(min=2, message = "name은 2글자 이상 필요 합니다.")
    private String name;
    @Past
    private Date joinDate;
    private String ssn;
    private String password;
}
