package com.domino.smerp.purchase.requestpurchaseorder;

import com.domino.smerp.common.BaseEntity;
import com.domino.smerp.purchase.itemrequestorder.ItemRequestOrder;
import com.domino.smerp.purchase.itemrequestpurchaseorder.ItemRequestPurchaseOrder;
import com.domino.smerp.purchase.requestorder.RequestOrder;
import com.domino.smerp.purchase.requestpurchaseorder.constants.RequestPurchaseOrderStatus;
import com.domino.smerp.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Builder
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "request_purchase_order")
@SQLRestriction("is_deleted = false")
public class RequestPurchaseOrder extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rpo_id", nullable = false)
    private Long rpoId; // 구매요청 PK

    // 사용자 (User) 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    @Column(name = "delivery_date")
    private LocalDate deliveryDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private RequestPurchaseOrderStatus status; // 구매요청 상태

    @Column(name = "remark", length = 100)
    private String remark; // 비고

    @Builder.Default
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false; // 소프트 삭제 여부

    @Column(name = "document_no", nullable = false, length = 30)
    private String documentNo; // 전표 번호

    // RequestOrder와 1:N 관계 (하나의 구매요청에 여러 발주가 매핑될 수 있음)
    @OneToMany(mappedBy = "requestPurchaseOrder", orphanRemoval = true)
    private List<ItemRequestPurchaseOrder> items = new ArrayList<>();

    // ====== 도메인 메서드 ======
    public void updateDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public void updateStatus(RequestPurchaseOrderStatus status) {
        this.status = status;
    }

    public void updateRemark(String remark) {
        this.remark = remark;
    }

    public void delete() {
        this.isDeleted = true;
    }

    public void updateDocumentNo(final LocalDate newDate, int newSequence) {
        this.documentNo = String.format("%s-%d",
                newDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")),
                newSequence);
    }

}
