package com.domino.smerp.purchase.purchaseorder;

import com.domino.smerp.common.BaseEntity;
import com.domino.smerp.purchase.requestorder.RequestOrder;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 구매(PurchaseOrder) 엔티티 - 발주(RequestOrder)와 1:1 매핑 - 발주 없는 구매는 존재할 수 없음
 */
@Entity
@Getter
@Builder
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "purchase_order")
// @Table(name = "purchase_order" ,indexes = {@Index(name = "idx_po_ro_id", columnList = "ro_id")})
public class PurchaseOrder extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "po_id", nullable = false)
  private Long poId; // 구매 PK

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ro_id", nullable = false, unique = true, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
  @Comment("발주 FK")
  private RequestOrder requestOrder;

  @Column(name = "warehouse_name", nullable = false, length = 50)
  private String warehouseName;

  @Column(name = "qty", nullable = false, precision = 12, scale = 3)
  private BigDecimal qty; // 수량 (발주와 다를 수 있음)

  @Column(name = "inbound_unit_price", nullable = false, precision = 12, scale = 2)
  private BigDecimal inboundUnitPrice;

    @Column(name = "special_price", precision = 12, scale = 2, updatable = false)
    private BigDecimal specialPrice;  // 특별단가

    @Column(name = "surtax", nullable = false, precision = 12, scale = 2)
  private BigDecimal surtax; // 부가세

  @Column(name = "price", nullable = false, precision = 12, scale = 2)
  private BigDecimal price; // 금액 (qty * 단가)

  @Column(name = "remark", length = 100)
  private String remark; // 비고

  @Builder.Default
  @Column(name = "is_deleted", nullable = false)
  private boolean isDeleted = false; // 소프트 삭제 여부

  @Column(name = "document_no", nullable = false, length = 30)
  private String documentNo; // 전표 번호

  // ====== 도메인 메서드 ======
  public void updateQty(final BigDecimal qty) {
    this.qty = qty;
  }

  public void updateSurtax(final BigDecimal surtax) {
    this.surtax = surtax;
  }

  public void updatePrice(final BigDecimal price) {
    this.price = price;
  }

  public void updateRemark(final String remark) {
    this.remark = remark;
  }

  public void updateInboundUnitPrice(final BigDecimal inboundUnitPrice) {
    this.inboundUnitPrice = inboundUnitPrice;
  }

  public void updateWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

  // Soft Delete
  public void delete() {this.isDeleted = true;}

  public void updateDocumentNo(final LocalDate newDate, int newSequence) {
    this.documentNo = String.format("%s-%d",
        newDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")),
        newSequence);
  }
    public void updateSpecialPrice(BigDecimal specialPrice, BigDecimal inboundUnitPrice) {
        this.specialPrice = (specialPrice != null) ? specialPrice : inboundUnitPrice;
    }
}
