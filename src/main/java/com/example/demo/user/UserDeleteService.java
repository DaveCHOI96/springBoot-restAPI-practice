package com.example.demo.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDeleteService {

    private final UserRepository userRepository;

    @Transactional
    public void executeCleanup(LocalDateTime threshold) {
        List<User> targets = userRepository.findAllByIsDeletedTrueAndDeletedAtBefore(threshold);
        if (!targets.isEmpty()) {
            List<Long> targetIds = targets.stream().map(User::getId).toList();
            userRepository.deleteAllByIdIn(targetIds);
        }
    }
}
