package com.nice.springBoot.nicerestfulservice.service;

import com.nice.springBoot.nicerestfulservice.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Component
public class UserDeoService {
    private static List<User> users = new ArrayList<>();
    private static int usersCount = 3;

    //init users
    static{
        users.add(User.builder().id(1).name("Kenneth").joinDate(new Date()).password("12345").ssn("999999-1111111").build());
        users.add(User.builder().id(2).name("Alice").joinDate(new Date()).password("12346").ssn("999999-2222222").build());
        users.add(User.builder().id(3).name("Elena").joinDate(new Date()).password("12347").ssn("999999-3333333").build());
    }

    //select users field
    public List<User> findAll(){
        return users;
    }

    //user add
    public User save(User user){
        if(user.getId() == null){
            user.setId(++usersCount);
        }
        users.add(user);
        return user;
    }

    //user find
    public User findUserById(@Valid int userId){
        for(User user : users){
            if(user.getId() == userId){
                return user;
            }
        }
        return null;
    }

    //user delete by id
    public User deleteByUserId(int userId){
        int index = 0;
        /*실시간으로 리스트를 remove(index)형태로 작업하면 add, delete 작업에 의해서
          index가 가변적이게 되는 경우로 인해 오류 발생한다.
          따라서 리스트 삭제도 Iterator 이용해서 지우는게 안전하다
         */
        Iterator<User> it = users.iterator();
        while(it.hasNext()){
            User user = it.next();

            if(user.getId() == userId){
                it.remove();
                usersCount--;
                return user;
            }
        }
//        for(User user : users){
//            if(user.getId() == userId){
//                index = users.indexOf(user);
//                users.remove(index);
//                usersCount--;
//            }
//        }
        return null;
    }

    public User modifyUser(User reqUser){
        Iterator<User> it = users.iterator();
        while(it.hasNext()){
            User user = it.next();
            if(user.getId() == reqUser.getId()){
                //user 자체가 static이라서 전역으로 공유되는 객체임(내부 값을 어디서 수정하던 적용됨)
                user.setName(reqUser.getName());
                user.setJoinDate(reqUser.getJoinDate());
                return user;
            }
        }
        return null;
    }
}
