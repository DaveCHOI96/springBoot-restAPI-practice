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

    // 단일 food insert
    @PostMapping("/users/{userId}/meal")
    public ResponseEntity<MealResponse> addMeal(
            @PathVariable Long userId, @RequestBody MealRequest request) {
        //add는 DB데이터에만 저장하면되기 때문에 response를 사용하지않고 void(빈상자)를 사용
        MealResponse response = mealService.saveMeal(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 다중 food insert (DB에는 각각 들어감)
    @PostMapping("/users/{userId}/meals")
    public ResponseEntity<List<MealResponse>> addAllMeal(
            @PathVariable Long userId, @RequestBody List<MealRequest> request) {
        List<MealResponse> response = mealService.registerAll(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/users/{userId}/meals")
    public ResponseEntity<List<MealResponse>> getMeal(@PathVariable Long userId) {
        List<MealResponse> responses = mealService.getMeals(userId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/users/{userId}/meals/today/total-kcal")
    public ResponseEntity<Integer> getTotalKcal(@PathVariable Long userId) {
        // 1. 서비스 호출해서 합계 가져오기 //합계를 가져오기에 Integer 반환
        Integer totalKcal = mealService.getTotalKcalToday(userId);
        return ResponseEntity.ok(totalKcal);
    }

    @GetMapping("/users/{userId}/meals/today/kcal-percent")
    public ResponseEntity<DailyProgressResponse> getKcalPercent(@PathVariable Long userId) {
        DailyProgressResponse response = mealService.getDailyProgress(userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/users/{userId}/meals/{mealId}")
    public ResponseEntity<MealResponse> updateMeal(
            //@PathVariable이 꼭 붙어야 주소창에서 ID를 읽어옴
            @PathVariable Long userId, @PathVariable Long mealId, @RequestBody  MealRequest request) {
        MealResponse response = mealService.updateMeal(userId, mealId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/users/{userId}/meals/{mealId}")
    public ResponseEntity<Void> deleteMeal(
            @PathVariable Long userId, @PathVariable Long mealId) {
        mealService.deleteMeal(userId, mealId);
        // 204 No Content 반환 (삭제 성공 시 관례)
        return ResponseEntity.noContent().build();
    }
}
