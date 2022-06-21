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
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Date;
import java.util.List;

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
    public MappingJacksonValue retrieveAllUsers(){
        List<User> users = userDeoService.findAll();
        SimpleBeanPropertyFilter simpleBeanPropertyFilter = SimpleBeanPropertyFilter.filterOutAllExcept("id","name","ssn","joinDate");
        FilterProvider filter = new SimpleFilterProvider().addFilter("UserInfo",simpleBeanPropertyFilter);
        MappingJacksonValue mapping = new MappingJacksonValue(users);
        mapping.setFilters(filter);
        return mapping;
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
    public MappingJacksonValue retrieveUserById(@PathVariable(value = "id")int userId) throws UserNotFoundException {
        User user = userDeoService.findUserById(userId);
        //예외처리
        if(user == null){
            throw new UserNotFoundException(String.format("UserNotFoundException : [%d]",userId));
        }
        SimpleBeanPropertyFilter simpleBeanPropertyFilter = SimpleBeanPropertyFilter.filterOutAllExcept("id","name","joinDate");
        FilterProvider filter = new SimpleFilterProvider().addFilter("UserInfo",simpleBeanPropertyFilter);
        MappingJacksonValue mapping = new MappingJacksonValue(user);
        mapping.setFilters(filter);
        return mapping;
    }

    @DeleteMapping(value = "/{id}")
    public User deleteByUserId(@PathVariable(value = "id") int userId){
        log.info("delete request id=>" + userId);
        User user = userDeoService.deleteByUserId(userId);
        if(user == null){
            throw new UserNotFoundException(String.format("User Not Found : [ %d ]",userId));
        }
        return user;
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
