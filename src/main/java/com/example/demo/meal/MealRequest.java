package com.example.demo.meal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record MealRequest(
        @NotBlank
        String foodName,
        @Positive(message = "칼로리는 0이상이여야 합니다.")
        Integer calories
) {
}
