package com.domino.smerp.purchase.requestpurchaseorder;

import com.domino.smerp.common.BaseEntity;
import com.domino.smerp.purchase.requestpurchaseorder.constants.RequestPurchaseOrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "request_purchase_order")
public class RequestPurchaseOrder extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rpo_id", nullable = false)
    private Long rpoId; // 구매요청 PK

    @Column(name = "user_id", nullable = false)
    private Long userId; // 사용자 FK

    @Column(name = "delivery_date", nullable = false)
    private LocalDate deliveryDate; // 납기요청일자

    @Column(name = "remark", length = 100)
    private String remark; // 비고

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private RequestPurchaseOrderStatus status; // 구매요청 상태

    @Builder.Default
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false; // 소프트 삭제 여부

    @Column(name = "document_no", nullable = false, length = 30)
    private String documentNo; // 전표번호

    // ===== 도메인 메서드 =====
    public void updateStatus(RequestPurchaseOrderStatus status) {
        this.status = status;
    }

    public void updateRemark(String remark) {
        this.remark = remark;
    }

    public void delete() {
        this.isDeleted = true;
    }
}
