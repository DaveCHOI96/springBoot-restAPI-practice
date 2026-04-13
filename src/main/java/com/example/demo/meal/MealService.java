package com.example.demo.meal;

import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MealService {

    private final MealRepository mealRepository;
    private final UserRepository userRepository;

    public MealResponse saveMeal(Long userId, MealRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
        Meal meal = Meal.builder()
                .foodName(request.foodName())
                .calories(request.calories())
                .user(user) // 유저 정보 연결
                .build();

        Meal savedMeal = mealRepository.save(meal);
        return MealResponse.from(savedMeal);
    }

    @Transactional(readOnly = true)
    public List<MealResponse> getMeals(Long userId) {
        List<Meal> meals = mealRepository.findByUserId(userId);

        return meals.stream()
                .map(MealResponse::from)
                .toList();
    }

    public Integer getTotalKcalToday(Long userId) {
        // 1. "오늘"의 시작 시간을 정의
        LocalDateTime start = LocalDate.now().atStartOfDay();
        // 2. "현재" 시간을 종료 시점으로 정의
        LocalDateTime end = LocalDateTime.now();

        List<Meal> meals = mealRepository.findByUserIdAndCreatedAtBetween(userId, start, end);

        //mapToInt를 사용해야 sum() 처리가 빠르다.
        return meals.stream()
                .mapToInt(Meal::getCalories).sum();
    }
}
