package com.example.demo.meal;

import java.time.LocalDateTime;

public record MealResponse(
        Long id,
        String foodName,
        Integer calories,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {
    public static MealResponse from(Meal meal) {
        return new MealResponse(meal.getId(), meal.getFoodName(),
                meal.getCalories(), meal.getCreatedAt(), meal.getModifiedAt());
    }
}
