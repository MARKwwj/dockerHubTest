package com.magic.longvideo.comsumer;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RabbitMQConsumer {

    /**
     * 处理代理分成后的结果，将结果同步到数据库中
     *
     * @param message
     */
    @RabbitListener(queues = "agentProfitResult")
    @RabbitHandler
    public void agentProfitResult(Map<String, Object> message) {
        //TODO 数据落地
    }

}
