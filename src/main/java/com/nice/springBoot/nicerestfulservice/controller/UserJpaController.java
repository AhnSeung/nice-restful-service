package com.nice.springBoot.nicerestfulservice.controller;

import com.nice.springBoot.nicerestfulservice.customException.UserNotFoundException;
import com.nice.springBoot.nicerestfulservice.entity.Post;
import com.nice.springBoot.nicerestfulservice.entity.ResponseData;
import com.nice.springBoot.nicerestfulservice.entity.User;
import com.nice.springBoot.nicerestfulservice.jpa.PostRepository;
import com.nice.springBoot.nicerestfulservice.jpa.UserRepository;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/jpa")
public class UserJpaController {
    private UserRepository userRepository;
    private PostRepository postRepository;

    @Autowired
    public UserJpaController(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }
    @GetMapping(value = "/users")
    public ResponseEntity retrieveAllUsers(){
        List<User> users = userRepository.findAll();
        int Cnt = (int)userRepository.count();
        List<EntityModel<User>> result = new ArrayList<>();
        for(User user : users){
            EntityModel<User> entityModel = EntityModel.of(user);
            WebMvcLinkBuilder linkTo = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).retrieveFindByIdUser(user.getId()));
            entityModel.add(linkTo.withRel("userDetail"));
            result.add(entityModel);
        }
        ResponseData responseData = ResponseData.builder().count(Cnt).users(result).build();
        //,WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).retrieveAllUsers()).withSelfRel())
        return ResponseEntity.ok(responseData);
    }

    @GetMapping(value = "/users/{id}")
    public ResponseEntity retrieveFindByIdUser(@PathVariable(value = "id") int user_id){
        Optional<User> result = userRepository.findById(user_id);
        if(result == null){
            throw new UserNotFoundException("User Not Find");
        }
        //Hateoas 적용
        EntityModel<User> entityModel = EntityModel.of(result.get());
        WebMvcLinkBuilder linkTo = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).retrieveFindByIdUser(user_id));
        entityModel.add(linkTo.withRel("user"));
        return ResponseEntity.ok(entityModel);
    }

    @PutMapping(value = "/users/{id}")
    public ResponseEntity updateUserById(@PathVariable(value = "id") int userId,
                                         @RequestBody User user
    ){
        //현재 해당 ID가 있는지 먼저 조회
        Optional<User> checkId = userRepository.findById(userId);

        //Id없으면 예외발생
        if(!checkId.isPresent()){
            throw new UserNotFoundException("User Not Found Exception");
        }

        //이제 업데이트
        user.setId(userId);
        userRepository.save(user);

        return ResponseEntity.noContent().build(); // 204 HttpState Code return
    }

    @GetMapping(value = "/users/{id}/posts")
    public List<Post> retrieveAllPostsByUser(@PathVariable(value="id")int userId){
        Optional<User> user = userRepository.findById(userId);

        if(!user.isPresent()){
            throw new UserNotFoundException("User Not Found");
        }

        return user.get().getPosts();
    }

}
