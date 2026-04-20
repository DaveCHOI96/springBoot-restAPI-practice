package com.example.demo.common;

import lombok.Builder;
import lombok.Getter;


@Builder
public record ErrorResponse(
        //불변성 때문에 class 보다는 record를 사용하는게 더 효과적
        int status,
        String error,
        String message
) {
}
