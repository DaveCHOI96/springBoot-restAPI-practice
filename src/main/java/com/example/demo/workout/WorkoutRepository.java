package com.example.demo.workout;

import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface WorkoutRepository extends JpaRepository<Workout, Long> {
    List<Workout> findByUserId(Long userId);

    @Query("select w from Workout w where w.id = :id")
    Optional<Workout> findActiveWorkoutById(@Param("id")Long id);

    //Meal과 같은 stream 방식 but 다량으로 등록시 네트워크 비용 + 메모리 사용이 높다.
//    List<Workout> findByUserIdAndCreatedAtBetween(Long userId, LocalDateTime start, LocalDateTime end);

    // Query 문을 만드는것이 메모리 사용량을 줄일 수 있음 (성능상 유리)
    @Query("select SUM(w.duration) from Workout w " +
            "where w.user.id = :userId " +
            "AND w.createdAt >= :startOfDay")
    Integer calculateTotalDurationToday(@Param("userId") Long userId,
                                        @Param("startOfDay")LocalDateTime startOfDay);
}
