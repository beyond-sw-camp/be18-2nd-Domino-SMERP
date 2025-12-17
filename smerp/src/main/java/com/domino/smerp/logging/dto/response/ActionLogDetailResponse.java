package com.domino.smerp.logging.dto.response;

import com.domino.smerp.logging.domain.ActionLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ActionLogDetailResponse {

    private final String id;
    private final String timestamp;

    private final String action;
    private final String entity;
    private final String entityId;

    private final boolean success;
    private final String actor;
    private final String clientIp;

    private final String failReason;

    private final String beforeData;
    private final String afterData;

    public static ActionLogDetailResponse from(ActionLog log) {
        return ActionLogDetailResponse.builder()
            .id(log.getId())
            .timestamp(log.getTimestamp().toString())
            .action(log.getAction())
            .entity(log.getEntity())
            .entityId(log.getEntityId())
            .success(log.isSuccess())
            .actor(log.getActor())
            .clientIp(maskIp(log.getClientIp()))
            .failReason(log.getFailReason())
            .beforeData(log.getBeforeData())
            .afterData(log.getAfterData())
            .build();
    }

    private static String maskIp(String ip) {
        return ip == null ? null : ip.replaceAll("\\d+$", "***");
    }
}
