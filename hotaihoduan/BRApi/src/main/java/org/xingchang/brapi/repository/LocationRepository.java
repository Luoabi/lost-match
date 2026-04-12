package org.xingchang.brapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.xingchang.brapi.entity.Location;

import java.util.List;

/**
 * 地点Repository
 */
@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    
    List<Location> findByEnabledOrderBySortAsc(Integer enabled);
    
    boolean existsByName(String name);
}
