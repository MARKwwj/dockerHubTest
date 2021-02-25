package com.magic.framework.redis.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class JedisSentinelProperties {

    @Value("${spring.jedis.sentinel.hosts:127.0.0.1:6379,127.0.0.1:6380,127.0.0.1:6381}")
    private String hosts;

    @Value("${spring.jedis.sentinel.password:#{null}}")
    private String password;

    @Value("${spring.jedis.sentinel.master-name:master}")
    private String masterName;

    @Value("${spring.jedis.sentinel.timeout:10}")
    private int timeout;

    @Value("${spring.jedis.sentinel.max-active:8}")
    private int maxActive;

    @Value("${spring.jedis.sentinel.max-idle:8}")
    private int maxIdle;

    @Value("${spring.jedis.sentinel.min-idle:0}")
    private int minIdle;

    @Value("${spring.jedis.sentinel.max-wait:-1}")
    private int maxWaitMillis;
}
