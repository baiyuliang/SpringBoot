package com.byl.springboottest.rocket;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 消息接收监听
 */
@Component
public class MQMsgListener implements MessageListenerConcurrently {

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        if (msgs == null || msgs.size() == 0) {
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
        MessageExt messageExt = msgs.get(0);
        System.out.println("接受到的消息为：" + messageExt.toString());
        if (messageExt.getTopic().equals("你的Topic")) {
            if (messageExt.getTags().equals("你的Tag")) {
                //判断该消息是否重复消费（RocketMQ不保证消息不重复，如果你的业务需要保证严格的不重复消息，需要你自己在业务端去重）
                //获取该消息重试次数
                int reconsume = messageExt.getReconsumeTimes();
                if (reconsume == 3) {//消息已经重试了3次，如果不需要再次消费，则返回成功
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
                //处理对应的业务逻辑

            }
        }
        // 如果没有return success ，consumer会重新消费该消息，直到return success
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

}