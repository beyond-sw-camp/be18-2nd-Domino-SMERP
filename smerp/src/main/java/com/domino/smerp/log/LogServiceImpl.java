package com.domino.smerp.log;

import com.domino.smerp.log.dto.response.LogListResponse;
import com.domino.smerp.log.dto.response.LogResponse;
import com.domino.smerp.log.repository.LogRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {
    private final LogRepository logRepository;

    @Override
    public List<LogListResponse> findAll() {
        return logRepository.findAll().stream()
                            .map(log -> LogListResponse.builder()
                                                       .doAt(log.getDoAt())
                                                       .entity(log.getEntity())
                                                       .action(log.getAction())
                                                       .actor(log.getActor())
                                                       .build())
                            .collect(Collectors.toList());
    }

    @Override
    public LogResponse findLogByLogId(Long logId) {
        Log log = logRepository.findById(logId).orElseThrow();

        return LogResponse.builder()
                          .doAt(log.getDoAt())
                          .entity(log.getEntity())
                          .action(log.getAction())
                          .actor(log.getActor())
                          .beforeData(log.getBeforeData())
                          .afterData(log.getAfterData())
                          .build();
    }
}
