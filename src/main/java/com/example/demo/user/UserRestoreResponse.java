package com.example.demo.user;

public record UserRestoreResponse(
        Long id,
        boolean isDeleted,
        String message
) {
    public static UserRestoreResponse from(User user) {
        String statusMessage = user.isDeleted() ? "복구에 실패했습니다" : "복구에 성공했습니다";

        return new UserRestoreResponse(
                user.getId(),
                user.isDeleted(),
                statusMessage
        );
    }
}
