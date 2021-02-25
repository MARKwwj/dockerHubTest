package com.magic.framework.redis.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class JedisClusterProperties {

    @Value("${spring.jedis.cluster.hosts:127.0.0.1:6379,127.0.0.1:6380,127.0.0.1:6381}")
    private String hosts;

    @Value("${spring.jedis.cluster.password:#{null}}")
    private String password;

    @Value("${spring.jedis.cluster.timeout:10}")
    private int timeout;

    @Value("${spring.jedis.cluster.connection-timeout:10}")
    private int connectionTimeout;

    @Value("${spring.jedis.cluster.max-attempts:3}")
    private int maxAttempts;

    @Value("${spring.jedis.cluster.max-active:8}")
    private int maxActive;

    @Value("${spring.jedis.cluster.max-idle:8}")
    private int maxIdle;

    @Value("${spring.jedis.cluster.min-idle:0}")
    private int minIdle;

    @Value("${spring.jedis.cluster.max-wait:-1}")
    private int maxWaitMillis;
}
