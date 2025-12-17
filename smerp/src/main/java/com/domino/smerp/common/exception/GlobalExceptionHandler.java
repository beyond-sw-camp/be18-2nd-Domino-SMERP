package com.domino.smerp.common.exception;

import com.domino.smerp.logging.domain.ErrorLog;
import com.domino.smerp.logging.repository.ErrorLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ErrorLogRepository errorLogRepository;

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(
        CustomException ex, HttpServletRequest request) {

        ErrorCode errorCode = ex.getErrorCode();

        ErrorLog errorLog = ErrorLog.builder()
            .method(request.getMethod())
            .uri(request.getRequestURI())
            .status(errorCode.getStatus().value())
            .clientIp(request.getRemoteAddr())
            .principal(getPrincipal())
            .exceptionClass(ex.getClass().getName())
            .exceptionMessage(ex.getMessage())
            .timestamp(LocalDateTime.now())
            .build();

        errorLogRepository.save(errorLog);

        return ResponseEntity
            .status(errorCode.getStatus())
            .body(new ErrorResponse(
                errorCode.getCode(),
                errorCode.getMessage(),
                errorCode.getStatus().value()
            ));
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(
        Exception ex, HttpServletRequest request) {

        ErrorLog errorLog = ErrorLog.builder()
            .method(request.getMethod())
            .uri(request.getRequestURI())
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .clientIp(request.getRemoteAddr())
            .principal(getPrincipal())
            .exceptionClass(ex.getClass().getName())
            .exceptionMessage(ex.getMessage())
            .timestamp(LocalDateTime.now())
            .build();

        errorLogRepository.save(errorLog);

        return ResponseEntity.internalServerError()
            .body(new ErrorResponse(
                "INTERNAL_SERVER_ERROR",
                ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value()
            ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return ResponseEntity.badRequest().body(new ErrorResponse("BAD_REQUEST", "유효성 검사 실패",HttpStatus.BAD_REQUEST.value()));
    }

    private String getPrincipal() {
        Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : "UNKNOWN";
    }
}
