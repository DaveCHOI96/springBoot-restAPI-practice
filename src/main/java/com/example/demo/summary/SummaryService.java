package com.example.demo.summary;

import com.example.demo.cache.CachePolicy;
import com.example.demo.cache.CacheService;
import com.example.demo.meal.MealService;
import com.example.demo.meal.MealSummary;
import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import com.example.demo.workout.WorkoutService;
import com.example.demo.workout.WorkoutSummary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SummaryService {

    private final UserRepository userRepository;
    private final WorkoutService workoutService;
    private final MealService mealService;
    private final CacheService cacheService;

    @Transactional(readOnly = true)
    public TodaySummaryResponse getTodaySummary(Long userId) {
        return cacheService.getWithPER(
                CachePolicy.TODAY_SUMMARY,
                userId.toString(),
                () -> loadFromDb(userId),
                TodaySummaryResponse.class  // ← 타입 명시로 역직렬화 100% 안전
        );
    }

    /**
     * 데이터 변경(운동/식단 기록) 시 캐시 무효화
     * ex. 운동 기록 저장 후 호출
     */
    public void evictTodaySummaryCache(Long userId) {
        cacheService.evict(CachePolicy.TODAY_SUMMARY, userId.toString());
    }

    // ── private ──────────────────────────────────────────────────────────────

    private TodaySummaryResponse loadFromDb(Long userId) {
        log.info("[DB] 대시보드 데이터 조회. userId={}", userId);

        User user = userRepository.findActiveUserById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다. id=" + userId));

        WorkoutSummary workout = getWorkoutSummaryWithFallback(user);
        MealSummary meal = getMealSummaryWithFallback(user);

        return new TodaySummaryResponse(user.getName(), workout, meal);
    }

    private WorkoutSummary getWorkoutSummaryWithFallback(User user) {
        try {
            return workoutService.getWorkoutSummary(user.getId());
        } catch (Exception e) {
            log.error("[Fallback] 운동 서비스 조회 실패. userId={}", user.getId(), e);
            return new WorkoutSummary(0, user.getTargetWorkoutDuration(), 0.0);
        }
    }

    private MealSummary getMealSummaryWithFallback(User user) {
        try {
            return mealService.getMealSummary(user.getId());
        } catch (Exception e) {
            log.error("[Fallback] 식단 서비스 조회 실패. userId={}", user.getId(), e);
            return new MealSummary(user.getTargetKcal(), 0, 0.0);
        }
    }
}
