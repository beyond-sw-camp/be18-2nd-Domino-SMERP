package com.domino.smerp.logging.domain;

import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "action-logs")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActionLog {

    @Id
    private String id;

    private String action;

    private String entity;
    private String entityId;

    private boolean success;
    private String failReason;

    private String beforeData;
    private String afterData;

    private String actor;
    private String clientIp;

    @Field(type = FieldType.Date, format = {}, pattern = "uuuu-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime timestamp;
}
