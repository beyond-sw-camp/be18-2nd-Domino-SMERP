package com.domino.smerp.bom.support;

import com.domino.smerp.bom.entity.Bom;
import com.domino.smerp.bom.repository.BomRepository;
import com.domino.smerp.common.exception.CustomException;
import com.domino.smerp.common.exception.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BomReader {

  private final BomRepository bomRepository;

  public Bom findBomById(final Long bomId) {
    return bomRepository.findById(bomId)
        .orElseThrow(() -> new CustomException(ErrorCode.BOM_NOT_FOUND));
  }

  public List<Bom> findChildren(final Long parentItemId) {
    return bomRepository.findByParentItem_ItemId(parentItemId);
  }

}
