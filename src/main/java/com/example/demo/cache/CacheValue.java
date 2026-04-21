package com.example.demo.cache;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CacheValue<T> implements Serializable {
    private T value; // 실제 저장할 데이터 (식단 정보 등)
    private long createdAt; // 생성 시간 (System.currentTimeMillis())

    // 편의를 위한 정적 메서드
    public static <T> CacheValue<T> of(T value) {
        return new CacheValue<>(value, System.currentTimeMillis());
    }
}
