package com.example.demo.water;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WaterRepository extends JpaRepository<Water, Long> {
    List<Water> findByUserId(Long userId);
}
