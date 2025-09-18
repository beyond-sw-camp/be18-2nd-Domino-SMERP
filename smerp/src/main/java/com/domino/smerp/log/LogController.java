package com.domino.smerp.log;

import com.domino.smerp.log.dto.response.LogListResponse;
import com.domino.smerp.log.dto.response.LogResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/logs")
@RequiredArgsConstructor
public class LogController {
    public final LogService logService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<LogListResponse> findAllLogs() {
        return logService.findAll();
    }

    @GetMapping("/{logId}")
    @ResponseStatus(HttpStatus.OK)
    public LogResponse findLogById(@PathVariable Long logId) {
        return logService.findLogByLogId(logId);
    }

//    @GetMapping
//    @ResponseStatus(HttpStatus.OK)
//    public LogResponse searchLogs (SearchLogRequest searchLogRequest) {
//        return logService.searchLog(searchLogRequest);
//    }
}