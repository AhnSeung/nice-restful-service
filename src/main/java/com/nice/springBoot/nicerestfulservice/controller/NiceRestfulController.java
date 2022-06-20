package com.nice.springBoot.nicerestfulservice.controller;

import com.nice.springBoot.nicerestfulservice.service.Author;
import com.nice.springBoot.nicerestfulservice.service.HelloWorldBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class NiceRestfulController {
    // Method 결정 -> 처음엔 GET
    // URI 결정(EndPoint 결정)
    // Java Method 결정(RequestMapping->요청 받아들일 Http-Method에 맞는 매핑방식필요)
    @GetMapping(value = "/hello-world")
    public String helloWorld(){
        return "Hello World";
    }

    @GetMapping(value="/hello-world-bean/{param}")
    public HelloWorldBean helloWorldBean(@PathVariable(value = "param") String name){
        String msg = String.format("Hello World %s",name);
        return new HelloWorldBean(msg);
    }

    @GetMapping(value="/books/{bookId}/authors/{authorId}")
    public Author getAuthorNm(@PathVariable(value = "bookId") String book_id,
                              @PathVariable(value="authorId") String author_id){
        return Author.builder().
                bookId(book_id).
                id(author_id).
                name("bookMaker").build();
    }

}
