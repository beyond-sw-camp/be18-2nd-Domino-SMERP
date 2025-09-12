package com.domino.smerp.item.repository;

import com.domino.smerp.item.entity.Item;
import com.domino.smerp.item.entity.QItem;
import com.domino.smerp.item.entity.QItemStatus;
import com.domino.smerp.item.constants.ItemStatusStatus;
import com.domino.smerp.item.dto.request.ItemSearchRequest;
import com.domino.smerp.item.entity.QItemStatus;
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
public class ItemQueryRepositoryImpl implements ItemQueryRepository {

  private final JPAQueryFactory queryFactory;

  @Override
  public Page<Item> searchItems(final ItemSearchRequest keyword, final Pageable pageable) {
    QItem item = QItem.item;
    QItemStatus itemStatus = QItemStatus.itemStatus;

    List<Item> results = queryFactory
        .selectFrom(item)
        .join(item.itemStatus, itemStatus).fetchJoin()
        .where(
            statusEq(keyword.getStatus()),
            nameContains(keyword.getName()),
            specificationContains(keyword.getSpecification()),
            groupName1Eq(keyword.getGroupName1()),
            groupName2Eq(keyword.getGroupName2()),
            groupName3Eq(keyword.getGroupName3())
        )
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    JPAQuery<Long> countQuery = queryFactory
        .select(item.count())
        .from(item)
        .join(item.itemStatus, itemStatus)
        .where(
            statusEq(keyword.getStatus()),
            nameContains(keyword.getName()),
            specificationContains(keyword.getSpecification()),
            groupName1Eq(keyword.getGroupName1()),
            groupName2Eq(keyword.getGroupName2()),
            groupName3Eq(keyword.getGroupName3())
        );

    return PageableExecutionUtils.getPage(results, pageable, countQuery::fetchOne);
  }



  // 품목 구분 검색
  private BooleanExpression statusEq(String status) {
    if (status == null || status.isEmpty()) {
      return null;
    }
    ItemStatusStatus statusEnum = ItemStatusStatus.fromLabel(status);
    return QItem.item.itemStatus.status.eq(statusEnum);
  }

  // 품목명 검색
  private BooleanExpression nameContains(String name) {
    return (name == null || name.isEmpty()) ? null : QItem.item.name.contains(name);
  }

  // 품목 규격 검색
  private BooleanExpression specificationContains(String specification) {
    return (specification == null || specification.isEmpty()) ? null
        : QItem.item.specification.contains(specification);
  }

  // 품목 품목그룹1 검색
  private BooleanExpression groupName1Eq(String groupName1) {
    return (groupName1 == null || groupName1.isEmpty()) ? null
        : QItem.item.groupName1.eq(groupName1);
  }

  // 품목 품목그룹2 검색
  private BooleanExpression groupName2Eq(String groupName2) {
    return (groupName2 == null || groupName2.isEmpty()) ? null
        : QItem.item.groupName2.eq(groupName2);
  }

  // 품목 품목그룹3 검색
  private BooleanExpression groupName3Eq(String groupName3) {
    return (groupName3 == null || groupName3.isEmpty()) ? null
        : QItem.item.groupName3.eq(groupName3);
  }
}
