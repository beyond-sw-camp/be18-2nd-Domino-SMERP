package com.domino.smerp.item;

import com.domino.smerp.item.constants.ItemStatusStatus;
import com.domino.smerp.log.audit.AuditLogEntityListener;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.envers.Audited;

@Entity
@ToString
@Audited
@EntityListeners(AuditLogEntityListener.class)
@Getter
@NoArgsConstructor
@Table(name = "item_status")
public class ItemStatus {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "item_status_id")
  private Long itemStatusId;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private ItemStatusStatus status;

}