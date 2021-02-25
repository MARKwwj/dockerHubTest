package com.magic.framework.utils;

import com.magic.framework.rabbitmq.BaseRabbitmqMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class RabbitmqUtil {

    public static void sendMessage(BaseRabbitmqMessage rabbitmqMessage) {
        if (rabbitmqMessage == null) {
            return;
        }
        sendObject(rabbitmqMessage.getQueueName(), rabbitmqMessage);
    }
    public static void sendObject(String queueName, Object queueItem) {
        RabbitTemplate rabbitTemplate = SpringBeanUtil.getBean(RabbitTemplate.class);
        rabbitTemplate.convertAndSend(queueName, queueItem);
    }
}
