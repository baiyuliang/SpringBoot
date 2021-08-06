package com.byl.springboottest.controller;

import com.byl.springboottest.bean.User;
import com.byl.springboottest.dao.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

//@Controller
//public class TestController {
//
//    @RequestMapping(value = "/hello",method = RequestMethod.GET)
//    @ResponseBody
//    public String hello() {
//        return "Hello World!";
//    }
//
//    @RequestMapping(value = "/success",method = RequestMethod.GET)
//    public String success() {
//        return "success";
//    }
//
//}

@RestController
@RequestMapping("/test")
public class TestController {

    @Resource
    UserRepository userRepository;

    @GetMapping("/hello")
    public String hello() {
        return "Hello World!";
    }

    @GetMapping("/user")
    public User getUser(Integer id) {
        return userRepository.getOne(id);
    }
}