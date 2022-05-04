package com.example.gachonhub.redisTemplate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("local")
@SpringBootTest
class RedisCustomTemplateTest {

    @Autowired
    private RedisCustomTemplate redisCustomTemplate;

    @Autowired
    private LettuceConnectionFactory factory;

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Test
    void redisConnectionTest() {
        String hostName = factory.getHostName();
        int port = factory.getPort();

        assertThat(hostName).isEqualTo(redisHost);
        assertThat(port).isEqualTo(redisPort);
    }

    @Test
    void redisTest() {
        redisCustomTemplate.setRedisStringValue("test", "hello");
        String test = redisCustomTemplate.getRedisStringValue("test");
        redisCustomTemplate.deleteRedisStringValue("test");
        String test1 = redisCustomTemplate.getRedisStringValue("test");

        assertThat(test).isEqualTo("hello");
        assertThat(test1).isNull();
    }

}