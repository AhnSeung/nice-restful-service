package com.nice.springBoot.nicerestfulservice.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
//@JsonIgnoreProperties(value = {"password","ssn"})
//@JsonFilter("UserInfo")
@Entity
public class User {
    @Id
    @GeneratedValue
    private Integer id;

    @NotNull
    @Size(min=2, message = "name은 2글자 이상 필요 합니다.")
    @ApiModelProperty(notes = "사용자 이름을 입력해주세요")
    private String name;
    @Past
    private Date joinDate;

    private String ssn;
    private String password;

    @OneToMany(mappedBy = "user")
    private List<Post> posts;
}
