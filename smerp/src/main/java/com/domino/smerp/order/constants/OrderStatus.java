package com.domino.smerp.order.constants;

public enum OrderStatus {
    PENDING,      // 승인전 판매 전
    APPROVED,   // 승인 후 판매 전
    COMPLETED,    // 승인 후 판매 후
    RETURNED,     // 반려
}