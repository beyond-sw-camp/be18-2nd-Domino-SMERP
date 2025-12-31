package com.domino.smerp.logging.dto.response;

import com.domino.smerp.logging.domain.ApiLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ApiLogResponse {

    private final String id;
    private final String timestamp;
    private final String method;
    private final String uri;
    private final int status;
    private final long duration;
    private final String principal;
    private final String clientIp;

    public static ApiLogResponse from(ApiLog log) {
        return ApiLogResponse.builder()
            .id(log.getId())
            .timestamp(log.getTimestamp().toString())
            .method(log.getMethod())
            .uri(log.getUri())
            .status(log.getStatus())
            .duration(log.getDuration())
            .principal(log.getPrincipal())
            .clientIp(maskIp(log.getClientIp()))
            .build();
    }

    private static String maskIp(String ip) {
        if (ip == null) return null;
        return ip.replaceAll("\\d+$", "***");
    }
}
