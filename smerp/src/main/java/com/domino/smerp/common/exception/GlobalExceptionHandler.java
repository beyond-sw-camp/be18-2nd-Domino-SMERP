package com.domino.smerp.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        log.info("[{}] {}", errorCode.getCode(), errorCode.getMessage());
        return new ResponseEntity<>(
            new ErrorResponse(errorCode.getCode(), errorCode.getMessage(),errorCode.getStatus().value()), errorCode.getStatus()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        return ResponseEntity.internalServerError()
                             .body(new ErrorResponse("INTERNAL_SERVER_ERROR", ex.getMessage(),
                                 HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return ResponseEntity.badRequest().body(new ErrorResponse("BAD_REQUEST", "유효성 검사 실패",HttpStatus.BAD_REQUEST.value()));
    }
}
