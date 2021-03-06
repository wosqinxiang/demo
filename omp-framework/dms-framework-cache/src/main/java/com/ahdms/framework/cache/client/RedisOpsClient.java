package com.ahdms.framework.cache.client;

import com.ahdms.framework.core.commom.util.*;
import com.ahdms.framework.core.exception.FrameworkException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author: Simms.shi
 * @Date: 2019/6/4 8:40
 * <pre>
 *  1、 client方法名与redis命令相匹配，redis命令列表参考 https://www.runoob.com/redis/redis-lists.html
 *  2、不同的redis数据类型，分别对应如下，可按需扩展，
 *     private ValueOperations<K, V> valueOps； string\bean类型（常用方法已实现）;
 *     private ListOperations<K, V> listOps； 满足基本的list结构，同时支持进出栈操作;
 *     private SetOperations<K, V>  setOps； string/bean类型（常用方法已实现）;
 *     private ZSetOperations<K, V> zSetOps； 有序列表结构;
 *     private HashOperations<K, HK, HV> hashOps； string为key,string/bean类型为value（常用方法已实现）;
 *  3、client统一使用了string泛型,bean转换是通过{@link com.ahdms.framework.core.commom.util.JsonUtils}完成转换;
 *  4、同一json文本可根据场景转换成不同的bean,适应自定义场景，避免ClassCastException
 *  5、入参校验,采用了快速失败方案
 *  </pre>
 */
@Slf4j
@Component
public class RedisOpsClient {

    /**
     * 获取指定 key 的值,转换成指定Bean
     */
    public <T> T get(String key, Class<T> clazz) {
        assertTrue(verifyKeys(key), "key is not exist");
        String json = opsForValue().get(key);
        return StringUtils.isEmpty(json) ? null : redisStrToValue(json, clazz);
    }

    /**
     * 获取指定 key 的值,转换成指定Bean
     */
    public <T> T get(String cacheName, String cacheKey, Class<T> clazz) {
        String fullKey = getFullKey(cacheName, cacheKey);
        return get(fullKey, clazz);
    }

    /**
     * 获取缓存，缓存不到时使用加载器加载
     *
     * @param key    缓存key
     * @param clazz  类型
     * @param loader 加载器
     */
    public <T> T get(String key, Class<T> clazz, Supplier<T> loader) {
        T value = this.get(key, clazz);
        if (value == null) {
            value = loader.get();
            if (value != null) {
                this.setnx(key, valueToRedisStr(value));
            }
        }
        return value;
    }

    /**
     * 获取缓存，缓存不到时使用加载器加载
     *
     * @param cacheName 缓存空间
     * @param cacheKey  缓存key
     * @param clazz     类型
     * @param loader    加载器
     */
    public <T> T get(String cacheName, String cacheKey, Class<T> clazz, Supplier<T> loader) {
        String fullKey = getFullKey(cacheName, cacheKey);
        return get(fullKey, clazz, loader);
    }

    /**
     * 获取缓存，缓存不到时使用加载器加载
     *
     * @param key     缓存key
     * @param seconds 缓存时间
     * @param clazz   类型
     * @param loader  加载器
     */
    public <T> T get(String key, long seconds, Class<T> clazz, Supplier<T> loader) {
        T value = this.get(key, clazz);
        if (value == null) {
            value = loader.get();
            if (value != null) {
                this.setnx(key, valueToRedisStr(value), seconds);
            }
        }
        return value;
    }

    /**
     * 获取缓存，缓存不到时使用加载器加载
     *
     * @param cacheName 缓存空间
     * @param cacheKey  缓存key
     * @param seconds   缓存时间
     * @param clazz     类型
     * @param loader    加载器
     */
    public <T> T get(String cacheName, String cacheKey, long seconds, Class<T> clazz, Supplier<T> loader) {
        String fullKey = getFullKey(cacheName, cacheKey);
        return get(fullKey, seconds, clazz, loader);
    }

    /**
     * 获取指定 key 的值。
     */
    public String getStr(String key) {
        assertTrue(verifyKeys(key), "key is not exist");
        return get(key, String.class);
    }

    /**
     * 获取指定 key 的值。
     *
     * @param cacheName 缓存空间
     * @param cacheKey  缓存key
     */
    public String getStr(String cacheName, String cacheKey) {
        String fullKey = getFullKey(cacheName, cacheKey);
        return getStr(fullKey);
    }

    /**
     * 获取指定 key 的值,转换成指定List<elementClazz>
     */
    public <T> List<T> getList(String key, Class<T> elementClazz) {
        assertTrue(verifyKeys(key), "key is not exist");
        String json = getStr(key);
        return StringUtils.isEmpty(json) ? null : JsonUtils.readList(json, elementClazz);
    }

    /**
     * 获取指定 key 的值,转换成指定List<elementClazz>
     */
    public <T> List<T> getList(String cacheName, String cacheKey, Class<T> elementClazz) {
        String fullKey = getFullKey(cacheName, cacheKey);
        return getList(fullKey, elementClazz);
    }

    /**
     * 获取缓存，缓存不到时使用加载器加载
     *
     * @param key          缓存key
     * @param elementClazz 子类型
     * @param loader       加载器
     */
    public <T> List<T> getList(String key, Class<T> elementClazz, Supplier<List<T>> loader) {
        List<T> value = this.getList(key, elementClazz);
        if (value == null) {
            value = loader.get();
            if (value != null) {
                this.setnx(key, valueToRedisStr(value));
            }
        }
        return value;
    }

    /**
     * 获取缓存，缓存不到时使用加载器加载
     *
     * @param cacheName    缓存空间
     * @param cacheKey     缓存key
     * @param elementClazz 子类型
     * @param loader       加载器
     */
    public <T> List<T> getList(String cacheName, String cacheKey, Class<T> elementClazz, Supplier<List<T>> loader) {
        String fullKey = getFullKey(cacheName, cacheKey);
        return getList(fullKey, elementClazz, loader);
    }

    /**
     * 获取缓存，缓存不到时使用加载器加载
     *
     * @param key          缓存key
     * @param seconds      缓存时间
     * @param elementClazz 子类型
     * @param loader       加载器
     */
    public <T> List<T> getList(String key, long seconds, Class<T> elementClazz, Supplier<List<T>> loader) {
        List<T> value = this.getList(key, elementClazz);
        if (value == null) {
            value = loader.get();
            if (value != null) {
                this.setnx(key, valueToRedisStr(value), seconds);
            }
        }
        return value;
    }

    /**
     * 获取缓存，缓存不到时使用加载器加载
     *
     * @param cacheName    缓存空间
     * @param cacheKey     缓存key
     * @param seconds      缓存时间
     * @param elementClazz 子类型
     * @param loader       加载器
     */
    public <T> List<T> getList(String cacheName, String cacheKey, long seconds, Class<T> elementClazz, Supplier<List<T>> loader) {
        String fullKey = getFullKey(cacheName, cacheKey);
        return getList(fullKey, seconds, elementClazz, loader);
    }

    /**
     * 将 key 中储存的数字值增一。(原子操作，适合分布式计数器业务)
     * 如果value不存在，则设置为0,在自增
     * 如果value不是数字类型，会报错
     */
    public long incr(String key) {
        assertTrue(verifyKeys(key), "key is not exist");
        return opsForValue().increment(key);
    }

    /**
     * 将 key 中储存的数字值增一。(原子操作，适合分布式计数器业务)
     * 如果value不存在，则设置为0,在自增
     * 如果value不是数字类型，会报错
     *
     * @param cacheName 缓存空间
     * @param cacheKey  缓存key
     */
    public long incr(String cacheName, String cacheKey) {
        String fullKey = getFullKey(cacheName, cacheKey);
        return this.incr(fullKey);
    }

    /**
     * 将 key 中储存的数字值增一。(原子操作，适合分布式计数器业务)
     * 如果value不存在，则设置为0,在自增
     * 如果value不是数字类型，会报错
     */
    public long incrBy(String key, long delta) {
        assertTrue(verifyKeys(key), "key is not exist");
        return opsForValue().increment(key, delta);
    }

    /**
     * 递增
     *
     * @param cacheName 缓存空间
     * @param cacheKey  缓存key
     */
    public long incrBy(String cacheName, String cacheKey, long delta) {
        String fullKey = getFullKey(cacheName, cacheKey);
        return this.incrBy(fullKey, delta);
    }

    /**
     * 为给定 key 设置过期时间，以秒计。
     */
    public void expire(String key, long seconds) {
        assertTrue(verifyKeys(key), "key is not exist");
        redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
    }

    /**
     * 为给定 key 设置过期时间，以秒计。
     */
    public void expire(String cacheName, String cacheKey, long seconds) {
        String fullKey = getFullKey(cacheName, cacheKey);
        this.expire(fullKey, seconds);
    }

    /**
     * @return key的剩余生存时间（-2：key不存在, -1: 未设置过期, 其他：剩余的秒）
     */
    public long ttl(String key) {
        if (StringUtils.isBlank(key)) return -2;
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * @return key的剩余生存时间（-2：key不存在, -1: 未设置过期, 其他：剩余的秒）
     */
    public long ttl(String cacheName, String cacheKey) {
        String fullKey = getFullKey(cacheName, cacheKey);
        return this.ttl(fullKey);
    }

    /*
     *将值 value 关联到 key ，并将 key 的过期时间设为 seconds (以秒为单位)。
     */
    public void setex(String key, Object obj, long seconds) {
        assertTrue(verifySimpleKeyValue(key, obj), "key & value is not exist");
        String value = valueToRedisStr(obj);
        opsForValue().set(key, value, seconds, TimeUnit.SECONDS);
    }

    /*
     *将值 value 关联到 key ，永久有效
     */
    public void setex(String key, Object obj) {
        assertTrue(verifySimpleKeyValue(key, obj), "key & value is not exist");
        String value = valueToRedisStr(obj);
        opsForValue().set(key, value);
    }

    /**
     * 只有在 key 不存在时设置 key 的值。有效期单位:秒
     */
    public boolean setnx(String key, Object obj, long seconds) {
        assertTrue(verifySimpleKeyValue(key, obj), "key & value is not exist");
        String value = valueToRedisStr(obj);
        return opsForValue().setIfAbsent(key, value, seconds, TimeUnit.SECONDS);
    }

    public boolean setnx(String key, Object obj) {
        assertTrue(verifySimpleKeyValue(key, obj), "key & value is not exist");
        String value = valueToRedisStr(obj);
        return opsForValue().setIfAbsent(key, value);
    }

    /**
     * 该命令用于在 key 存在时删除 key。
     *
     * @param cacheName 缓存空间
     * @param cacheKey  缓存key
     * @return Long
     */
    public Long del(String cacheName, String cacheKey) {
        String fullKey = getFullKey(cacheName, cacheKey);
        return del(fullKey);
    }

    /**
     * 该命令用于在 key 存在时删除 key。
     */
    public Long del(String... keys) {
        if (ObjectUtils.isEmpty(keys)) {
            return 0L;
        }
        return del0(Arrays.asList(keys));
    }

    /**
     * 该命令用于在 key 存在时删除 key。
     */
    public Long del0(String cacheName, String... keys) {
        if (ObjectUtils.isEmpty(keys)) {
            return 0L;
        }
        Set<String> keySet = Stream.of(keys)
                .map(key -> cacheName.concat(StringPool.COLON).concat(key))
                .collect(Collectors.toSet());
        return del0(keySet);
    }

    /**
     * 该命令用于在 key 存在时删除 key。
     */
    public Long del0(Collection<String> keys) {
        if (CollectionUtils.isEmpty(keys)) return 0L;
        return redisTemplate.delete(keys);
    }

    /**
     * 该命令用于在 key 存在时删除 key。
     */
    public Long del0(String cacheName, Collection<String> keys) {
        if (ObjectUtils.isEmpty(keys)) {
            return 0L;
        }
        Set<String> keySet = keys.stream()
                .map(key -> cacheName.concat(StringPool.COLON).concat(key))
                .collect(Collectors.toSet());
        return del0(keySet);
    }

    /**
     * 将哈希表 key 中的字段 field 的值设为 value 。
     */
    public void hset(String key, String field, Object obj) {
        assertTrue(verifyKeys(key, field) && verifyValues(obj), "key & value is not exist");
        String value = valueToRedisStr(obj);
        opsForHash().put(key, field, value);
    }

    /**
     * 只有在字段 field 不存在时，设置哈希表字段的值。
     */
    public void hsetnx(String key, String field, Object obj) {
        assertTrue(verifyKeys(key, field) && verifyValues(obj), "key & value is not exist");
        String value = valueToRedisStr(obj);
        opsForHash().putIfAbsent(key, field, value);
    }

    /**
     * 同时将多个 field-value (域-值)对设置到哈希表 key 中。
     */
    public void hmset(String key, Map<String, ?> objects) {
        assertTrue(verifyKeys(key) && !CollectionUtils.isEmpty(objects), "key & value is not exist");
        Map<String, String> values = new HashMap<>(objects.size(), 1);
        objects.forEach((k, v) -> {
            values.put(k, valueToRedisStr(v));
        });
        opsForHash().putAll(key, values);
    }

    /**
     * 获取存储在哈希表中指定字段的值,并转换成指定的Bean。
     */
    public <T> T hget(String key, String field, Class<T> clazz) {
        assertTrue(verifyKeys(key) && verifyKeys(field), "keys & fields is not exist");
        String value = opsForHash().get(key, field);
        return redisStrToValue(value, clazz);
    }

    /**
     * 获取存储在哈希表中指定字段的值,并转换成指定的Bean。
     */
    public <T> T hget(String key, String field, Class<T> clazz, Supplier<T> loader) {
        assertTrue(verifyKeys(key) && verifyKeys(field), "keys & fields is not exist");
        String json = opsForHash().get(key, field);
        if (StringUtils.isBlank(json)) {
            T value = loader.get();
            if (value != null) {
                this.hsetnx(key, field, value);
            }
            return value;
        }
        return redisStrToValue(json, clazz);
    }

    /**
     * 获取存储在哈希表中指定字段的值。
     */
    public String hgetStr(String key, String field) {
        return hget(key, field, String.class);
    }

    /**
     * hash 删除
     *
     * @param key   key
     * @param field field
     * @return Long
     */
    public Long hdel(String key, String field) {
        assertTrue(verifyKeys(key) && verifyKeys(field), "keys & fields is not exist");
        return opsForHash().delete(key, field);
    }

    /**
     * hash 删除
     *
     * @param key   key
     * @param field field
     * @return Long
     */
    public Long hdel0(String key, Object... field) {
        assertTrue(verifyKeys(key) && verifyValues(field), "keys & fields is not exist");
        return opsForHash().delete(key, field);
    }

    /**
     * 获取所有给定字段的值,将item转成指定的Bean
     */
    public <T> Map<String, T> hmget(String key, Class<T> clazz, String... fields) {
        assertTrue(verifyKeys(key) && verifyKeys(fields), "keys & fields is not exist");

        List<String> values = opsForHash().multiGet(key, Arrays.asList(fields));
        Map<String, T> result = new HashMap<>();
        for (int i = 0; i < fields.length; i++) {
            if (StringUtils.isNotBlank(values.get(i))) {
                result.put(fields[i], redisStrToValue(values.get(i), clazz));
            }
        }
        return result;
    }

    /**
     * 获取所有给定字段的值,将item转成指定的Bean
     */
    public <T> Map<String, T> hmget(String key, Class<T> clazz, Supplier<Map<String, T>> loader, String... fields) {
        assertTrue(verifyKeys(key) && verifyKeys(fields), "keys & fields is not exist");
        List<String> values = opsForHash().multiGet(key, Arrays.asList(fields));
        // 如果为空利用加载器加载
        if (StringUtils.isAnyNotBlank(values)) {
            Map<String, T> result = new HashMap<>();
            for (int i = 0; i < fields.length; i++) {
                String value = values.get(i);
                if (StringUtils.isNotBlank(value)) {
                    result.put(fields[i], redisStrToValue(value, clazz));
                }
            }
            return result;
        }
        Map<String, T> result = loader.get();
        if (result == null || result.isEmpty()) {
            return Collections.emptyMap();
        }
        this.hmset(key, result);
        return result;
    }

    /**
     * 获取所有给定字段的值
     */
    public Map<String, String> hmgetStr(String key, String... fields) {
        return hmget(key, String.class, fields);
    }

    /**
     * 查看哈希表 key 中，指定的字段是否存在。
     */
    public Boolean hexists(String key, String field) {
        assertTrue(verifyKeys(key, field), "keys & fields is not exist");
        return opsForHash().hasKey(key, field);
    }

    /**
     * 向集合添加一个或多个成员
     * 如果是bean对象，需要确保add和rem时刻属性值是"相等"的
     */
    public Long sadd(String key, Object... values) {
        assertTrue(verifyKeys(key) && verifyValues(values), "keys & fields is not exist");
        return opsForSet().add(key, Stream.of(values).map(RedisOpsClient::valueToRedisStr).toArray(String[]::new));
    }

    /**
     * 移除集合中一个或多个成员,按对象
     * 如果是bean对象，需要确保add和rem时刻属性值是"相等"的
     */
    public Long srem(String key, Object... values) {
        assertTrue(verifyKeys(key) && verifyValues(values), "keys & fields is not exist");
        return opsForSet().remove(key, Stream.of(values).map(RedisOpsClient::valueToRedisStr).toArray());
    }

    /**
     * 判断 member 元素是否是集合 key 的成员
     */
    public Boolean sismember(String key, Object value) {
        assertTrue(verifySimpleKeyValue(key, value), "keys & fields is not exist");
        return opsForSet().isMember(key, valueToRedisStr(value));
    }


    /**
     * 返回集合中的所有成员,字符串
     */
    public Set<String> smembersStr(String key) {
        return smembers(key, String.class);
    }

    /**
     * 返回集合中的所有成员,元素按对象
     */
    public <T> Set<T> smembers(String key, Class<T> elementClazz) {
        assertTrue(verifyKeys(key) && elementClazz != null, "keys && elementClazz is not exist");
        Set<String> members = opsForSet().members(key);
        if (!CollectionUtils.isEmpty(members)) {
            return members.stream().map(v -> redisStrToValue(v, elementClazz)).collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }


    //value 操作类
    public ValueOperations<String, String> opsForValue() {
        return redisTemplate.opsForValue();
    }

    //hash 操作类
    public HashOperations<String, String, String> opsForHash() {
        return redisTemplate.opsForHash();
    }

    //set 操作类
    public SetOperations<String, String> opsForSet() {
        return redisTemplate.opsForSet();
    }

    //list 操作类
    public ListOperations<String, String> opsForList() {
        return redisTemplate.opsForList();
    }

    @Autowired
    public StringRedisTemplate redisTemplate;

    public String getFullKey(String cacheName, String cacheKey) {
        assertTrue(verifyKeys(cacheName, cacheKey), "cacheName & cacheKey is empty.");
        return cacheName.concat(StringPool.COLON).concat(cacheKey);
    }

    private static <T> T redisStrToValue(String object, Class<T> clazz) {
        if (clazz == String.class) {
            return (T) object;
        } else {
            return JsonUtils.readValue(object, clazz);
        }
    }

    private static String valueToRedisStr(Object obj) {
        if (obj == null) {
            return null;
        } else if (obj instanceof String) {
            return (String) obj;//
        } else {
            return JsonUtils.toString(obj);
        }
    }

    private static boolean verifyKeys(String... keys) {
        return !CollectionUtils.isEmpty(keys) && !StringUtils.isAnyBlank(keys);
    }

    private static boolean verifyValues(Object... values) {
        return !CollectionUtils.isEmpty(values) && ObjectUtils.allNotNull(values);
    }

    private static boolean verifySimpleKeyValue(String key, Object value) {
        return verifyKeys(key) && verifyValues(value);
    }


    public static void assertTrue(boolean expression, String message) {
        if (!expression) {
            throw new FrameworkException(message);
        }
    }
}
