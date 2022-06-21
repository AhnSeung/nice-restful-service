package com.nice.springBoot.nicerestfulservice.controller;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.nice.springBoot.nicerestfulservice.customException.UserNotFoundException;
import com.nice.springBoot.nicerestfulservice.entity.User;
import com.nice.springBoot.nicerestfulservice.entity.UserV2;
import com.nice.springBoot.nicerestfulservice.service.UserDeoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class AdminUserController {
    private UserDeoService userDeoService;
    @Autowired
    public AdminUserController(UserDeoService userDeoService) {
        this.userDeoService = userDeoService;
    }

    @GetMapping(value = "/admin/users")
    public MappingJacksonValue retrieveAllUsers(){
        List<User> users = userDeoService.findAll();
        SimpleBeanPropertyFilter simpleBeanPropertyFilter = SimpleBeanPropertyFilter.filterOutAllExcept("id","name","ssn","joinDate");
        FilterProvider filter = new SimpleFilterProvider().addFilter("UserInfo",simpleBeanPropertyFilter);
        MappingJacksonValue mapping = new MappingJacksonValue(users);
        mapping.setFilters(filter);
        return mapping;
    }

    @GetMapping(value = "/admin/users/{id}/", headers="X-API-VERSION=1")
    public MappingJacksonValue retrieveUserById(@PathVariable(value = "id")int userId
    ) throws UserNotFoundException {
        User user = userDeoService.findUserById(userId);
        //예외처리
        if(user == null){
            throw new UserNotFoundException(String.format("UserNotFoundException : [%d]",userId));
        }
        SimpleBeanPropertyFilter simpleBeanPropertyFilter = SimpleBeanPropertyFilter.filterOutAllExcept("id","name","ssn","joinDate");
        FilterProvider filter = new SimpleFilterProvider().addFilter("UserInfo",simpleBeanPropertyFilter);
        MappingJacksonValue mapping = new MappingJacksonValue(user);
        mapping.setFilters(filter);
        return mapping;
    }

    @GetMapping(value="/admin/users/{id}/", headers="X-API-VERSION=2")
    public MappingJacksonValue retrieveUserByIdV2(@PathVariable(value = "id")int userId) throws UserNotFoundException {
        User user = userDeoService.findUserById(userId);
        //예외처리
        if(user == null){
            throw new UserNotFoundException(String.format("UserNotFoundException : [%d]",userId));
        }
        UserV2 userv2 = UserV2.builder().id(user.getId()).name(user.getName()).ssn(user.getSsn()).joinDate(user.getJoinDate()).grade("VIP").build();
        SimpleBeanPropertyFilter simpleBeanPropertyFilter = SimpleBeanPropertyFilter.filterOutAllExcept("id","name","ssn","joinDate","grade");
        FilterProvider filter = new SimpleFilterProvider().addFilter("UserInfoV2",simpleBeanPropertyFilter);
        MappingJacksonValue mapping = new MappingJacksonValue(userv2);
        mapping.setFilters(filter);
        return mapping;
    }
}
