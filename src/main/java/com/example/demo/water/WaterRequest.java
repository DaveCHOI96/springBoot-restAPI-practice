package com.example.demo.water;

import jakarta.validation.constraints.Positive;

public record WaterRequest(
        @Positive(message = "물 섭취량은 0보다 커야 합니다.") //양수만 가능
        Integer amount
) {
}
