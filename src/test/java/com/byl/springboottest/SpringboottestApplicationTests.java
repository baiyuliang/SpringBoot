package com.byl.springboottest;

import com.alibaba.fastjson.JSON;
import com.byl.springboottest.bean.ResponseData;
import com.byl.springboottest.bean.User;
import com.byl.springboottest.dao.UserRepository;
import com.byl.springboottest.service.UserService;
import com.byl.springboottest.utils.SaltUtil;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Optional;

@SpringBootTest
class SpringboottestApplicationTests {

    @Resource
    UserRepository userRepository;

    @Test
    void contextLoads() {

    }

    @Test
    void testDao() {
        Optional<User> optional = userRepository.findById(2);
        System.out.println("获取用户成功>>" + optional.get().getNickname());
    }

    @Resource
    DefaultMQProducer defaultMQProducer;

    @Test
    void testSendRocketMq() throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        String msg = "rocketmq发送查询消息：查询成功1";
        Message sendMsg = new Message("MyTopic", "MyTag", msg.getBytes());
        sendMsg.setKeys("testKey");
        SendResult sendResult = defaultMQProducer.send(sendMsg);
        SendStatus sendStatus = sendResult.getSendStatus();
        System.out.println("发送状态：" + sendStatus);
        System.out.println("发送信息：" + sendResult.toString());
    }

    @Test
    void testSendRocketMq2() throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        String msg = "rocketmq发送查询消息：查询成功2";
        Message sendMsg = new Message("MyTopic", "MyTag2", msg.getBytes());
        sendMsg.setKeys("testKey");
        SendResult sendResult = defaultMQProducer.send(sendMsg);
        SendStatus sendStatus = sendResult.getSendStatus();
        System.out.println("发送状态：" + sendStatus);
        System.out.println("发送信息：" + sendResult.toString());
    }

    @Resource
    UserService userService;

    @Test
    void testLogin() {
        ResponseData responseData = userService.login("admin", "admin");
        System.out.println(JSON.toJSON(responseData));
    }

    @Test
    void testPwdSalt(){
        SaltUtil.encryptPassword("admin","admin");
    }

}
