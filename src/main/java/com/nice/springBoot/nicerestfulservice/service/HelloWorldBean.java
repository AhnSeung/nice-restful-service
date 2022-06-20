package com.nice.springBoot.nicerestfulservice.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

@Data
@AllArgsConstructor
public class HelloWorldBean {
    private String message;
}
