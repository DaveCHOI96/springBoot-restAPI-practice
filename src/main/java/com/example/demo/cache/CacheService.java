package com.example.demo.cache;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Slf4j
@Component
@RequiredArgsConstructor
public class CacheService {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    private static final double BETA = 1.0;

    // Random → SecureRandom: 예측 불가능한 난수 생성 (노란줄 원인)
    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * PER(Probabilistic Early Recomputation) + Jitter 적용 캐시 조회
     *
     * @param policy    캐시 정책 (TTL, Jitter, PER gap 포함)
     * @param subKey    캐시 키 구분자 (ex. userId)
     * @param dbLoader  캐시 미스 시 실행할 DB 조회 로직
     * @param valueType 역직렬화 대상 클래스
     */
    public <T> T getWithPER(CachePolicy policy, String subKey, Supplier<T> dbLoader, Class<T> valueType) {
        JavaType javaType = objectMapper.getTypeFactory().constructType(valueType);
        return getWithPER(policy, subKey, dbLoader, javaType);
    }

    /**
     * List&lt;T&gt; 등 복잡한 제네릭 타입을 위한 overload
     *
     * <pre>{@code
     * JavaType type = objectMapper.getTypeFactory()
     *         .constructCollectionType(List.class, MyDto.class);
     * cacheService.getWithPER(policy, key, loader, type);
     * }</pre>
     */
    public <T> T getWithPER(CachePolicy policy, String subKey, Supplier<T> dbLoader, JavaType valueType) {
        String key = policy.buildKey(subKey);

        // 1. Redis 조회
        String cachedJson = redisTemplate.opsForValue().get(key);
        long remainingTtl = getRemainingTtl(key);

        // 2. PER 확률 계산 (캐시가 존재할 때만)
        boolean isProbabilisticExpired = cachedJson != null
                && remainingTtl > 0
                && shouldEarlyRecompute(policy, remainingTtl);

        // 3. 캐시 히트
        if (cachedJson != null && !isProbabilisticExpired) {
            return deserialize(cachedJson, valueType, key);
        }

        // 4. 캐시 미스 또는 PER 조기 갱신
        log.info("[Cache] DB 조회. key={}, reason={}", key,
                cachedJson == null ? "MISS" : "PER_EARLY_RECOMPUTE");

        T freshValue = dbLoader.get();
        store(key, freshValue, policy);
        return freshValue;
    }

    /**
     * 캐시 수동 삭제 (데이터 변경 시 호출)
     */
    public void evict(CachePolicy policy, String subKey) {
        String key = policy.buildKey(subKey);
        boolean deleted = Boolean.TRUE.equals(redisTemplate.delete(key)); // unboxing NPE 방지 (노란줄 원인)
        log.info("[Cache] Evict. key={}, deleted={}", key, deleted);
    }

    // ── private helpers ──────────────────────────────────────────────────────

    /**
     * PER 조기 갱신 여부 판단
     * 공식: remainingTtl <= perGap * beta * -log(rand)
     */
    private boolean shouldEarlyRecompute(CachePolicy policy, long remainingTtl) {
        double rand = Math.max(RANDOM.nextDouble(), 1e-10); // log(0) = -Infinity 방지
        double earlyRecomputeWindow = policy.getPerGap() * BETA * -Math.log(rand);

        if (remainingTtl <= earlyRecomputeWindow) {
            // {:.2f}는 SLF4J 미지원 포맷 → String.format으로 변환 (노란줄 원인)
            log.debug("[PER] 조기 갱신 트리거. remainingTtl={}s, window={}s",
                    remainingTtl, String.format("%.2f", earlyRecomputeWindow));
            return true;
        }
        return false;
    }

    private long getRemainingTtl(String key) {
        Long ttl = redisTemplate.getExpire(key, TimeUnit.SECONDS);
        return (ttl != null && ttl > 0) ? ttl : 0L;
    }

    private <T> void store(String key, T value, CachePolicy policy) {
        try {
            String json = objectMapper.writeValueAsString(value);
            long jitter = RANDOM.nextInt(policy.getJitterRange() + 1);
            Duration finalTtl = policy.getBaseTtl().plusSeconds(jitter);
            redisTemplate.opsForValue().set(key, json, finalTtl);
            log.debug("[Cache] 저장 완료. key={}, ttl={}s", key, finalTtl.getSeconds());
        } catch (Exception e) {
            log.error("[Cache] 직렬화 실패. key={}", key, e);
        }
    }

    private <T> T deserialize(String json, JavaType valueType, String key) {
        try {
            return objectMapper.readValue(json, valueType);
        } catch (Exception e) {
            log.warn("[Cache] 역직렬화 실패, 캐시 삭제. key={}", key, e);
            redisTemplate.delete(key);
            return null;
        }
    }
}