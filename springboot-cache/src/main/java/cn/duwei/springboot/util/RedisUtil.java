package cn.duwei.springboot.util;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;
@Component
@Slf4j
public class RedisUtil {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /** -------------------key相关操作--------------------- */

    public void pipeLine(Map<String, String> map){
        try {
            redisTemplate.executePipelined((RedisCallback<Long>) redisConnection -> {
                StringRedisConnection stringRedisConnection = (StringRedisConnection) redisConnection;
                stringRedisConnection.openPipeline();
                map.forEach((key, value) -> stringRedisConnection.set(key, value));
                stringRedisConnection.closePipeline();
                return null;
            });
        } catch (Exception e) {
            log.error("pipeline error,", e);
        }
    }

    /**
     * 批量操作，可设置过期时间
     * @param map 命令
     * @param seconds 过期时间
     */
    public void pipeLine(Map<String, String> map,long seconds){
        try {
            redisTemplate.executePipelined((RedisCallback<Long>) redisConnection -> {
                StringRedisConnection stringRedisConnection = (StringRedisConnection) redisConnection;
                stringRedisConnection.openPipeline();
                map.forEach((key, value) -> stringRedisConnection.set(key, value, Expiration.seconds(seconds), RedisStringCommands.SetOption.UPSERT));
                stringRedisConnection.closePipeline();
                return null;
            });
        } catch (Exception e) {
            log.error("pipeline error,", e);
        }
    }

    public void pipeLineDelete(List<String> commandList){
        try {
            redisTemplate.executePipelined((RedisCallback<Long>) redisConnection -> {
                StringRedisConnection stringRedisConnection = (StringRedisConnection) redisConnection;
                stringRedisConnection.openPipeline();
                commandList.forEach(command-> stringRedisConnection.del(command));
                stringRedisConnection.closePipeline();
                return null;
            });
        } catch (Exception e) {
            log.error("pipeline error,", e);
        }

    }
    /**
     * 删除key
     *
     * @param key
     */
    public void delete(String key) {
        redisTemplate.delete(key);

    }

    /**
     * 批量删除key
     *
     * @param keys
     */
    public void delete(Collection<String> keys) {
        redisTemplate.delete(keys);
    }

    /**
     * 序列化key
     *
     * @param key
     * @return
     */
    public byte[] dump(String key) {
        return redisTemplate.dump(key);
    }

    /**
     * 是否存在key
     *
     * @param key
     * @return
     */
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 设置过期时间
     *
     * @param key
     * @param timeout
     * @param unit
     * @return
     */
    public Boolean expire(String key, long timeout, TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 设置过期时间
     *
     * @param key
     * @param date
     * @return
     */
    public Boolean expireAt(String key, Date date) {
        return redisTemplate.expireAt(key, date);
    }

    /**
     * 查找匹配的key
     *
     * @param pattern
     * @return
     */
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * 将当前数据库的 key 移动到给定的数据库 db 当中
     *
     * @param key
     * @param dbIndex
     * @return
     */
    public Boolean move(String key, int dbIndex) {
        return redisTemplate.move(key, dbIndex);
    }

    /**
     * 移除 key 的过期时间，key 将持久保持
     *
     * @param key
     * @return
     */
    public Boolean persist(String key) {
        return redisTemplate.persist(key);
    }

    /**
     * 返回 key 的剩余的过期时间
     *
     * @param key
     * @param unit
     * @return
     */
    public Long getExpire(String key, TimeUnit unit) {
        return redisTemplate.getExpire(key, unit);
    }

    /**
     * 返回 key 的剩余的过期时间
     *
     * @param key
     * @return
     */
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key);
    }

    /**
     * 从当前数据库中随机返回一个 key
     *
     * @return
     */
    public String randomKey() {
        return redisTemplate.randomKey();
    }

    /**
     * 修改 key 的名称
     *
     * @param oldKey
     * @param newKey
     */
    public void rename(String oldKey, String newKey) {
        redisTemplate.rename(oldKey, newKey);
    }

    /**
     * 仅当 newkey 不存在时，将 oldKey 改名为 newkey
     *
     * @param oldKey
     * @param newKey
     * @return
     */
    public Boolean renameIfAbsent(String oldKey, String newKey) {
        return redisTemplate.renameIfAbsent(oldKey, newKey);
    }

    /**
     * 返回 key 所储存的值的类型
     *
     * @param key
     * @return
     */
    public DataType type(String key) {
        return redisTemplate.type(key);
    }

    /** -------------------string相关操作--------------------- */


    public void setKeyValue(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void setKeyValue(String key, Object value) {
        redisTemplate.opsForValue().set(key, JsonUtil.toJson(value));
    }

    public void setKeyValueExpire(String key, String value, long milliseconds) {
        redisTemplate.opsForValue().set(key, value, milliseconds, TimeUnit.MILLISECONDS);
    }

    public void setKeyValueExpire(String key, Object value, long milliseconds) {
        redisTemplate.opsForValue().set(key, JsonUtil.toJson(value), milliseconds, TimeUnit.MILLISECONDS);
    }

    public <T> T getValue(String key, Class<T> clazz) {
        String json = redisTemplate.opsForValue().get(key);
        return JsonUtil.toBean(json, clazz);
    }

    public <T> T getValue(String key, TypeReference<T> clazz) {
        String json = redisTemplate.opsForValue().get(key);
        return JsonUtil.toBean(json, clazz);
    }

    public String getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public Long increaseByKey(String key) {
        return redisTemplate.opsForValue().increment(key);
    }

    public boolean exist(String key) {
        return redisTemplate.hasKey(key);
    }

    public long listSize(String key) {
        return redisTemplate.opsForList().size(key);
    }

    public long listRightPushAll(String key, Collection<String> values){
        return redisTemplate.opsForList().rightPushAll(key, values);
    }

    public long listRightPushAll(String key, Set<String> values){
        return redisTemplate.opsForList().rightPushAll(key, values);
    }

    public List<String> listRange(String key, int start, int end){
        return redisTemplate.opsForList().range(key, start, end);
    }

    public void hashSet(String key, String hashKey, String value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    public void hashSet(String key, Map<String, String> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    public void hashSet(String key, String hashKey, Object value){
        redisTemplate.opsForHash().put(key, hashKey, JsonUtil.toJson(value));
    }

    public String hashGet(String key, String hashKey){
        return  (String) redisTemplate.opsForHash().get(key, hashKey);
    }

    public <T> T hashGet(String key, String hashKey, Class<T> clazz){
        String value = (String) redisTemplate.opsForHash().get(key, hashKey);
        return JsonUtil.toBean(value, clazz);
    }

    public long hashDelete(String key, String hashKey) {
        return redisTemplate.opsForHash().delete(key, hashKey);
    }

    public boolean hasHashKey(String key, String hashKey) {
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }


    // -------------------------zset(sortedSet)-----------------------------
    /**
     * <pre>
     * Add specified member with the specified score to the sorted set stored at key.
     * * If the key exists but does not hold a sorted set, null is returned.
     * Time complexity: O(log(N))
     * </pre>
     *
     * @param key
     * @param member
     * @param score
     * @return
     */
    public Boolean zsetAdd(String key, String member, double score) {
        Boolean result = false;
        try {
            result = redisTemplate.opsForZSet().add(key, member, score);
        } catch (Exception e) {
            result = null;
        }
        return result;
    }

    /**
     * <pre>
     * Returns the sorted set cardinality (number of elements) of the sorted set stored at key.
     * * If key does not exist,return 0。
     * * If the key exists but does not hold a sorted set, null is returned.
     * Time complexity: O(1)
     * </pre>
     *
     * @param key
     * @return
     */
    public Long zsetSize(String key) {
        Long result = null;
        try {
            result = redisTemplate.opsForZSet().size(key);
        } catch (Exception e) {
            result = null;
        }
        return result;
    }

    /**
     * <pre>
     * Returns the rank of member in the sorted set stored at key, with the
     * scores ordered from low to high. The rank (or index) is 0-based, which
     * means that the member with the lowest score has rank 0.
     * * If key does not exist,null is returned.
     * * If the key exists but does not hold a sorted set, null is returned.
     * Time complexity: O(log(N))
     * </pre>
     *
     * @param key
     * @param value
     * @return
     */
    public Long zsetRank(String key, String value) {
        Long result = null;
        try {
            result = redisTemplate.opsForZSet().rank(key, value);
        } catch (Exception e) {
            result = null;
        }
        return result;
    }

    public Set<String> hashKeys(String redisKey){
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        Set<String> keys = opsForHash.keys(redisKey);
        return  keys;
    }

    public List<String> hashValues(String redisKey){
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        List<String> values = opsForHash.values(redisKey);
        return values;
    }

    /**
     * zgu
     * @param script
     * @param params
     * @param args
     * @return
     */
    public Object executeLua(String script, List<String> params, List<String> args) throws Exception{
        try {
            Object result = redisTemplate.execute(RedisScript.of(script, String.class), params, args.toString());
            return result;
        } catch (Exception e) {
            log.error("redis execute lua script exception, script:" +
                    script + "params:" + params + "args:" + args);
            throw e;
        }
    }
}
