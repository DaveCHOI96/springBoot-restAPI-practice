package com.example.demo.meal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MealRepository extends JpaRepository<Meal, Long> {
    List<Meal> findByUserId(Long userId);

    List<Meal> findByUserIdAndCreatedAtBetween(Long userId, LocalDateTime start, LocalDateTime end);

    Page<Meal> findByUserIdAndCreatedAtBetween(Long userId, LocalDateTime start, LocalDateTime end, Pageable pageable);

    Page<Meal> findByUserIdIn(List<Long> userId, Pageable pageable);

    @Query("select m from Meal m where m.id = :id")
    Optional<Meal> findActiveMealById(@Param("id") Long id);


}
