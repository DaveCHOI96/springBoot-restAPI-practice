package com.example.demo.meal;

import com.example.demo.user.User;


public record MealSummary(
        Integer todayTotalCalories,
        Integer targetCalories,
        Double achievementRate
) {
    public static MealSummary of(User user, Integer totalCalories) {
        double rate = (user.getTargetKcal() > 0)
                ? (double) totalCalories / user.getTargetKcal() * 100 : 0;
        return new MealSummary(
                totalCalories,
                user.getTargetKcal(),
                Math.round(rate * 10) / 10.0);
    }
}
