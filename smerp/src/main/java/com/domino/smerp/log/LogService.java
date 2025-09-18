package com.domino.smerp.log;

import com.domino.smerp.log.dto.LogListResponse;
import com.domino.smerp.log.dto.LogResponse;
import java.util.List;

public interface LogService {
    List<LogListResponse> findAll();

    LogResponse findLogByLogId(Long logId);
}

