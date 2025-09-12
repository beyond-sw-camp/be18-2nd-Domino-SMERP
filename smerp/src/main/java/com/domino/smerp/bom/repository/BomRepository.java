package com.domino.smerp.bom.repository;

import com.domino.smerp.bom.entity.Bom;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BomRepository extends JpaRepository<Bom, Long> {
    List<Bom> findByParentItemItemId(final Long parentItemId);

    boolean existsByParentItemItemIdAndChildItemItemId(Long parentItemId, Long childItemId);


}
