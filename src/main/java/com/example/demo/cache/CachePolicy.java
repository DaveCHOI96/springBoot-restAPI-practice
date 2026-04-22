package com.example.demo.cache;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Duration;

@Getter
@RequiredArgsConstructor
public enum CachePolicy {

    TODAY_SUMMARY("todaySummary", Duration.ofMinutes(5), 30, 10),
    USER_PROFILE("userProfile", Duration.ofHours(24), 600, 30);

    private final String cacheName;
    private final Duration baseTtl;

    /** Jitter 최대 범위 (초). 실제 TTL = baseTtl + random(0 ~ jitterRange) */
    private final int jitterRange;

    /**
     * PER gap: 캐시 재계산에 예상되는 소요 시간(초).
     * 값이 클수록 만료 전 더 일찍 갱신을 시도합니다.
     */
    private final int perGap;

    public String buildKey(String subKey) {
        return cacheName + "::" + subKey;
    }
}