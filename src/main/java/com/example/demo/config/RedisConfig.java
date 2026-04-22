package com.example.demo.config;

import com.example.demo.cache.CachePolicy;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class RedisConfig {

    /**
     * CacheService 전용 StringRedisTemplate
     *
     * CacheService가 직렬화/역직렬화를 ObjectMapper로 직접 수행하므로
     * RedisTemplate은 단순 String 저장만 담당합니다.
     * → GenericJackson2JsonRedisSerializer의 제네릭 타입 소거 문제 원천 차단
     */
    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }

    /**
     * 공용 ObjectMapper
     * - JavaTimeModule: LocalDate, LocalDateTime 직렬화 지원
     * - WRITE_DATES_AS_TIMESTAMPS 비활성화: ISO-8601 문자열로 저장
     */
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
}