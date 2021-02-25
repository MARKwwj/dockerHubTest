package com.magic.framework.redis.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class JedisShardingProperties {

    @Value("${spring.jedis.sharding.host:127.0.0.1:127.0.0.1:6379,127.0.0.1:6380,127.0.0.1:6381}")
    private String hosts;

    @Value("${spring.jedis.sharding.password:#{null}}")
    private String password;

    @Value("${spring.jedis.sharding.timeout:-1}")
    private int timeout;

    @Value("${spring.jedis.sharding.max-active:8}")
    private int maxActive;

    @Value("${spring.jedis.sharding.max-idle:8}")
    private int maxIdle;

    @Value("${spring.jedis.sharding.min-idle:0}")
    private int minIdle;

    @Value("${spring.jedis.sharding.max-wait:-1}")
    private int maxWaitMillis;
}
