package com.domino.smerp.common.util;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import org.springframework.data.domain.Sort;

import java.util.*;

public class QuerydslUtils {


  public static List<OrderSpecifier<?>> getSort(
      Sort sort,
      Map<String, ? extends Path<? extends Comparable<?>>> mapping
  ) {
    List<OrderSpecifier<?>> orders = new ArrayList<>();

    if (sort.isUnsorted()) {
      return orders;
    }

    sort.forEach(order -> {
      Path<? extends Comparable<?>> path = mapping.get(order.getProperty());
      if (path != null) {
        orders.add(new OrderSpecifier<>(
            order.isAscending() ? Order.ASC : Order.DESC,
            path
        ));
      }
    });

    return orders;
  }

}
