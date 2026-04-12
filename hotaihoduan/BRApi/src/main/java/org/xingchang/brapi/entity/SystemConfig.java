package org.xingchang.brapi.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * 系统配置实体类
 */
@Data
@Entity
@Table(name = "system_config")
public class SystemConfig {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "config_key", nullable = false, unique = true, length = 100)
    private String configKey;
    
    @Column(name = "config_value", columnDefinition = "TEXT")
    private String configValue;
    
    @Column(length = 200)
    private String description;
    
    @UpdateTimestamp
    @Column(name = "update_time")
    private LocalDateTime updateTime;
}
