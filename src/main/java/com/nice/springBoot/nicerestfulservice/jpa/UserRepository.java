package com.nice.springBoot.nicerestfulservice.jpa;

import com.nice.springBoot.nicerestfulservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
}
