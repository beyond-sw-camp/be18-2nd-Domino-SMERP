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
    DUPLICATE_BUSINESS_NUMBER(HttpStatus.CONFLICT, "DUPLICATE_BUSINESS_NUMBER", "이미 등록된 사업자번호입니다."),
    DUPLICATE_COMPANY_NAME(HttpStatus.CONFLICT, "DUPLICATE_COMPANY_NAME", "이미 등록된 회사명입니다."),

    //item - 품목과 관련된 예외 정보
    ITEM_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "ITEM_STATUS_NOT_FOUND", "존재하지 않는 품목 구분입니다."),
    ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "ITEM_NOT_FOUND", "존재하지 않는 품목입니다."),
    ITEM_NAME_REQUIRED(HttpStatus.BAD_REQUEST, "ITEM_NAME_REQUIRED", "품목명은 필수입니다."),
    ITEM_UNIT_REQUIRED(HttpStatus.BAD_REQUEST, "ITEM_UNIT_REQUIRED", "단위는 필수입니다."),
    ITEM_RFID_REQUIRED(HttpStatus.BAD_REQUEST, "ITEM_RFID_REQUIRED", "RFID는 필수입니다."),
    INVALID_ITEM_ACT(HttpStatus.BAD_REQUEST, "INVALID_ITEM_ACT", "잘못된 품목 사용여부 값입니다."),
    INVALID_SAFETY_STOCK_ACT(HttpStatus.BAD_REQUEST, "INVALID_SAFETY_STOCK_ACT", "잘못된 안전재고 여부 값입니다."),
    INVALID_SAFETY_STOCK(HttpStatus.BAD_REQUEST, "INVALID_SAFETY_STOCK", "안전재고수량은 0 이상이어야 합니다."),
    INVALID_UNIT_PRICE(HttpStatus.BAD_REQUEST, "INVALID_UNIT_PRICE", "단가는 0 이상이어야 합니다."),
    ITEM_DELETE_CONFLICT(HttpStatus.CONFLICT, "ITEM_DELETE_CONFLICT", "다른 데이터에서 참조 중이라 품목을 삭제할 수 없습니다.(수불이력)"), // 소프트 딜리트 예정
    DUPLICATE_ITEM(HttpStatus.CONFLICT, "DUPLICATE_ITEM", "이미 존재하는 품목입니다."),
    DUPLICATE_RFID(HttpStatus.CONFLICT, "DUPLICATE_RFID", "이미 등록된 RFID입니다."),

    // order - 주문과 관련된 예외 정보
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "ORDER_NOT_FOUND", "존재하지 않는 주문입니다."),
    ITEMS_REQUIRED(HttpStatus.BAD_REQUEST, "ITEMS_REQUIRED", "주문에는 최소 1개 이상의 품목이 필요합니다."),
    INVALID_ORDER_REQUEST(HttpStatus.BAD_REQUEST, "INVALID_REQUEST", "잘못된 주문 요청입니다."),

    // 전표 생성 예외 정보
    DOCUMENT_NO_GENERATION_FAILED(HttpStatus.CONFLICT, "DOCUMENT_NO_GENERATION_FAILED", "전표번호 생성에 실패했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}