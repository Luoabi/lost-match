package org.xingchang.brapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.xingchang.brapi.entity.SystemConfig;

import java.util.Optional;

/**
 * 系统配置Repository
 */
@Repository
public interface SystemConfigRepository extends JpaRepository<SystemConfig, Long> {
    
    Optional<SystemConfig> findByConfigKey(String configKey);
}
