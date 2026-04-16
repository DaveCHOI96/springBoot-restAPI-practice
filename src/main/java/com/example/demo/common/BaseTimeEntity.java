package com.example.demo.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass // 이 클래스를 상속받는 엔티티들이 아래 필드들을 컬럼으로 인식하게함
@EntityListeners(AuditingEntityListener.class) // 자동으로 시간을 넣어주는 기능 활성화
public class BaseTimeEntity {

    @CreatedDate // 데이터가 생성될 때 시간이 자동으로 저장됨
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate // 데이터가 수정될 때 시간이 자동 업데이트
    private LocalDateTime modifiedAt;
}
