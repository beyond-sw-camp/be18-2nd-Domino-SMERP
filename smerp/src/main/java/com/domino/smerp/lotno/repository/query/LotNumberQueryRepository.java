package com.domino.smerp.lotno.repository.query;

import com.domino.smerp.lotno.LotNumber;
import com.domino.smerp.lotno.dto.request.SearchLotNumberRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LotNumberQueryRepository {

  Page<LotNumber> searchLots(final SearchLotNumberRequest keyword, final Pageable pageable);

}
