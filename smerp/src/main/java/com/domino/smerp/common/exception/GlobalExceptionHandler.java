package com.domino.smerp.common.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
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

    // ✅ Enum 변환 실패 (예: 잘못된 주문 상태값)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        if (ex.getCause() instanceof InvalidFormatException cause) {
            if (cause.getTargetType().isEnum()) {
                log.warn("Enum 변환 실패: {}", cause.getValue());
                return new ResponseEntity<>(
                        new ErrorResponse(ErrorCode.INVALID_ORDER_STATUS.getCode(),
                                ErrorCode.INVALID_ORDER_STATUS.getMessage(),
                                ErrorCode.INVALID_ORDER_STATUS.getStatus().value()),
                        ErrorCode.INVALID_ORDER_STATUS.getStatus()
                );
            }
        }

        // Enum 관련이 아니면 기존 로직처럼 InternalServerError로 처리
        return ResponseEntity.internalServerError()
                .body(new ErrorResponse("INTERNAL_SERVER_ERROR", ex.getMessage(),
                        HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }


}
