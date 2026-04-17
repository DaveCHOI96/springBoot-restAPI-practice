package com.example.demo.user;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class UserCleanupScheduler {

    private final UserDeleteService userDeleteService;

    // 트랜잭션 범위의 최소화: @Transactional이 메서드 전체에 걸려 있으면,
    // 대상 유저를 조회하고(findAll), ID 리스트를 만들고,
    // 로그를 찍는 모든 과정 동안 DB 커넥션을 점유

    // 매일 새벽 3시에 실행(크론 표현식)
    @Scheduled(cron = "0 0 3 * * *")
    public void cleanupScheduledTask() {
        LocalDateTime threshold = LocalDateTime.now().minusMonths(3);
        userDeleteService.executeCleanup(threshold);
    }
//    @Transactional
//    public void cleanupDeletedUsers() {
//        LocalDateTime threshold = LocalDateTime.now().minusMonths(3);
//
//        // 1. 삭제할 대상자들을 먼저 조회 (로그를 남기거나 다른 처리를 위해 필요)
//        List<User> targets = userRepository.findAllByIsDeletedTrueAndDeletedAtBefore(threshold);
//
//        if (!targets.isEmpty()) {
//            List<Long> targetIds = targets.stream().map(User::getId).toList();
//
//            // 2. 수집된 ID들로 한 번에 영구 삭제 (Hard Delete)
//            // 이때 User 엔티티에 설정된 CascadeType.REMOVE가 작동하여 자식 데이터들도 삭제됩니다.
//            userRepository.deleteAllByIdIn(targetIds);
//            log.info("{} 명의 유저 영구 삭제 완료", targetIds.size());
//        }
//    }
}
