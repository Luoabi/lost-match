package org.xingchang.brapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.xingchang.brapi.entity.FoundItem;

import java.util.List;

/**
 * 拾获Repository
 */
@Repository
public interface FoundItemRepository extends JpaRepository<FoundItem, Long>, JpaSpecificationExecutor<FoundItem> {
    
    List<FoundItem> findByUserId(Long userId);
    
    List<FoundItem> findByStatus(Integer status);
    
    List<FoundItem> findByAuditStatus(Integer auditStatus);
    
    Long countByUserId(Long userId);
    
    Long countByStatus(Integer status);
}
