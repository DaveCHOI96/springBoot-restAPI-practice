package com.example.demo.workout;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WorkoutRepository extends JpaRepository<Workout, Long> {
    List<Workout> findByUserId(Long userId);

    @Query("select w from Workout w where w.id = :id")
    Optional<Workout> findActiveWorkoutById(@Param("id")Long id);
}
