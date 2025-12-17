package com.domino.smerp.logging.domain;

import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "api-logs")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiLog {

    @Id
    private String id;

    private String method;
    private String uri;
    private int status;
    private long duration;
    private String clientIp;
    private String principal;

    @Field(type = FieldType.Date, format = {}, pattern = "uuuu-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime timestamp;
}
