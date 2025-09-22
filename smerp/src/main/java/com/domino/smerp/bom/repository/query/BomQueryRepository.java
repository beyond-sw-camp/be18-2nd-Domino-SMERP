package com.domino.smerp.bom.repository.query;

import com.domino.smerp.bom.entity.BomClosure;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BomQueryRepository {

  Page<BomClosure> searchBoms(Long itemId, Pageable pageable);

  // 고아 데이터까지 찾아오기
  List<Long> findAllBomAndOrphanItemIds();

}
