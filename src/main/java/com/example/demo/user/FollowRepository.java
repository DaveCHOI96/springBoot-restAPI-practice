package com.example.demo.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    // 이미 팔로우한 상태인지 확인
    boolean existsByFollowerAndFollowing(User follower, User following);

    // 언팔로우를 위해 특정 관계 찾기
    Optional<Follow> findByFollowerAndFollowing(User follower, User following);

    // 내가 팔로우하는 사람들 ID 리스트 가져오기
    List<Follow> findByFollowerId(Long followerId);
}
