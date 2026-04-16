package com.example.demo.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FollowService {

    private FollowRepository followRepository;
    private UserRepository userRepository;

    public FollowResponse follow(Long followerId, Long followingId) {
        if (followerId.equals(followingId)) {
            throw new IllegalArgumentException("자기 자신을 팔로우 할 수 없습니다.");
        }

        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new IllegalArgumentException("팔로워가 존재하지 않습니다."));
        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new IllegalArgumentException("팔로윙 대상이 존재하지 않습니다."));

        if (followRepository.existsByFollowerAndFollowing(follower, following)) {
            throw new IllegalArgumentException("이미 팔로우 중인 유저입니다.");
        }

        Follow follow = Follow.builder()
                .follower(follower)
                .following(following)
                .build();

        // 편의 메서드를 통해 양방향 관계 설정
        // 이 과정에서 Follow 객체에 User가 세팅되고, User의 List에도 Follow가 추가됩니다.
        follow.setFollower(follower);
        follow.setFollowing(following);

        Follow savedFollow = followRepository.save(follow);
        return FollowResponse.from(savedFollow);
    }

    public UnfollowResponse unfollow(Long followerId, Long followingId) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new IllegalArgumentException("팔로워가 존재하지 않습니다."));
        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new IllegalArgumentException("팔로잉 대상이 존재하지 않습니다."));

        Follow follow = followRepository.findByFollowerAndFollowing(follower, following)
                .orElseThrow(() -> new IllegalArgumentException("팔로우 관계가 아닙니다."));

        followRepository.delete(follow);
        return new UnfollowResponse(followerId, followingId, "언팔로우 성공");
    }
}
