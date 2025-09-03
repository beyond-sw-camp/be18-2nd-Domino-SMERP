package com.domino.smerp.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class DatedEntity {

  @Column(name = "created_date", nullable = false, updatable = false)
  private LocalDate createdDate; // DATE

  @Column(name = "updated_date")
  private LocalDate updatedDate; // DATE

  @PrePersist
  public void onPrePersist() {
    this.createdDate = LocalDate.now();
  }

  @PreUpdate
  public void onPreUpdate() {
    this.updatedDate = LocalDate.now();
  }
}

