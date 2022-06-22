package com.nice.springBoot.nicerestfulservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Post {
    @Id
    @GeneratedValue
    private Integer id;

    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    User user;
}
