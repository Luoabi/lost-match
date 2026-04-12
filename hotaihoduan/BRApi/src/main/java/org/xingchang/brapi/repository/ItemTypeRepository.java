package org.xingchang.brapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.xingchang.brapi.entity.ItemType;

import java.util.List;

/**
 * 物品类型Repository
 */
@Repository
public interface ItemTypeRepository extends JpaRepository<ItemType, Long> {
    
    List<ItemType> findByEnabledOrderBySortAsc(Integer enabled);
    
    boolean existsByName(String name);
}
