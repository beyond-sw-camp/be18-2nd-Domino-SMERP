package com.domino.smerp.log;

import com.domino.smerp.log.dto.response.LogListResponse;
import com.domino.smerp.log.dto.response.LogResponse;
import java.util.List;

public interface LogService {
    List<LogListResponse> findAll();

    LogResponse findLogByLogId(Long logId);
}

