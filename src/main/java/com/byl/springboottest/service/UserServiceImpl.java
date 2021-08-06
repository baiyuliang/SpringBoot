package com.byl.springboottest.service;

import com.byl.springboottest.bean.ResponseData;
import com.byl.springboottest.bean.User;
import com.byl.springboottest.dao.UserRepository;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    UserRepository userRepository;
    @Resource
    DefaultMQProducer defaultMQProducer;

    @Override
    public User getUserById(Integer id) {
        System.out.println("getUserById：" + id);
        User user = userRepository.findById(id).get();
        Message sendMsg = new Message("MyTopic", "login", "loginSuccess".getBytes());
        SendResult sendResult = null;
        try {
            sendResult = defaultMQProducer.send(sendMsg);
        } catch (MQClientException | InterruptedException | MQBrokerException | RemotingException e) {
            e.printStackTrace();
        }
        SendStatus sendStatus = sendResult.getSendStatus();
        System.out.println("状态：" + sendStatus);
        System.out.println("信息：" + sendResult.toString());
        return user;
    }

    @Override
    public User getUserByName(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public ResponseData login(String username, String password) {
        ResponseData responseData = new ResponseData();
        User user = userRepository.findByUsernameAndPassword(username, password);
        if (user == null) {
            responseData.setCode(-1);
            responseData.setMsg("账号或密码错误");
        } else {
            responseData.setCode(1);
            responseData.setMsg("登录成功");
            responseData.setData(user);
        }
        return responseData;
    }

    @Override
    public ResponseData getUserList(Integer page, Integer limit) {
        ResponseData responseData = new ResponseData();
        Specification<User> specification = (Specification<User>) (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<User> userPage = userRepository.findAll(specification, pageable);
        responseData.setCode(1);
        responseData.setMsg("获取用户列表成功");
        responseData.setData(toList((int) userPage.getTotalElements(), userPage.getContent()));
        return responseData;
    }


    @Override
    public ResponseData logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return new ResponseData(1, "退出成功");
    }

    public Map<String, Object> toList(int total, List list) {
        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        map.put("total", total);
        return map;
    }
}
