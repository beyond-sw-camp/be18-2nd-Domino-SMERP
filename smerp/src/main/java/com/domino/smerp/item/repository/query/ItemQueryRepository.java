package com.domino.smerp.item.repository.query;

import com.domino.smerp.item.dto.request.SearchItemRequest;
import com.domino.smerp.item.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemQueryRepository {

  Page<Item> searchItems(final SearchItemRequest keyword, final Pageable pageable);

}
