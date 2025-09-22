package com.domino.smerp.purchase.requestorder;

import com.domino.smerp.client.Client;
import com.domino.smerp.common.BaseEntity;
import com.domino.smerp.purchase.itemrequestorder.ItemRequestOrder;
import com.domino.smerp.purchase.purchaseorder.PurchaseOrder;
import com.domino.smerp.purchase.requestorder.constants.RequestOrderStatus;
import com.domino.smerp.purchase.requestpurchaseorder.RequestPurchaseOrder;
import com.domino.smerp.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Builder
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "request_order")
public class RequestOrder extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ro_id", nullable = false)
    private Long roId; // 발주 PK

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rpo_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private RequestPurchaseOrder requestPurchaseOrder;

    // PurchaseOrder 엔티티와 OneToOne 매핑
    // mappedBy를 사용하여 PurchaseOrder 엔티티의 'requestOrder' 필드에 의해 매핑됨을 명시합니다.
    @OneToOne(mappedBy = "requestOrder", fetch = FetchType.LAZY)
    private PurchaseOrder purchaseOrder;

    // 사용자 (User) 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    // 거래처 (Client) 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Client client;

    @Column(name = "delivery_date", nullable = false)
    private LocalDate deliveryDate; // 납기일자

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private RequestOrderStatus status; // 발주 상태

    @Column(name = "remark", length = 100)
    private String remark; // 비고

    @Builder.Default
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false; // 소프트 삭제 여부

    @Column(name = "document_no", nullable = false, length = 30)
    private String documentNo; // 전표 번호

    @OneToMany(mappedBy = "requestOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemRequestOrder> items = new ArrayList<>();

    // ====== 도메인 메서드 ======
    public void updateDeliveryDate(LocalDate deliveryDate) {
    this.deliveryDate = deliveryDate;
    }

    public void updateStatus(RequestOrderStatus status) {
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
