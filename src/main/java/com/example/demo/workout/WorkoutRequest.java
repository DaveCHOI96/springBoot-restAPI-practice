package com.example.demo.workout;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record WorkoutRequest(
        @NotBlank(message = "운동 제목은 필수입니다.")
        @Size(max = 50, message = "제목은 50자를 넘을 수 없습니다.")
        String title,

        @Positive(message = "운동 시간은 0보다 커야 합니다.")
        Integer duration
) {
}
