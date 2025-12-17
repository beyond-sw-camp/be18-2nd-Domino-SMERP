package com.domino.smerp.logging.test;

import com.domino.smerp.logging.domain.ApiLog;
import com.domino.smerp.logging.repository.ApiLogRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ElasticTestController {

    private final ApiLogRepository apiLogRepository;

    @GetMapping("/es-test")
    public String test() {
        ApiLog log = ApiLog.builder()
            .method("GET")
            .uri("/es-test")
            .status(200)
            .duration(10)
            .clientIp("127.0.0.1")
            .timestamp(LocalDateTime.now())
            .build();

        apiLogRepository.save(log);
        return "OK";
    }

    @GetMapping("/error-test")
    public void errorTest() {
        throw new RuntimeException("테스트용 예외");
    }
}
