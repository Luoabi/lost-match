package org.xingchang.brapi.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 拾获物实体类
 */
@Data
@Entity
@Table(name = "found_items")
public class FoundItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String title;
    
    @Column(nullable = false, length = 50)
    private String type;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "JSON")
    private List<String> images;
    
    @Column(length = 100)
    private String location;
    
    @Column(name = "location_detail", length = 200)
    private String locationDetail;
    
    @Column(precision = 10, scale = 6)
    private BigDecimal longitude;
    
    @Column(precision = 10, scale = 6)
    private BigDecimal latitude;
    
    @Column(name = "found_time")
    private LocalDateTime foundTime;
    
    @Column(name = "contact_name", length = 50)
    private String contactName;
    
    @Column(name = "contact_phone", length = 20)
    private String contactPhone;
    
    @Column(columnDefinition = "TINYINT DEFAULT 0")
    private Integer status = 0;
    
    @Column(name = "view_count", columnDefinition = "INT DEFAULT 0")
    private Integer viewCount = 0;
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "JSON")
    private List<String> keywords;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "audit_status", columnDefinition = "TINYINT DEFAULT 0")
    private Integer auditStatus = 0;
    
    @Column(name = "audit_remark", length = 500)
    private String auditRemark;
    
    @Column(name = "audit_time")
    private LocalDateTime auditTime;
    
    @Column(name = "auditor_id")
    private Long auditorId;
    
    @CreationTimestamp
    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;
    
    @UpdateTimestamp
    @Column(name = "update_time")
    private LocalDateTime updateTime;
    
    // 非数据库字段，用于前端展示
    @Transient
    private String userName;
    
    @Transient
    private String userAvatar;
    
    @Transient
    private String statusText;
    
    @Transient
    private String auditStatusText;
}
