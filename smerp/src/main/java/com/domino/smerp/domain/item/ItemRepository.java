package com.domino.smerp.domain.item;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ItemRepository extends JpaRepository<Item, Integer> {
  // 특정 품목을 이름으로 찾는 등의 메서드를 여기에 추가할 수 있습니다.
}



