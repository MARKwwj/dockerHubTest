package com.magic.longvideo.common.config;

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
    public Queue agentProfitResult() {
        return new Queue("agentProfitResult");
    }

    /**
     * 用于处理分成结果
     * @return
     */
    @Bean
    public Queue bindUser() {
        return new Queue("bindUser");
    }
}
