package com.example.demo.summary;

import com.example.demo.cache.CacheService;
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

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SummaryService {

    private final UserRepository userRepository;
    private final WorkoutService workoutService;
    private final MealService mealService;

//    private final CacheService cacheService;

    @Cacheable(value = "todaySummary", key = "#userId", cacheManager = "cacheManager")
    public TodaySummaryResponse getTodaySummary(Long userId) {
        System.out.println("--- [BD 호출] 대시보드 데이터를 계산합니다 ---"); // 캐시되면 이 로그가 안 찍힘
        User user = userRepository.findActiveUserById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        WorkoutSummary workout = getWorkoutSummaryWithFallback(user);
        MealSummary meal = getMealSummaryWithFallback(user);

        return new TodaySummaryResponse(user.getName(), workout, meal);
    }
// PER 알고리즘 방식 Redis
//    public TodaySummaryResponse getTodaySummary(Long userId) {
//        String cacheKey = "todaySummary::" + userId;
//
//        // @Cacheable 대신 cacheService.getWithPER 사용
//        return cacheService.getWithPER(
//                cacheKey,
//                () -> {
//                    // 이 람다 블록 안의 코드는 캐시가 없거나 갱신이 필요할 때만 실행됨
//                    log.info("--- [DB 호출] 대시보드 데이터를 계산합니다 (PER 적용) ---");
//
//                    User user = userRepository.findActiveUserById(userId)
//                            .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));
//
//                    WorkoutSummary workout = getWorkoutSummaryWithFallback(user);
//                    MealSummary meal = getMealSummaryWithFallback(user);
//
//                    return new TodaySummaryResponse(user.getName(), workout, meal);
//                },
//                Duration.ofMinutes(5) // TTL설정
//        );
//    }

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
