package com.domino.smerp.lotno.repository.query;

import com.domino.smerp.item.constants.ItemStatusStatus;
import com.domino.smerp.item.entity.QItem;
import com.domino.smerp.lotno.constants.LotNumberStatus;
import com.domino.smerp.lotno.dto.request.SearchLotNumberRequest;
import com.domino.smerp.lotno.entity.LotNumber;
import com.domino.smerp.lotno.entity.QLotNumber;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LotNumberQueryRepositoryImpl implements LotNumberQueryRepository {

  private final JPAQueryFactory queryFactory;
  @Override
  public Page<LotNumber> searchLots(final SearchLotNumberRequest keyword,
      final Pageable pageable) {

    QLotNumber lotNumber = QLotNumber.lotNumber;
    QItem item = QItem.item;

    // 실제 데이터 조회
    List<LotNumber> results = queryFactory
        .selectFrom(lotNumber)
        .join(lotNumber.item, item).fetchJoin()
        .where(
            lotNameContains(keyword.getLotName()),
            itemNameContains(keyword.getItemName()),
            statusEq(keyword.getStatus())
        )
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    // 카운트 쿼리
    JPAQuery<Long> countQuery = queryFactory
        .select(lotNumber.count())
        .from(lotNumber)
        .join(lotNumber.item, item)
        .where(
            lotNameContains(keyword.getLotName()),
            itemNameContains(keyword.getItemName()),
            statusEq(keyword.getStatus())
        );

    return PageableExecutionUtils.getPage(results, pageable, countQuery::fetchOne);
  }

  // lot명 검색
  private BooleanExpression lotNameContains(final String lotName) {
    return (lotName == null || lotName.isEmpty()) ? null : QLotNumber.lotNumber.name.contains(lotName);
  }


  // 품목명 검색
  private BooleanExpression itemNameContains(final String itemName) {
    return (itemName == null || itemName.isEmpty()) ? null : QItem.item.name.contains(itemName);
  }

  // 품목 구분 검색
  private BooleanExpression statusEq(final String status) {
    if (status == null || status.isEmpty()) {
      return null;
    }
    LotNumberStatus statusEnum = LotNumberStatus.fromLabel(status);
    return QLotNumber.lotNumber.status.eq(statusEnum);
  }

}
