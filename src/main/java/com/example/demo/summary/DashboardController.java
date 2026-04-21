package com.example.demo.summary;

import com.example.demo.meal.MealService;
import com.example.demo.meal.MealSummary;
import com.example.demo.workout.WorkoutService;
import com.example.demo.workout.WorkoutSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DashboardController {

    private final SummaryService summaryService;
    private final WorkoutService workoutService;
    private final MealService mealService;

    @GetMapping("/api/summary/{userId}")
    public ResponseEntity<TodaySummaryResponse> getFullSummary(@PathVariable Long userId) {
        TodaySummaryResponse response = summaryService.getTodaySummary(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/meals/summary/{userId}")
    public ResponseEntity<MealSummary> getMealSummary(@PathVariable Long userId) {
        MealSummary response = mealService.getMealSummary(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/workouts/summary/{userId}")
    public ResponseEntity<WorkoutSummary> getWorkoutSummary(@PathVariable Long userId) {
        WorkoutSummary response = workoutService.getWorkoutSummary(userId);
        return ResponseEntity.ok(response);
    }

}
