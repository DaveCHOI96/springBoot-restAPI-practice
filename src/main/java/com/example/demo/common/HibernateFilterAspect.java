package com.example.demo.common;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class HibernateFilterAspect {

    private final EntityManager entityManager;

    // 모든 서비스 실행 전
    @Before("execution(* com.example.demo..*Service.*(..))")
    public void enableFilter() {
        Session session = entityManager.unwrap(Session.class);
        session.enableFilter("deletedFilter");
    }
}
