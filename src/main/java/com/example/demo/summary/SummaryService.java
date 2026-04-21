package com.example.demo.summary;

import com.example.demo.meal.MealService;
import com.example.demo.meal.MealSummary;
import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import com.example.demo.workout.WorkoutService;
import com.example.demo.workout.WorkoutSummary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SummaryService {

    private final UserRepository userRepository;
    private final WorkoutService workoutService;
    private final MealService mealService;

    @Cacheable(value = "todaySummary", key = "#userId", cacheManager = "cacheManager")
    public TodaySummaryResponse getTodaySummary(Long userId) {
        System.out.println("--- [BD 호출] 대시보드 데이터를 계산합니다 ---"); // 캐시되면 이 로그가 안 찍힘
        User user = userRepository.findActiveUserById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        WorkoutSummary workout = getWorkoutSummaryWithFallback(user);
        MealSummary meal = getMealSummaryWithFallback(user);

        return new TodaySummaryResponse(user.getName(), workout, meal);
    }

    private WorkoutSummary getWorkoutSummaryWithFallback(User user) {
        try {
            return workoutService.getWorkoutSummary(user.getId());
        } catch (Exception e) {
            log.error("운동 서비스 조회 중 오류 발생 (User: {})", user.getId(), e);
            return new WorkoutSummary(0, user.getTargetWorkoutDuration(), 0.0);
        }
    }

    private MealSummary getMealSummaryWithFallback(User user) {
        try {
            return mealService.getMealSummary(user.getId());
        } catch (Exception e) {
            log.error("식단 서비스 조회 중 오류 발생 (User: {})", user.getId(), e);
            return new MealSummary(user.getTargetKcal(), 0, 0.0);
        }
    }
}
