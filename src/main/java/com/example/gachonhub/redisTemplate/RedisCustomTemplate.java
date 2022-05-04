package com.example.gachonhub.redisTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisCustomTemplate {

    private final StringRedisTemplate redisTemplate;

    public String getRedisStringValue(String key) {
        ValueOperations<String, String> stringValueOperations = redisTemplate.opsForValue();
        return stringValueOperations.get(key);
    }

    public String setRedisStringValue(String key, String value) {
        ValueOperations<String, String> stringValueOperations = redisTemplate.opsForValue();
        stringValueOperations.set(key, value);
        return stringValueOperations.get(key);
    }

    public void deleteRedisStringValue(String key) {
        redisTemplate.delete(key);
    }

    public String setRedisTokenFullValue(String key, String value, long timeout, TimeUnit unit) {
        ValueOperations<String, String> stringValueOperations = redisTemplate.opsForValue();
        stringValueOperations.set(key, value, timeout, unit);
        return stringValueOperations.get(key);
    }
}
