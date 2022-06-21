package com.nice.springBoot.nicerestfulservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@RestController
public class LocaleController {
    @Autowired
    public LocaleController(MessageSource messageSource, LocaleResolver localeResolver) {
        this.messageSource = messageSource;
        this.localeResolver = localeResolver;
    }
    //Locale 설정
    private MessageSource messageSource;
    private LocaleResolver localeResolver;

    @GetMapping(value = "/hello-internationalized")
    public String helloInternationalized(@RequestHeader(name="Accept-Language", required = false) Locale locale,
                                         HttpServletRequest request){
        locale = locale == null ? localeResolver.resolveLocale(request) : locale;
        return messageSource.getMessage("greeting.message",null,locale);
    }

}
