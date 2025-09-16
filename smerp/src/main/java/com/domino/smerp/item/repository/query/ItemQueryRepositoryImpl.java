package com.domino.smerp.item.repository.query;

import com.domino.smerp.common.util.QuerydslUtils;
import com.domino.smerp.item.constants.ItemStatusStatus;
import com.domino.smerp.item.dto.request.SearchItemRequest;
import com.domino.smerp.item.entity.Item;
import com.domino.smerp.item.entity.QItem;
import com.domino.smerp.item.entity.QItemStatus;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Map;
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
  public Page<Item> searchItems(final SearchItemRequest keyword, final Pageable pageable) {
    QItem item = QItem.item;
    QItemStatus itemStatus = QItemStatus.itemStatus;

    // property(정렬기준) → QPath 매핑
    Map<String, Path<? extends Comparable<?>>> sortMapping = Map.of(
        "itemId", item.itemId,
        // "itemCode", item.itemCode,   // TODO: 추후 itemCode 추가 시 주석 해제
        "name", item.name,
        "unit", item.unit,
        "itemStatus", item.itemStatus.status,
        "rfid", item.rfid,
        "itemAct", item.itemAct,
        "groupName1", item.groupName1,
        "groupName2", item.groupName2,
        "groupName3", item.groupName3
    );

    // 정렬
    List<OrderSpecifier<?>> orders = QuerydslUtils.getSort(pageable.getSort(), sortMapping);

    // 기본 정렬 (없을 때 fallback)
    if (orders.isEmpty()) {
      orders.add(item.itemId.desc());
    }

    // SELECT한 데이터
    List<Item> results = queryFactory
        .selectFrom(item)
        .join(item.itemStatus, itemStatus).fetchJoin()
        .where(
            statusEq(keyword.getStatus()),
            nameContains(keyword.getName()),
            // itemCodeContains(keyword.getItemCode()),
            specificationContains(keyword.getSpecification()),
            groupName1Contains(keyword.getGroupName1()),
            groupName2Contains(keyword.getGroupName2()),
            groupName3Contains(keyword.getGroupName3())
        )
        .orderBy(orders.toArray(new OrderSpecifier[0]))       // 정렬 로직
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
            // itemCodeContains(keyword.getItemCode()),
            specificationContains(keyword.getSpecification()),
            groupName1Contains(keyword.getGroupName1()),
            groupName2Contains(keyword.getGroupName2()),
            groupName3Contains(keyword.getGroupName3())
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

  // TODO: 품목코드
  // 품목코드 검색
//  private BooleanExpression itemCodeContains(String itemCode) {
//    return (itemCode == null || itemCode.isEmpty()) ? null : QItem.item.itemCode.contains(itemCode);
//  }

  // 품목 규격 검색
  private BooleanExpression specificationContains(String specification) {
    return (specification == null || specification.isEmpty()) ? null
        : QItem.item.specification.contains(specification);
  }

  // 품목 품목그룹1 검색
  private BooleanExpression groupName1Contains(String groupName1) {
    return (groupName1 == null || groupName1.isEmpty()) ? null
        : QItem.item.groupName1.contains(groupName1);
  }

  // 품목 품목그룹2 검색
  private BooleanExpression groupName2Contains(String groupName2) {
    return (groupName2 == null || groupName2.isEmpty()) ? null
        : QItem.item.groupName2.contains(groupName2);
  }

  // 품목 품목그룹3 검색
  private BooleanExpression groupName3Contains(String groupName3) {
    return (groupName3 == null || groupName3.isEmpty()) ? null
        : QItem.item.groupName3.contains(groupName3);
  }
}
