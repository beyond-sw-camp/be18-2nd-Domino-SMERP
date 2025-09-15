package com.domino.smerp.lotno.repository.command;

import com.domino.smerp.item.entity.QItem;
import com.domino.smerp.lotno.entity.QLotNumber;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class LotNumberCommandRepositoryImpl implements LotNumberCommandRepository {

  private final JPAQueryFactory queryFactory;

  private final QLotNumber lotNumber = QLotNumber.lotNumber;
  private final QItem item = QItem.item;

  @Override
  @Transactional
  public void bulkSoftDeleteByItemId(final Long itemId) {
    queryFactory
        .update(lotNumber)
        .set(lotNumber.isDeleted, true)
        .where(lotNumber.item.itemId.eq(itemId))
        .execute(); // 쿼리 실행
  }
}
