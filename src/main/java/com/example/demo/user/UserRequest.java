package com.example.demo.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequest(
        String name,

        @Email(message = "올바르지않은 이메일 형식 입니다.")
        //문자열이 null인지 or "" 빈문자열인지, 공백만 있는지 검사
        //사용자가 email칸에 스페이스바 치고 가입하는거 입구컷!!
        @NotBlank(message = "이메일 입력은 필수 입니다.")
        String email,
        Integer age,
        String phoneNumber
) {
}
