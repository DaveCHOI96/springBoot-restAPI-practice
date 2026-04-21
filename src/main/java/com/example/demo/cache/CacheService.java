package com.example.demo.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class CacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final double beta = 1.0; // 가중치 (커질수록 더 일찍 갱신)
    private static final long gap = 10; // 예상 갱신 소요 시간(초)

    //T를 쓰면 하나의 메서드로 모든 타입을 다 소화할 수 있다
    //**T**는 자바의 **제네릭(Generic)**이라는 문법입니다. 쉽게 말하면 **"어떤 타입이
    //들어올지 아직 모르니, 나중에 쓸 때 결정하겠다"**라는 뜻의 타입 파라미터
    @SuppressWarnings("unchecked") // 경고 띄우지말란 어노테이션
    public <T> T getWithPER(String key, Supplier<T> dbLoader, Duration ttl) {
        // 1. 캐시 조회
        CacheValue<T> cached = (CacheValue<T>) redisTemplate.opsForValue().get(key);
        Long remainingTtl = redisTemplate.getExpire(key, TimeUnit.SECONDS);

        // 2. PER 조건 확인 : 남은 시간이 확률적 계산값보다
        if (cached == null || (remainingTtl - (gap * beta * Math.log(Math.random())) <= 0)) {
           System.out.println("--- [PER] 확률적 조기 갱신 혹은 신규 생성 시작! ---");

           // 3. DB에서 새 데이터 가져오기 (무거운 로직 실행)
            T newValue = dbLoader.get();

            // 4. Redis에 다시 저장
            CacheValue<T> wrapValue = CacheValue.of(newValue);
            redisTemplate.opsForValue().set(key, wrapValue, ttl);
            return newValue;
        }
        return cached.getValue();
    }
}
