package com.example.demo.workout;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class WorkoutController {

    private final WorkoutRepository workoutRepository;
    private final WorkoutService workoutService;

    @PostMapping("/users/{userId}/workouts")
    public ResponseEntity<WorkoutResponse> addWorkout(
            @PathVariable Long userId, @RequestBody WorkoutRequest request) {
        WorkoutResponse response = workoutService.savaWorkout(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/users/{userId}/workouts")
    //@PathVariable: "저 위 주소창에 있는 {userId} 자리에 적힌 숫자를 쏙 빼서 이 변수에 담아줘!"라는 뜻입니다.
    public ResponseEntity<List<WorkoutResponse>> getWorkout(@PathVariable Long userId) {
        //workoutService에 userId에 대한 getWorkouts 메서드를 실행시켜, 나온 정보를 response에 담아
        List<WorkoutResponse> responses = workoutService.getWorkouts(userId);
        return ResponseEntity.ok(responses);
    }
}
