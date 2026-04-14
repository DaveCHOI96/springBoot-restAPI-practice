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

    public List<MealResponse> registerAll(Long userId, List<MealRequest> requests) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다 "));
        List<Meal> meals = requests.stream()
                .map(request -> Meal.builder()
                        .calories(request.calories())
                        .foodName(request.foodName())
                        .user(user).build())
                .toList();
        List<Meal> savedMeals = mealRepository.saveAll(meals);
        return savedMeals.stream()
                .map(MealResponse::from)
                .toList();
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

    public DailyProgressResponse getDailyProgress(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));

        // 오늘 먹은 총 칼로리 불러오기
        Integer currentKcal = getTotalKcalToday(userId);

        // 목표 칼로리가 0 or null일 경우를 대비한 안전 로직
        if (user.getTargetKcal() == null || user.getTargetKcal() == 0) {
            return new DailyProgressResponse(user.getTargetKcal(), currentKcal, 0.0);
        }

        // 퍼센트 계산
        // (double)로 형변환을 해줘야 소수점 계산이 정확해요!
        // 계산 및 소수점 다듬기 (소수점 첫째 자리까지)
        double rawPercentage = (double) currentKcal / user.getTargetKcal()*100;
        double percentage = Math.round(rawPercentage * 10) / 10.0;
        return new DailyProgressResponse(user.getTargetKcal(), currentKcal, percentage);
    }

    public MealResponse updateMeal(Long userId, Long mealId, MealRequest request) {
        // 1. 수정할 식사 기록을 찾습니다.
        Meal meal = mealRepository.findById(mealId)
                .orElseThrow(() -> new IllegalArgumentException("해당 식사 기록을 찾을 수 없습니다."));
        // 2. [중요] 이 기록이 요청한 유저의 것이 맞는지 확인합니다. (권한 체크)
        if (!meal.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("해당 기록을 수정할 권한이 없습니다.");
        }

        // 3. 데이터 수정 (Entity update 메서드 활용)
        meal.update(request.foodName(), request.calories());

        // 4. 수정된 결과를 응답으로 변환하여 반환합니다.
        return MealResponse.from(meal);
    }

    public void deleteMeal(Long userId, Long mealId) {
        Meal meal = mealRepository.findById(mealId)
                .orElseThrow(() -> new IllegalArgumentException("해당 음식이 존재하지않습니다."));
        if (!meal.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }

        mealRepository.delete(meal);
    }
}
