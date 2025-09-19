package com.domino.smerp.bom.service.cache;

import com.domino.smerp.bom.entity.Bom;
import com.domino.smerp.bom.entity.BomCostCache;
import com.domino.smerp.bom.repository.BomRepository;
import com.domino.smerp.item.Item;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BomCacheBuilder {

  private final BomRepository bomRepository;

  public List<BomCostCache> build(final Item rootItem) {
    List<BomCostCache> caches = new ArrayList<>();
    dfsBuild(rootItem, rootItem, BigDecimal.ONE, 0, caches);
    return caches;
  }

  private BigDecimal dfsBuild(
      final Item root,
      final Item current,
      final BigDecimal accQty,
      final int depth,
      final List<BomCostCache> caches
  ) {
    List<Bom> children = bomRepository.findByParentItem_ItemId(current.getItemId());

    // 리프 노드
    if (children.isEmpty()) {
      BigDecimal unitCost =
          current.getInboundUnitPrice() != null ? current.getInboundUnitPrice() : BigDecimal.ZERO;
      BigDecimal totalCost = accQty.multiply(unitCost);

      caches.add(BomCostCache.create(
          root.getItemId(),
          current.getItemId(),
          depth,
          accQty,
          unitCost,
          totalCost
      ));
      return totalCost;
    }

    // 내부 노드
    BigDecimal totalCost = BigDecimal.ZERO;
    for (Bom childBom : children) {
      Item child = childBom.getChildItem();
      BigDecimal newAccQty = accQty.multiply(childBom.getQty());
      totalCost = totalCost.add(dfsBuild(root, child, newAccQty, depth + 1, caches));
    }

    caches.add(BomCostCache.create(
        root.getItemId(),
        current.getItemId(),
        depth,
        accQty,
        BigDecimal.ZERO,
        totalCost
    ));

    return totalCost;
  }
}
