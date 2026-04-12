package com.example.demo.workout;

public record WorkoutResponse(
        Long id,
        String title,
        Integer duration
) {
    public static WorkoutResponse from(Workout workout) {
        return new WorkoutResponse(workout.getId(), workout.getTitle(), workout.getDuration());
    }
}
