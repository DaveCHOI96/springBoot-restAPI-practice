package com.example.demo.common;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class) // 검증 오류(@Valid)가 났을 때 실행
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        // 1. 에러 메시지 문자열(String)을 추출합니다.
        String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        // 2. 추출한 문자열을 ErrorResponse 객체에 담아서 보냅니다.
        return ResponseEntity.badRequest().body(new ErrorResponse(errorMessage));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        String errorMessage = ex.getMessage();
        return ResponseEntity.badRequest().body(new ErrorResponse(errorMessage));
    }
}
