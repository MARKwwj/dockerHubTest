package com.magic.framework.redis.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class JedisStandaloneProperties {

    @Value("${spring.jedis.standalone.host:127.0.0.1}")
    private String host;

    @Value("${spring.jedis.standalone.port:6379}")
    private int port;

    @Value("${spring.jedis.standalone.password:#{null}}")
    private String password;

    @Value("${spring.jedis.standalone.timeout:2000}")
    private int timeout;

    @Value("${spring.jedis.standalone.max-active:8}")
    private int maxActive;

    @Value("${spring.jedis.standalone.max-idle:8}")
    private int maxIdle;

    @Value("${spring.jedis.standalone.min-idle:0}")
    private int minIdle;

    @Value("${spring.jedis.standalone.max-wait:-1}")
    private int maxWaitMillis;
}
