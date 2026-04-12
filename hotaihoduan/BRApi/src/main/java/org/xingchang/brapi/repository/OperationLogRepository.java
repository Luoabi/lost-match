package org.xingchang.brapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.xingchang.brapi.entity.OperationLog;

import java.time.LocalDateTime;

/**
 * 操作日志Repository
 */
@Repository
public interface OperationLogRepository extends JpaRepository<OperationLog, Long>, JpaSpecificationExecutor<OperationLog> {
    
    void deleteByCreateTimeBefore(LocalDateTime time);
}
