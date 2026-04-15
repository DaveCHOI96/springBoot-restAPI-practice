package com.example.demo.meal;

public record DailyProgressResponse(
        Integer targetKcal,
        Integer currentKcal,
        Double percentage
) {
}
