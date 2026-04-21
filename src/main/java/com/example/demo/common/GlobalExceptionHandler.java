package com.example.demo.common;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllException(Exception ex) {
        //개발 단계에서는 로그를 남겨서 원인 파악
        log.error("예상치 못한 서버 에러 발생: ", ex);

        ErrorResponse response = ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("INTERNAL_SERVER_ERROR")
                .message("서버 내부 오류가 발생했습니다. 잠시 후 다시 시도해주세요.")
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(EntityNotFoundException ex) {
        ErrorResponse response = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .error("NOT_FOUND")
                .message(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }



    // @Valid 검증 오류 처리
    @ExceptionHandler(MethodArgumentNotValidException.class) // 검증 오류(@Valid)가 났을 때 실행
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        // 에러 메시지 문자열(String)을 추출합니다.
        String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();

        ErrorResponse response = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error("VALIDATION_ERROR") //에러타입 구분
                .message(errorMessage)
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    // 잘못된 인자(비즈니스 로직 예외) 처리
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorResponse response = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error("BAD_REQUEST")
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
