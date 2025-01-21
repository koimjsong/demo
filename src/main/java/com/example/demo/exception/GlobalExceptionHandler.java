package com.example.demo.exception;

import com.example.demo.model.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessLogicException.class)
    public ResponseEntity<ApiResponse> handleBusinessLogicException(BusinessLogicException ex) {
        ExceptionCode code = ex.getExceptionCode();

        ApiResponse response = new ApiResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                code.getMessage(),
                code.getCode()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

/*    // 일반 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGenericException(Exception ex) {
        ApiResponse response = new ApiResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "알 수 없는 서버 오류가 발생했습니다.",
                "ERROR"
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }*/
}
