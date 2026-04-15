package com.example.demo.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/follows")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @PostMapping("/{followerId}/{followingId}")
    public ResponseEntity<FollowResponse> follow(
            @PathVariable Long followerId,
            @PathVariable Long followingId) {
        FollowResponse response = followService.follow(followerId, followingId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{followerId}/{followingId}")
    public ResponseEntity<UnfollowResponse> unfollow(
            @PathVariable Long followerId,
            @PathVariable Long followingId) {
        UnfollowResponse response = followService.unfollow(followerId, followingId);
        return ResponseEntity.ok(response);
    }
}
