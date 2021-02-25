package com.magic.framework.redis.config;

import com.magic.framework.redis.JedisUtil;
import com.magic.framework.redis.properties.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.util.StringUtils;
import redis.clients.jedis.*;

import javax.annotation.PostConstruct;
import java.util.*;

@Configuration
public class JedisConfig {
    private final JedisStandaloneProperties jedisStandaloneProperties;
    private final JedisSentinelProperties jedisSentinelProperties;
    private final JedisShardingProperties jedisShardingProperties;
    private final JedisClusterProperties jedisClusterProperties;
    private final JedisInitProperties jedisInitProperties;

    public JedisConfig(
            JedisStandaloneProperties jedisStandaloneProperties,
            JedisSentinelProperties jedisSentinelProperties,
            JedisShardingProperties jedisShardingProperties,
            JedisClusterProperties jedisClusterProperties,
            JedisInitProperties jedisInitProperties
    ) {
        this.jedisStandaloneProperties = jedisStandaloneProperties;
        this.jedisSentinelProperties = jedisSentinelProperties;
        this.jedisShardingProperties = jedisShardingProperties;
        this.jedisClusterProperties = jedisClusterProperties;
        this.jedisInitProperties = jedisInitProperties;
    }

    @PostConstruct
    private void init() {
        JedisUtil.init(jedisInitProperties.getUse());
    }

    @Bean
    @Lazy
    public JedisPool standalone() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(jedisStandaloneProperties.getMaxActive());
        jedisPoolConfig.setMaxIdle(jedisStandaloneProperties.getMaxIdle());
        jedisPoolConfig.setMinIdle(jedisStandaloneProperties.getMinIdle());
        jedisPoolConfig.setMaxWaitMillis(jedisStandaloneProperties.getMaxWaitMillis());
        JedisPool jedisPool = new JedisPool(
                jedisPoolConfig,
                jedisStandaloneProperties.getHost(),
                jedisStandaloneProperties.getPort(),
                jedisStandaloneProperties.getTimeout(),
                StringUtils.isEmpty(jedisStandaloneProperties.getPassword()) ? null : jedisStandaloneProperties.getPassword()
        );
        Jedis client = jedisPool.getResource();
        client.ping();
        client.close();
        return jedisPool;
    }

    @Bean
    @Lazy
    public JedisSentinelPool sentinel() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(jedisSentinelProperties.getMaxActive());
        jedisPoolConfig.setMaxIdle(jedisSentinelProperties.getMaxIdle());
        jedisPoolConfig.setMinIdle(jedisSentinelProperties.getMinIdle());
        jedisPoolConfig.setMaxWaitMillis(jedisSentinelProperties.getMaxWaitMillis());
        String[] servers = jedisSentinelProperties.getHosts().split(",");
        Set<String> sentinels = new HashSet<>();
        for (String server : servers) {
            sentinels.add(server);
        }
        JedisSentinelPool sentinelPool = new JedisSentinelPool(
                jedisSentinelProperties.getMasterName(),
                sentinels,
                jedisPoolConfig,
                jedisSentinelProperties.getTimeout(),
                StringUtils.isEmpty(jedisSentinelProperties.getPassword()) ? null : jedisSentinelProperties.getPassword()
        );
        Jedis client = sentinelPool.getResource();
        client.ping();
        client.close();
        return sentinelPool;
    }

    @Bean
    @Lazy
    public JedisCluster cluster() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(jedisClusterProperties.getMaxActive());
        jedisPoolConfig.setMaxIdle(jedisClusterProperties.getMaxIdle());
        jedisPoolConfig.setMinIdle(jedisClusterProperties.getMinIdle());
        jedisPoolConfig.setMaxWaitMillis(jedisClusterProperties.getMaxWaitMillis());
        String[] servers = jedisClusterProperties.getHosts().split(",");
        Set<HostAndPort> nodes = new HashSet<>();
        for (String server : servers) {
            int idx = server.lastIndexOf(':');
            String host = idx != -1 ? server.substring(0, idx) : server;
            String port = idx != -1 ? server.substring(idx + 1) : "";
            nodes.add(new HostAndPort(host, Integer.parseInt(port)));
        }
        JedisCluster clusterClient = new JedisCluster(
                nodes,
                this.jedisClusterProperties.getConnectionTimeout(),
                this.jedisClusterProperties.getTimeout(),
                this.jedisClusterProperties.getMaxAttempts(),
                StringUtils.isEmpty(jedisClusterProperties.getPassword()) ? null : jedisClusterProperties.getPassword(),
                jedisPoolConfig);
        Map<String, JedisPool> clusterNodes = clusterClient.getClusterNodes();
        for (Map.Entry<String, JedisPool> entry : clusterNodes.entrySet()) {
            JedisPool jedisPool = entry.getValue();
            Jedis client = jedisPool.getResource();
            client.ping();
            client.close();
        }
        return clusterClient;
    }

    @Bean
    @Lazy
    public ShardedJedis sharding() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(jedisShardingProperties.getMaxActive());
        jedisPoolConfig.setMaxIdle(jedisShardingProperties.getMaxIdle());
        jedisPoolConfig.setMinIdle(jedisShardingProperties.getMinIdle());
        jedisPoolConfig.setMaxWaitMillis(jedisShardingProperties.getMaxWaitMillis());
        String[] servers = jedisShardingProperties.getHosts().split(",");
        List<JedisShardInfo> shards = new ArrayList<>();
        for (String server : servers) {
            int idx = server.lastIndexOf(':');
            String host = idx != -1 ? server.substring(0, idx) : server;
            String port = idx != -1 ? server.substring(idx + 1) : "";
            shards.add(new JedisShardInfo(host, Integer.parseInt(port)));
        }
        ShardedJedis sharding = new ShardedJedis(shards);
        Collection<Jedis> clients = sharding.getAllShards();
        for (Jedis client : clients) {
            client.ping();
            client.close();
        }
        return sharding;
    }
}
