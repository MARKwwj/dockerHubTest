package com.magic.framework.redis.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class JedisInitProperties {
    // 可能的值：standalone, sentinel, cluster, sharding
    @Value("${spring.jedis.use:#{null}}")
    private String use;
}
