package com.example.demo.meal;

public record MealResponse(
        Long id,
        String foodName,
        Integer calories
) {
    public static MealResponse from(Meal meal) {
        return new MealResponse(meal.getId(), meal.getFoodName(), meal.getCalories());
    }
}
