package com.example.demo.workout;

import com.example.demo.user.User;

public record WorkoutSummary(
        Integer todayTotalDuration,
        Integer targetDuration,
        Double achievementRate
) {
    public static WorkoutSummary of(User user, Integer totalDuration) {
        double rate = (user.getTargetWorkoutDuration() > 0)
            ? (double) totalDuration / user.getTargetWorkoutDuration() * 100 : 0;

        return new WorkoutSummary(
                totalDuration,
                user.getTargetWorkoutDuration(),
                Math.round(rate * 10) / 10.0 // 소수점 첫째자리까지 반올림
        );
    }
}
