package com.example.demo.user;

public record FollowResponse(
        Long followId,
        Long followerId,
        String followerName,
        Long followingId,
        String followingName
) {
    public static FollowResponse from(Follow follow) {
        return new FollowResponse(
                follow.getId(),
                follow.getFollower().getId(),
                follow.getFollower().getName(),
                follow.getFollowing().getId(),
                follow.getFollowing().getName()
        );
    }
}
