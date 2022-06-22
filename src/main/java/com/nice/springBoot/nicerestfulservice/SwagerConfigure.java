package com.nice.springBoot.nicerestfulservice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableSwagger2
public class SwagerConfigure {
    private static final Contact DEFAULT_CONTATCT = new Contact("Ahn SeungHyun", "https://test.co.kr", "myemail@test.com");
    private static final ApiInfo DEFAULT_API_INFO = new ApiInfo("Api Test",
            "Api test swagger",
            "1.0",
            "nothing",
            DEFAULT_CONTATCT,
            "Apache 2.0",
            "aaa",
            new ArrayList<>()
    );
    private static final Set<String> DEFAULT_PRODUCES_AND_CONSUMES = new HashSet<>(Arrays.asList(
            "application/json", "application/xml"
        )
    );
    @Bean
    public Docket api(){
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(DEFAULT_API_INFO)
                .produces(DEFAULT_PRODUCES_AND_CONSUMES)
                .consumes(DEFAULT_PRODUCES_AND_CONSUMES);
    }
}
