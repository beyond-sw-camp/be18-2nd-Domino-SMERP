package com.domino.smerp.bom.repository.query;

import java.util.List;

public interface BomQueryRepository {

  // 고아 데이터까지 찾아오기
  List<Long> findAllBomAndOrphanItemIds();

}
