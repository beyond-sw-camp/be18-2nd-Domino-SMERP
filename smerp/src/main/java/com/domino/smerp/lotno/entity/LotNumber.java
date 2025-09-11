package com.domino.smerp.lotno.entity;

import com.domino.smerp.common.BaseEntity;
import com.domino.smerp.item.entity.Item;
import com.domino.smerp.lotno.constants.LotNumberStatus;
import com.domino.smerp.lotno.dto.request.CreateLotNumberRequest;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "lot_number")
@SQLRestriction("is_deleted = false")
public class LotNumber extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "lot_id")
  private Long lotId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "item_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
  private Item item;

  @Column(name = "name", nullable = false, length = 30)
  private String name;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private LotNumberStatus status;

  @Builder.Default
  @Column(name = "is_deleted", nullable = false)
  private boolean isDeleted = false;


  // Lot.No 생성
  public static LotNumber create(CreateLotNumberRequest request, Item item) {
    return LotNumber.builder()
        .item(item)
        .name(request.getName())
        .status(LotNumberStatus.fromLabel(request.getStatus()))
        .build();
  }

  // 품목 삭제 (소프트딜리트)
  public void delete() {
    this.isDeleted = true;
  }

}