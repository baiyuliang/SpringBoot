package com.byl.springboottest.dao;

import com.byl.springboottest.bean.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(value = "select * from user where id = ?", nativeQuery=true)
    User getUser(Integer id);

    User findByUsername(String username);

    User findByUsernameAndPassword(String username,String password);

    Page<User> findAll(Specification<User> spec, Pageable pageable);

}