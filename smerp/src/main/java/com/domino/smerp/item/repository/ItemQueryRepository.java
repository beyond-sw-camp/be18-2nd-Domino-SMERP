package com.domino.smerp.item.repository;

import com.domino.smerp.item.Item;
import com.domino.smerp.item.dto.request.ItemSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemQueryRepository {

  Page<Item> searchItems(final ItemSearchRequest keyword, final Pageable pageable);

}
