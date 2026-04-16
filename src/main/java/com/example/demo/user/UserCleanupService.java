package com.example.demo.user;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserCleanupService {

    private final UserRepository userRepository;

    // 매일 새벽 3시에 실행(크론 표현식)
    @Scheduled(cron = "0 0 3 * * *")
    @Transactional
    public void cleanupDeletedUsers() {
        LocalDateTime threshold = LocalDateTime.now().minusMonths(3);

        // 1. 삭제할 대상자들을 먼저 조회 (로그를 남기거나 다른 처리를 위해 필요)
        List<User> targets = userRepository.findAllByIsDeletedTrueAndDeletedAtBefore(threshold);

        if (!targets.isEmpty()) {
            List<Long> targetIds = targets.stream().map(User::getId).toList();

            // 2. 수집된 ID들로 한 번에 영구 삭제 (Hard Delete)
            // 이때 User 엔티티에 설정된 CascadeType.REMOVE가 작동하여 자식 데이터들도 삭제됩니다.
            userRepository.deleteAllByIdIn(targetIds);

            System.out.println(targetIds.size() + "명의 유저 데이터가 영구 삭제되었습니다.");
        }
    }
}
