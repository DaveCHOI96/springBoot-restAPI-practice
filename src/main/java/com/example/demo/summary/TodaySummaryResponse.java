package com.example.demo.summary;

import com.example.demo.meal.MealSummary;
import com.example.demo.workout.WorkoutSummary;

public record TodaySummaryResponse(
        String userName,
        WorkoutSummary workout,
        MealSummary meal
) {
}
