package com.domino.smerp.purchase.itemrpocrossedtablerequest;

import com.domino.smerp.purchase.itemrpocrossedtablerequest.dto.request.ItemRpoCrossedTableRequest;
import com.domino.smerp.purchase.itemrpocrossedtablerequest.dto.response.ItemRpoCrossedTableResponse;
import java.util.List;

public interface ItemRpoCrossedTableService {

  ItemRpoCrossedTableResponse addLine(final Long rpoId, final ItemRpoCrossedTableRequest request);

  List<ItemRpoCrossedTableResponse> getLinesByRpoId(final Long rpoId);

  ItemRpoCrossedTableResponse updateLine(final Long rpoId, final Long lineId,
      final ItemRpoCrossedTableRequest request);

  void deleteLine(final Long rpoId, final Long lineId);
}
