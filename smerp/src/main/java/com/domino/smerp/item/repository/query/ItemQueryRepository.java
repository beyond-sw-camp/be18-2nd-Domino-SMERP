package com.domino.smerp.item.repository.query;

import com.domino.smerp.item.Item;
import com.domino.smerp.item.dto.request.SearchItemRequest;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemQueryRepository {

  Page<Item> searchItems(final SearchItemRequest keyword, final Pageable pageable);

  List<Long> findAllActiveItemIds();


}
