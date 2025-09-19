package com.domino.smerp.common.util;

import com.domino.smerp.common.exception.CustomException;
import com.domino.smerp.common.exception.ErrorCode;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.function.Function;

// 전표번호 생성 유틸 (yyyy/mm/dd-n 형식)
@Component
public class DocumentNoGenerator {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    // 신규 전표번호 생성

    public String generate(LocalDate documentDate,
                           Function<String, Optional<Integer>> findMaxSeqByPrefix) {

        String datePart = documentDate.format(FORMATTER);
        int retry = 0;

        while (retry < 3) {
            try {
                Integer maxSeq = findMaxSeqByPrefix.apply(datePart).orElse(0);
                int nextSeq = maxSeq + 1;
                return datePart + "-" + nextSeq;
            } catch (DataIntegrityViolationException e) {
                retry++;
            }
        }
        throw new CustomException(ErrorCode.DOCUMENT_NO_GENERATION_FAILED);
    }

    // 수정 시 기존 전표 유지, 날짜가 변경된 경우만 새 번호 생성

    public String generateOrKeep(String existingDocNo,
                                 LocalDate newDocDate,
                                 Function<String, Optional<Integer>> findMaxSeqByPrefix) {

        if (newDocDate == null) {
            throw new CustomException(ErrorCode.INVALID_ORDER_REQUEST);
        }

        String newDatePart = newDocDate.format(FORMATTER);

        if (existingDocNo != null && existingDocNo.startsWith(newDatePart)) {
            return existingDocNo; // 날짜 동일 → 기존 번호 유지
        }

        return generate(newDocDate, findMaxSeqByPrefix);
    }
}
