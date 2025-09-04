package com.domino.smerp.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    //user - 유저와 관련된 예외 정보
    INVALID_LOGIN(HttpStatus.UNAUTHORIZED, "INVALID_LOGIN", "로그인 정보가 올바르지 않습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "사용자를 찾을 수 없습니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "DUPLICATE_EMAIL", "이미 등록된 이메일입니다."),
    DUPLICATE_PHONE(HttpStatus.CONFLICT, "DUPLICATE_PHONE", "이미 등록된 전화번호입니다."),
    DUPLICATE_LOGINID(HttpStatus.CONFLICT, "DUPLICATE_LOGINID", "이미 사용 중인 아이디입니다."),
    DUPLICATE_SSN(HttpStatus.CONFLICT, "DUPLICATE_SSN", "이미 등록된 주민번호입니다."),

    //client - 거래처와 관련된 예외 정보
    CLIENT_NOT_FOUND(HttpStatus.NOT_FOUND, "CLIENT_NOT_FOUND", "존재하지 않는 거래처입니다."),
    DUPLICATE_COMPANY(HttpStatus.CONFLICT, "DUPLICATE_COMPANY", "이미 등록된 사업자번호입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {

        this.status = status;
        this.code = code;
        this.message = message;
    }
}
