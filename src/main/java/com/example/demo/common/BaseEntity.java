package com.example.demo.common;


import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@FilterDef(name = "deletedFilter")
// 해당 어노테이션 restore 메서드 조회까지 isDeletede = true 유저 막아버려서 사용 불가능.
//@SQLRestriction("is_deleted = false") // 모든 SELECT 쿼리에 이 SQL 조건이 강제로 붙습니다.
public abstract class BaseEntity extends BaseTimeEntity {

    private boolean isDeleted = false;
    private LocalDateTime deletedAt;

    public void softDelete() {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
    }

    public void restore() {
        this.isDeleted = false;
        this.deletedAt = null;
    }
}
