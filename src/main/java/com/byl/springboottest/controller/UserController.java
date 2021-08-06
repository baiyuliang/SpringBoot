package com.byl.springboottest.controller;

import com.byl.springboottest.bean.ResponseData;
import com.byl.springboottest.bean.User;
import com.byl.springboottest.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    UserService userService;

//    @GetMapping("/{id}")
//    public User getUserById(@PathVariable Integer id) {
//        return userService.getUserById(id);
//    }


    @GetMapping("/getUserByName")
    public User getUserByName(String username) {
        return userService.getUserByName(username);
    }


    @PostMapping("/login")
    public ResponseData login(@RequestParam Map<String, String> params) {
        if (params.get("username") == null || params.get("password") == null) return new ResponseData(-1, "账号或账号不能为空");
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(params.get("username"), params.get("password"));
//        usernamePasswordToken.setRememberMe(true);//记住密码
        try {
            subject.login(usernamePasswordToken);
            subject.hasRole("admin");
        } catch (UnknownAccountException e) {
            return new ResponseData(-1, "用户不存在");
        } catch (AuthenticationException e) {
            return new ResponseData(-1, "账号或密码错误");
        } catch (AuthorizationException e) {
            return new ResponseData(-1, "没有权限");
        }
        return new ResponseData(1, "登录成功");
    }

    @GetMapping("/list")
    public ResponseData list(Integer page, Integer limit) {
        if (page == null) page = 1;
        if (limit == null) limit = 10;
        return userService.getUserList(page, limit);
    }

    @GetMapping("/search")
    public ResponseData search(@RequestParam Map<String, String> params) {
        return new ResponseData(1, "");
    }

    @PostMapping("/reg")
    public ResponseData reg(@RequestParam Map<String, String> params) {
        return new ResponseData(1, "注册成功");
    }

    @GetMapping("/logout")
    public ResponseData logout() {
        return userService.logout();
    }
}