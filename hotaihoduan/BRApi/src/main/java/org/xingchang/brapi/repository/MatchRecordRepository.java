package org.xingchang.brapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.xingchang.brapi.entity.MatchRecord;

import java.util.List;

/**
 * 匹配记录Repository
 */
@Repository
public interface MatchRecordRepository extends JpaRepository<MatchRecord, Long>, JpaSpecificationExecutor<MatchRecord> {
    
    List<MatchRecord> findByLostItemId(Long lostItemId);
    
    List<MatchRecord> findByFoundItemId(Long foundItemId);
    
    List<MatchRecord> findByStatus(Integer status);
}
