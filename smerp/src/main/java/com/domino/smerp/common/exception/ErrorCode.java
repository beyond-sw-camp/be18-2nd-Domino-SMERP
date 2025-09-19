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

    // item - 품목 관련 예외 정보
    // item - 400
    ITEM_NAME_REQUIRED(HttpStatus.BAD_REQUEST, "ITEM_NAME_REQUIRED", "품목명은 필수입니다."),
    ITEM_UNIT_REQUIRED(HttpStatus.BAD_REQUEST, "ITEM_UNIT_REQUIRED", "단위는 필수입니다."),
    ITEM_RFID_REQUIRED(HttpStatus.BAD_REQUEST, "ITEM_RFID_REQUIRED", "RFID는 필수입니다."),
    INVALID_ITEM_ACT(HttpStatus.BAD_REQUEST, "INVALID_ITEM_ACT", "잘못된 품목 사용여부 값입니다."),
    INVALID_SAFETY_STOCK_ACT(HttpStatus.BAD_REQUEST, "INVALID_SAFETY_STOCK_ACT", "잘못된 안전재고 여부 값입니다."),
    INVALID_SAFETY_STOCK(HttpStatus.BAD_REQUEST, "INVALID_SAFETY_STOCK", "안전재고수량은 0 이상이어야 합니다."),
    INVALID_UNIT_PRICE(HttpStatus.BAD_REQUEST, "INVALID_UNIT_PRICE", "단가는 0 이상이어야 합니다."),
    ITEM_CODE_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "ITEM_CODE_LIMIT_EXCEEDED", "해당 품목코드는 최대 999개까지 생성 가능합니다."),
    // item - 403
    ITEM_FORBIDDEN(HttpStatus.FORBIDDEN, "ITEM_FORBIDDEN", "해당 품목은 관리자만 접근할 수 있습니다. 관리자에게 문의하세요"),
    // item - 404
    ITEM_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "ITEM_STATUS_NOT_FOUND", "존재하지 않는 품목 구분입니다."),
    ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "ITEM_NOT_FOUND", "존재하지 않는 품목입니다."),
    ITEM_NOT_AVAILABLE(HttpStatus.NOT_FOUND,"ITEM_NOT_AVAILABLE", "더 이상 사용할 수 없는 품목입니다."),
    // item - 409
    DUPLICATE_ITEM(HttpStatus.CONFLICT, "DUPLICATE_ITEM", "이미 존재하는 품목입니다."),
    DUPLICATE_RFID(HttpStatus.CONFLICT, "DUPLICATE_RFID", "이미 등록된 RFID입니다."),
    DUPLICATE_ITEM_CODE(HttpStatus.CONFLICT, "ITEM_CODE_DUPLICATED","이미 존재하는 품목 코드입니다."),
    ITEM_DELETE_CONFLICT(HttpStatus.CONFLICT, "ITEM_DELETE_CONFLICT", "다른 데이터에서 참조 중이라 품목을 삭제할 수 없습니다.(수불이력)"),

    // bom
    // bom - 404
    BOM_NOT_FOUND(HttpStatus.NOT_FOUND, "BOM_NOT_FOUND", "존재하지 않는 BOM입니다."),
    // bom - 409
    BOM_DUPLICATE_RELATIONSHIP(HttpStatus.CONFLICT, "BOM_DUPLICATE_RELATIONSHIP", "이미 존재하는 BOM 관계입니다."),
    BOM_CIRCULAR_REFERENCE(HttpStatus.CONFLICT, "BOM_CIRCULAR_REFERENCE", "BOM 관계에서 순환 참조가 발생하여 수정할 수 없습니다."),
    BOM_DELETE_CONFLICT(HttpStatus.CONFLICT, "BOM_DELETE_CONFLICT", "하위 품목이 존재하여 삭제가 불가능합니다."),

    // lot_number - Lot.No 관련 예외 정보
    // lot_number - 400
    INVALID_LOTNUMBER_STATUS(HttpStatus.BAD_REQUEST, "INVALID_LOTNUMBER_STATUS", "잘못된 Lot.No 상태 값입니다."),
    LOTNUMBER_NAME_REQUIRED(HttpStatus.BAD_REQUEST, "LOTNUMBER_NAME_REQUIRED", "시리얼 번호 부여를 위해 날짜를 선택해주세요."),
    // lot_number - 403
    LOTNUMBER_FORBIDDEN(HttpStatus.FORBIDDEN, "LOTNUMBER_FORBIDDEN", "해당 Lot.No은 관리자만 접근할 수 있습니다. 관리자에게 문의하세요"),
    // lot_number - 404
    LOTNUMBER_NOT_FOUND(HttpStatus.NOT_FOUND,"LOTNUMBER_NOT_FOUND", "존재하지 않는 Lot.No입니다."),
    LOTNUMBER_NOT_AVAILABLE(HttpStatus.NOT_FOUND,"LOTNUMBER_NOT_AVAILABLE", "더 이상 사용할 수 없는 Lot.No입니다."),
    // lot_number - 409
    DUPLICATE_LOTNUMBER(HttpStatus.CONFLICT,"DUPLICATE_LOTNUMBER", "이미 사용중인 Lot.No입니다.");



    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}