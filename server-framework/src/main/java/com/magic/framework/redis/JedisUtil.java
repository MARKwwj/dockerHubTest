package com.magic.framework.redis;

import cn.hutool.core.util.StrUtil;
import com.magic.framework.redis.config.JedisConfig;
import com.magic.framework.utils.SpringBeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;
import redis.clients.jedis.commands.JedisClusterCommands;
import redis.clients.jedis.commands.JedisCommands;
import redis.clients.jedis.params.ZIncrByParams;

import java.io.Closeable;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JedisUtil {
    private static final Logger logger = LoggerFactory.getLogger(JedisConfig.class);
    private static String useModel;
    private static boolean isCluster;

    public static void init(String model) {
        useModel = model;
        if (!StrUtil.isBlank(useModel)) {
            Object client = getClient();
            returnResource((Closeable) client);
        }
        logger.info("Jedis 初始化成功: " + model);
    }

    private static <T> T getClient() {
        Object client = null;
        switch (useModel) {
            case "standalone":
                client = SpringBeanUtil.getBean(JedisPool.class).getResource();
                break;
            case "sentinel":
                client = SpringBeanUtil.getBean(JedisSentinelPool.class).getResource();
                break;
            case "sharding":
                client = SpringBeanUtil.getBean(ShardedJedis.class);
                break;
            case "cluster":
                isCluster = true;
                client = SpringBeanUtil.getBean(JedisCluster.class);
                break;
            default:
                throw new RuntimeException("不支持的模式: " + useModel);
        }

        return (T) client;
    }

    public static boolean exist(String key) {
        Closeable closeable = null;
        try {
            if (isCluster) {
                JedisClusterCommands client = getClient();
                closeable = (Closeable) client;
                return client.exists(key);
            }
            JedisCommands client = getClient();
            closeable = (Closeable) client;
            return client.exists(key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            returnResource(closeable);
        }
    }


    public static Long del(String key) {
        Closeable closeable = null;
        try {
            if (isCluster) {
                JedisClusterCommands client = getClient();
                closeable = (Closeable) client;
                return client.del(key);
            }

            JedisCommands client = getClient();
            closeable = (Closeable) client;
            return client.del(key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            returnResource(closeable);
        }
    }

    public static boolean expire(String key, int second) {
        Closeable closeable = null;
        try {
            if (isCluster) {
                JedisClusterCommands client = getClient();
                closeable = (Closeable) client;
                return client.expire(key, second) == 1;
            }

            JedisCommands client = getClient();
            closeable = (Closeable) client;
            return client.expire(key, second) == 1;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            returnResource(closeable);
        }
    }


    public static Long expireNotExist(String key, int second) {
        Closeable closeable = null;
        try {
            if (isCluster) {
                JedisClusterCommands client = getClient();
                closeable = (Closeable) client;
                Long expireTime = client.ttl(key);
                if (expireTime > 0) {
                    return expireTime;
                }
                return client.expire(key, second);
            }
            JedisCommands client = getClient();
            closeable = (Closeable) client;
            Long expireTime = client.ttl(key);
            if (expireTime > 0) {
                return expireTime;
            }
            return client.expire(key, second);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            returnResource(closeable);
        }
    }


    public static String getStr(String key) {
        Closeable closeable = null;
        try {
            if (isCluster) {
                JedisClusterCommands client = getClient();
                closeable = (Closeable) client;
                return client.get(key);
            }

            JedisCommands client = getClient();
            closeable = (Closeable) client;
            return client.get(key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            returnResource(closeable);
        }
    }

    public static byte[] get(byte[] key) {
        Jedis client = null;
        try {
            client = getClient();
            return client.get(key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            returnResource(client);
        }
    }

    public static boolean set(byte[] key, byte[] value) {
        Jedis client = null;
        try {
            client = getClient();
            return "OK".equals(client.set(key, value));
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            returnResource(client);
        }
    }


    public static boolean setStr(String key, String value) {
        Closeable closeable = null;
        try {
            if (isCluster) {
                JedisClusterCommands client = getClient();
                closeable = (Closeable) client;
                return "OK".equals(client.set(key, value));
            }

            JedisCommands client = getClient();
            closeable = (Closeable) client;
            return "OK".equals(client.set(key, value));
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            returnResource(closeable);
        }
    }

    public static boolean setStr(String key, String value, int second) {
        Closeable closeable = null;
        try {
            if (isCluster) {
                JedisClusterCommands client = getClient();
                closeable = (Closeable) client;
                return "OK".equals(client.setex(key, second, value));
            }
            JedisCommands client = getClient();
            closeable = (Closeable) client;
            return "OK".equals(client.setex(key, second, value));
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            returnResource(closeable);
        }
    }

    public static boolean setStrEx(String key, String value, int second) {
        Closeable closeable = null;
        try {
            if (isCluster) {
                JedisClusterCommands client = getClient();
                closeable = (Closeable) client;
                return "OK".equals(client.setex(key, second, value));
            }

            JedisCommands client = getClient();
            closeable = (Closeable) client;
            return "OK".equals(client.setex(key, second, value));
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            returnResource(closeable);
        }
    }

    public static boolean hSet(String key, String filed, String value) {
        Closeable closeable = null;
        try {
            if (isCluster) {
                JedisClusterCommands client = getClient();
                closeable = (Closeable) client;
                client.hset(key, filed, value);
                return true;
            }

            JedisCommands client = getClient();
            closeable = (Closeable) client;
            client.hset(key, filed, value);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            returnResource(closeable);
        }
    }

    public static boolean hSetExpire(String key, Map<String, String> map, int second) {

        Closeable closeable = null;
        try {
            if (isCluster) {
                JedisClusterCommands client = getClient();
                closeable = (Closeable) client;
                client.hset(key, map);
                if (second > 0) {
                    return 1 == client.expire(key, second);
                }
                return true;
            }
            JedisCommands client = getClient();
            closeable = (Closeable) client;
            client.hset(key, map);
            if (second > 0) {
                return 1 == client.expire(key, second);
            }
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println(key);
            System.out.println(map);
            System.out.println(second);
            returnResource(closeable);
        }
    }

    public static boolean hSet(String key, Map<String, String> map) {
        Closeable closeable = null;
        try {
            if (isCluster) {
                JedisClusterCommands client = getClient();
                closeable = (Closeable) client;
                client.hset(key, map);
                return true;
            }
            JedisCommands client = getClient();
            closeable = (Closeable) client;
            client.hset(key, map);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            returnResource(closeable);
        }
    }

    public static Map<String, String> hGet(String key) {
        Closeable closeable = null;
        try {
            if (isCluster) {
                JedisClusterCommands client = getClient();
                closeable = (Closeable) client;
                return client.hgetAll(key);
            }

            JedisCommands client = getClient();
            closeable = (Closeable) client;
            return client.hgetAll(key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            returnResource(closeable);
        }
    }

    public static String hGet(String key, String field) {
        Closeable closeable = null;
        try {
            if (isCluster) {
                JedisClusterCommands client = getClient();
                closeable = (Closeable) client;
                return client.hget(key, field);
            }

            JedisCommands client = getClient();
            closeable = (Closeable) client;
            return client.hget(key, field);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            returnResource(closeable);
        }
    }

    public static Long hDel(String key, String... fields) {
        Closeable closeable = null;
        try {
            if (isCluster) {
                JedisClusterCommands client = getClient();
                closeable = (Closeable) client;
                return client.hdel(key, fields);
            }

            JedisCommands client = getClient();
            closeable = (Closeable) client;
            return client.hdel(key, fields);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            returnResource(closeable);
        }
    }

    public static String lIndex(String key, long index) {
        Closeable closeable = null;
        try {
            if (isCluster) {
                JedisClusterCommands client = getClient();
                closeable = (Closeable) client;
                return client.lindex(key, index);
            }

            JedisCommands client = getClient();
            closeable = (Closeable) client;
            return client.lindex(key, index);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            returnResource(closeable);
        }
    }

    public static List<String> lRange(String key) {
        Closeable closeable = null;
        try {
            if (isCluster) {
                JedisClusterCommands client = getClient();
                closeable = (Closeable) client;
                return client.lrange(key, 0, -1);
            }

            JedisCommands client = getClient();
            closeable = (Closeable) client;
            return client.lrange(key, 0, -1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            returnResource(closeable);
        }
    }

    public static List<String> lRange(String key, int start, int stop) {
        Closeable closeable = null;
        try {
            if (isCluster) {
                JedisClusterCommands client = getClient();
                closeable = (Closeable) client;
                return client.lrange(key, start, stop);
            }
            JedisCommands client = getClient();
            closeable = (Closeable) client;
            return client.lrange(key, start, stop);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            returnResource(closeable);
        }
    }

    public static Long lLen(String key) {
        Closeable closeable = null;
        try {
            if (isCluster) {
                JedisClusterCommands client = getClient();
                closeable = (Closeable) client;
                return client.llen(key);
            }
            JedisCommands client = getClient();
            closeable = (Closeable) client;
            return client.llen(key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            returnResource(closeable);
        }
    }


    public static boolean lPush(String key, String value) {
        Closeable closeable = null;
        try {
            if (isCluster) {
                JedisClusterCommands client = getClient();
                closeable = (Closeable) client;
                client.lpush(key, value);
                return true;
            }

            JedisCommands client = getClient();
            closeable = (Closeable) client;
            client.lpush(key, value);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            returnResource(closeable);
        }
    }

    public static boolean lPush(String key, List<String> values) {
        Closeable closeable = null;
        try {
            if (isCluster) {
                JedisClusterCommands client = getClient();
                closeable = (Closeable) client;
                for (String value : values) {
                    client.lpush(key, value);
                }
                return true;
            }

            JedisCommands client = getClient();
            closeable = (Closeable) client;
            for (String value : values) {
                client.lpush(key, value);
            }
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            returnResource(closeable);
        }
    }

    public static Long incr(String key) {
        Closeable closeable = null;
        try {
            if (isCluster) {
                JedisClusterCommands client = getClient();
                closeable = (Closeable) client;
                return client.incr(key);
            }

            JedisCommands client = getClient();
            closeable = (Closeable) client;
            return client.incr(key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            returnResource(closeable);
        }
    }

    public static Long incrBy(String key, long increment) {
        Closeable closeable = null;
        try {
            if (isCluster) {
                JedisClusterCommands client = getClient();
                closeable = (Closeable) client;
                return client.incrBy(key, increment);
            }

            JedisCommands client = getClient();
            closeable = (Closeable) client;
            return client.incrBy(key, increment);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            returnResource(closeable);
        }
    }

    public static Double zIncrBy(String key, double score, String value) {
        Closeable closeable = null;
        try {
            if (isCluster) {
                JedisClusterCommands client = getClient();
                closeable = (Closeable) client;
                return client.zincrby(key, score, value);
            }

            JedisCommands client = getClient();
            closeable = (Closeable) client;
            return client.zincrby(key, score, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            returnResource(closeable);
        }
    }

    public static Double zIncrByParams(String key, double score, String value, ZIncrByParams zIncrByParams) {
        Closeable closeable = null;
        try {
            if (isCluster) {
                JedisClusterCommands client = getClient();
                closeable = (Closeable) client;
                return client.zincrby(key, score, value, zIncrByParams);
            }

            JedisCommands client = getClient();
            closeable = (Closeable) client;
            return client.zincrby(key, score, value, zIncrByParams);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            returnResource(closeable);
        }
    }


    public static Set<String> zRevRange(String key, long start, long end) {
        Closeable closeable = null;
        try {
            if (isCluster) {
                JedisClusterCommands client = getClient();
                closeable = (Closeable) client;
                return client.zrevrange(key, start, end);
            }
            JedisCommands client = getClient();
            closeable = (Closeable) client;
            return client.zrevrange(key, start, end);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            returnResource(closeable);
        }
    }

    public static Set<Tuple> zRevRangeWithScores(String key, long start, long end) {
        Closeable closeable = null;
        try {
            if (isCluster) {
                JedisClusterCommands client = getClient();
                closeable = (Closeable) client;
                return client.zrevrangeWithScores(key, start, end);
            }
            JedisCommands client = getClient();
            closeable = (Closeable) client;
            return client.zrevrangeWithScores(key, start, end);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            returnResource(closeable);
        }
    }


    private static void returnResource(Closeable client) {
        if (client != null) {
            try {
                client.close();
            } catch (Exception e) {
            }
        }
    }
}
