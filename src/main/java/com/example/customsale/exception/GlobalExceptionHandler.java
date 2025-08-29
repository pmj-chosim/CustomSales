package com.example.customsale.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 400, @Valid 검증 실패 시 발생하는 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        ErrorResponse errorResponse = new ErrorResponse("유효성 검사 실패", HttpStatus.BAD_REQUEST.value(), errors);
        return ResponseEntity.badRequest().body(errorResponse);
    }


    // 500 Internal Server Error: 그 외 모든 예외
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse("내부 서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }


    // 서비스 로직에서 발생하는 IllegalArgumentException(예: 중복 아이디) 처리
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), HttpStatus.CONFLICT.value(), null);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }
}

// 에러 응답에 사용될 DTO 클래스
@Getter
@Schema(description = "에러 응답 DTO")
class ErrorResponse {
    @Schema(description = "에러 메시지", example = "유효성 검사 실패")
    private final String message;

    @Schema(description = "HTTP 상태 코드", example = "400")
    private final int status;

    @Schema(description = "상세 에러 내용 (유효성 검사 시)", example = "{\"name\": \"아이디는 5자 이상 10자 이하여야 합니다.\"}")
    private final Map<String, String> errors;

    public ErrorResponse(String message, int status, Map<String, String> errors) {
        this.message = message;
        this.status = status;
        this.errors = errors;
    }
}
