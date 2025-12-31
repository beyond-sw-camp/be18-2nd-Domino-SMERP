package com.domino.smerp.logging.dto.response;

import com.domino.smerp.logging.domain.ErrorLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ErrorLogResponse {

    private final String id;
    private final String timestamp;
    private final String method;
    private final String uri;
    private final int status;
    private final String principal;
    private final String clientIp;
    private final String exceptionClass;
    private final String exceptionMessage;

    public static ErrorLogResponse from(ErrorLog log) {
        return ErrorLogResponse.builder()
            .id(log.getId())
            .timestamp(log.getTimestamp().toString())
            .method(log.getMethod())
            .uri(log.getUri())
            .status(log.getStatus())
            .principal(log.getPrincipal())
            .clientIp(maskIp(log.getClientIp()))
            .exceptionClass(log.getExceptionClass())
            .exceptionMessage(log.getExceptionMessage())
            .build();
    }

    private static String maskIp(String ip) {
        if (ip == null) return null;
        return ip.replaceAll("\\d+$", "***");
    }
}