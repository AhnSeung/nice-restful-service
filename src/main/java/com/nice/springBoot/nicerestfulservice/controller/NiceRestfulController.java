package com.nice.springBoot.nicerestfulservice.controller;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.nice.springBoot.nicerestfulservice.customException.UserNotFoundException;
import com.nice.springBoot.nicerestfulservice.entity.User;
import com.nice.springBoot.nicerestfulservice.service.UserDeoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.swing.text.html.parser.Entity;
import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping("/users")
public class NiceRestfulController {

    // Method 결정 -> 처음엔 GET
    // URI 결정(EndPoint 결정)
    // Java Method 결정(RequestMapping->요청 받아들일 Http-Method에 맞는 매핑방식필요)
    private UserDeoService userDeoService;

    @Autowired
    public NiceRestfulController(UserDeoService userDeoService) {
        this.userDeoService = userDeoService;
    }

    @GetMapping(value = "")
    public ResponseEntity<CollectionModel<EntityModel<User>>> retrieveAllUsers(){
        List<EntityModel<User>> result = new ArrayList<>();
        List<User> users = userDeoService.findAll();
        //Hateos 이용하기 위해 엔티티 모델 선언
        for(User user : users){
            EntityModel entityModel = EntityModel.of(users);
            //웹링크선언해서 현재 클래스의 요청처리 메소드를 연결해줌(그러면 해당 링크값이 생성됨)
            Link linkTo = linkTo(methodOn(this.getClass()).retrieveUserById(user.getId())).withRel("View");
            //엔티티모델과 웹링크 연결(엔티티모델 객체 각각에 링크값이 추가됨)
            entityModel.add(linkTo.withRel("all-users"));
            result.add(entityModel); // 리턴객체에 각 엔티티들 추가해준다
        }
        return ResponseEntity.ok(CollectionModel.of(result, linkTo(methodOn(this.getClass()).retrieveAllUsers()).withSelfRel()));

//        SimpleBeanPropertyFilter simpleBeanPropertyFilter = SimpleBeanPropertyFilter.filterOutAllExcept("id","name","ssn","joinDate");
//        FilterProvider filter = new SimpleFilterProvider().addFilter("UserInfo",simpleBeanPropertyFilter);
//        MappingJacksonValue mapping = new MappingJacksonValue(users);
//        mapping.setFilters(filter);
//        return mapping;
    }

    @PostMapping(value = "")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user){
        log.info("create user info => " + user.toString());
        User saveInfo = userDeoService.save(user);
        //현재 요청온 path에다가 추가적으로 뒤에 커스트마이징된 path(등록된 ID 객체 이용해서) 추가해서 URI형태로 만든다
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(saveInfo.getId()).toUri();
        //String msg = String.format("Registered your ID : [%d]",userDeoService.save(user).getId());
        //URI 형태로 만든 location값을 리턴한다
        //리턴된 경로가 유저 상세 정보 페이지를 볼 수 있는 경로랑 매칭 가능하다
        //본인의 ID를 알 수 있으면서 상세정보 요청 경로라 볼 수 있기에 Rest-API 설계시 좋다(연관정보를 리턴 해주니깐?)
        return ResponseEntity.created(location).build();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity retrieveUserById(@PathVariable(value = "id")int userId) throws UserNotFoundException {
        User user = userDeoService.findUserById(userId);
        //예외처리
        if(user == null){
            throw new UserNotFoundException(String.format("UserNotFoundException : [%d]",userId));
        }
//        SimpleBeanPropertyFilter simpleBeanPropertyFilter = SimpleBeanPropertyFilter.filterOutAllExcept("id","name","joinDate");
//        FilterProvider filter = new SimpleFilterProvider().addFilter("UserInfo",simpleBeanPropertyFilter);
//        MappingJacksonValue mapping = new MappingJacksonValue(user);
//        mapping.setFilters(filter);
//        return mapping;
        //Hateos 이용하기 위해 엔티티 모델 선언
        EntityModel entityModel = EntityModel.of(user);
        //웹링크선언
        WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveUserById(userId));
        //엔티티모델과 웹링크 연결
        entityModel.add(linkTo.withRel("all-users"));

        return ResponseEntity.ok(entityModel);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteByUserId(@PathVariable(value = "id") int userId){
        log.info("delete request id=>" + userId);
        User user = userDeoService.deleteByUserId(userId);
        if(user == null){
            throw new UserNotFoundException(String.format("User Not Found : [ %d ]",userId));
        }
        //Hateos 이용하기 위해 엔티티 모델 선언
        EntityModel entityModel = EntityModel.of(user);
        //웹링크선언
        WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllUsers());
        //엔티티모델과 웹링크 연결
        entityModel.add(linkTo.withRel("all-users"));

        return ResponseEntity.ok(entityModel);
    }

    @PutMapping(value="/{id}")
    public User modifyByUserId(@PathVariable(value = "id") int userId, @RequestBody User user){
        user.setId(userId);
        User result = userDeoService.modifyUser(user);
        if(result == null){
            throw new UserNotFoundException(String.format("User Not Found : [ %d ]",user.getId()));
        }
        return result;
    }

}
