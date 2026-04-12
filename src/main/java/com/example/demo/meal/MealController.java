package com.example.demo.meal;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MealController {

    private final MealService mealService;
    private final MealRepository mealRepository;

    @PostMapping("/users/{userId}/meals")
    public ResponseEntity<MealResponse> addMeal(
            @PathVariable Long userId, @RequestBody MealRequest request) {
        //add는 DB데이터에만 저장하면되기 때문에 response를 사용하지않고 void(빈상자)를 사용
        MealResponse response = mealService.saveMeal(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/users/{userId}/meals")
    public ResponseEntity<List<MealResponse>> getMeal(@PathVariable Long userId) {
        List<MealResponse> responses = mealService.getMeals(userId);
        return ResponseEntity.ok(responses);
    }
}
