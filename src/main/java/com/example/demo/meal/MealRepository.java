package com.example.demo.meal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MealRepository extends JpaRepository<Meal, Long> {
    List<Meal> findByUserId(Long userId);

    List<Meal> findByUserIdAndCreatedAtBetween(Long userId, LocalDateTime start, LocalDateTime end);

    Page<Meal> findByUserIdAndCreatedAtBetween(Long userId, LocalDateTime start, LocalDateTime end, Pageable pageable);

    Page<Meal> findByUserIdIn(List<Long> userId, Pageable pageable);

}
