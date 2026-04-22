package com.example.demo.summary;

import com.example.demo.meal.MealSummary;
import com.example.demo.workout.WorkoutSummary;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class TodaySummaryResponse{
        private String userName;
        private WorkoutSummary workout;
        private MealSummary meal;
}
