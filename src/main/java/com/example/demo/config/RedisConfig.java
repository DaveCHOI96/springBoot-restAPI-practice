package com.example.demo.config;

import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.Random;

@Configuration
public class RedisConfig {

    private final Random random = new Random();

    // 기본 캐시 설정(전역 설정)
    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                //.plusSeconds(random.nextInt(61) Jitter방식 Cache Stampede 해결 전략 사용
                .entryTtl(Duration.ofMinutes(10).plusSeconds(random.nextInt(61))) // 기본값은 10분으로 설정
                .disableCachingNullValues()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
    }

    // 캐시 이름별로 설정을 커스터마이징 (실무에서 중요)
    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        return (builder) -> builder
                // 오늘 요약 정보는 데이터가 자주 바뀌므로 5분 유지
                .withCacheConfiguration("todaySummary",
                        RedisCacheConfiguration.defaultCacheConfig()
                                .entryTtl(Duration.ofMinutes(5).plusSeconds(random.nextInt(31)))
                                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer())))

                // 유저 프로필 같은 정보는 자주 안 바뀌므로 24시간 유지
                .withCacheConfiguration("userProfile",
                        RedisCacheConfiguration.defaultCacheConfig()
                                .entryTtl(Duration.ofHours(24).plusSeconds(random.nextInt(601)))
                                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer())));
    }
}
