package com.example.demo.user;

public record UnfollowResponse(
        Long followerId,
        Long followingId,
        String message
) {
}
