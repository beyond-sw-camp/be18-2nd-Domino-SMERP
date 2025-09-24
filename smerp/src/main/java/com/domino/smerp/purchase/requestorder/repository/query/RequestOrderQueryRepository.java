// file: src/main/java/com/domino/smerp/purchase/requestorder/repository/query/RequestOrderQueryRepository.java
package com.domino.smerp.purchase.requestorder.repository.query;

import com.domino.smerp.purchase.requestorder.RequestOrder;
import com.domino.smerp.purchase.requestorder.dto.request.SearchRequestOrderRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RequestOrderQueryRepository {
    Page<RequestOrder> searchRequestOrder(final SearchRequestOrderRequest condition,
                                          final Pageable pageable);
}
