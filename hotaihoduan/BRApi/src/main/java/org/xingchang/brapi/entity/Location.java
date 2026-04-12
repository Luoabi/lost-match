package org.xingchang.brapi.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * 地点实体类
 */
@Data
@Entity
@Table(name = "locations")
public class Location {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 100)
    private String name;
    
    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer sort = 0;
    
    @Column(columnDefinition = "TINYINT DEFAULT 1")
    private Integer enabled = 1;
    
    @CreationTimestamp
    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;
    
    @UpdateTimestamp
    @Column(name = "update_time")
    private LocalDateTime updateTime;
}
