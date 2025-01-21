package com.example.demo.Service.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RedisService {

    private final StringRedisTemplate stringRedisTemplate;

    public RedisService(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * Stores a key-value pair in Redis with an expiration time.
     *
     * @param key     The key to store in Redis.
     * @param value   The value to associate with the key.
     * @param timeout The expiration duration for the key-value pair.
     */
    public void setValues(String key, String value, Duration timeout) {
        try {
            stringRedisTemplate.opsForValue().set(key, value, timeout);
        } catch (Exception e) {
            throw new RuntimeException("Error setting value in Redis", e);
        }
    }

    /**
     * Retrieves a value from Redis by key.
     *
     *
     * @param key The key to look up in Redis.
     * @return The value associated with the key, or null if not found.
     */
    public String getValues(String key) {
        try {
            return stringRedisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving value from Redis", e);
        }
    }

    /**
     * Deletes a key-value pair from Redis.
     *
     * @param key The key to delete from Redis.
     */
    public void deleteValues(String key) {
        try {
            stringRedisTemplate.delete(key);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting value from Redis", e);
        }
    }

    public boolean checkExistsValue(String key) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(key));
    }
}
