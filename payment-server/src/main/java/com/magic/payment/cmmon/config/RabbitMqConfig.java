package com.magic.payment.cmmon.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    /**
     * 用于处理分成结果
     * @return
     */
    @Bean
    public Queue agentProfitResultQue() {
        return new Queue("agentProfitResultQue");
    }
}
