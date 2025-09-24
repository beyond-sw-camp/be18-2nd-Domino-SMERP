package com.domino.smerp.workorder.repository;

import com.domino.smerp.workorder.WorkOrder;
import com.domino.smerp.workorder.dto.request.SearchWorkOrderRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WorkOrderQueryRepository {

  Page<WorkOrder> searchWorkOrders(SearchWorkOrderRequest request, Pageable pageable);
}
