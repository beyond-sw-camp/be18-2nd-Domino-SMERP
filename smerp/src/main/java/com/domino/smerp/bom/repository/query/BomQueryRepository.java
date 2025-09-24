package com.domino.smerp.bom.repository.query;

import com.domino.smerp.bom.dto.request.SearchBomRequest;
import com.domino.smerp.bom.dto.response.BomListResponse;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BomQueryRepository {

  Page<BomListResponse> searchBoms(final SearchBomRequest request, final Pageable pageable);

  // 고아 데이터까지 찾아오기
  List<Long> findAllBomAndOrphanItemIds();

}
