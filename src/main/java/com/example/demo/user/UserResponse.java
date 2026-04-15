package com.example.demo.user;

public record UserResponse(
        Long id,
        String name,
        String email,
        Integer age,
        String phoneNumber
) {
    // 엔티티를 영수증으로 변환해주는 변환기
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAge(),
                user.getPhoneNumber()
        );
    }

}
