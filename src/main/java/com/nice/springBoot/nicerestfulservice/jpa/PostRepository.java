package com.nice.springBoot.nicerestfulservice.jpa;

import com.nice.springBoot.nicerestfulservice.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post,Integer> {
}
