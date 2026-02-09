package com.siddardha.redisDemonstration.Service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RedisLimiterService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final int LIMIT = 5;
    private static final int WINDOW_SECONDS = 60;

    public RedisLimiterService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean isAllowed(String userId) {
        String key = "rate:" +userId;
        Long count  = redisTemplate.opsForValue().increment(key);

        if(count ==1) {
            redisTemplate.expire(key, Duration.ofSeconds(WINDOW_SECONDS));
        }
        return count <= LIMIT;
    }

    public Long getRemainingTtl(String userId) {
        return redisTemplate.getExpire("rate:" + userId);
    }
}
