package com.byl.springboottest.service;

import com.byl.springboottest.bean.ResponseData;
import com.byl.springboottest.bean.User;

public interface UserService {

    User getUserById(Integer id);

    User getUserByName(String username);

    ResponseData login(String username, String password);

    ResponseData getUserList(Integer page,Integer limit);

    ResponseData logout();
}
