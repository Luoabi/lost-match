package org.xingchang.brapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.xingchang.brapi.entity.LostItem;

import java.util.List;

/**
 * 失物Repository
 */
@Repository
public interface LostItemRepository extends JpaRepository<LostItem, Long>, JpaSpecificationExecutor<LostItem> {
    
    List<LostItem> findByUserId(Long userId);
    
    List<LostItem> findByStatus(Integer status);
    
    List<LostItem> findByAuditStatus(Integer auditStatus);
    
    Long countByUserId(Long userId);
    
    Long countByStatus(Integer status);
}
