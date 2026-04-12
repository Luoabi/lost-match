package org.xingchang.brapi.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 匹配记录实体类
 */
@Data
@Entity
@Table(name = "match_records")
public class MatchRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "lost_item_id", nullable = false)
    private Long lostItemId;
    
    @Column(name = "found_item_id", nullable = false)
    private Long foundItemId;
    
    @Column(name = "match_score", precision = 3, scale = 2)
    private BigDecimal matchScore;
    
    @Column(name = "image_score", precision = 3, scale = 2)
    private BigDecimal imageScore;
    
    @Column(name = "location_score", precision = 3, scale = 2)
    private BigDecimal locationScore;
    
    @Column(name = "text_score", precision = 3, scale = 2)
    private BigDecimal textScore;
    
    @Column(name = "match_type", length = 20)
    private String matchType = "auto";
    
    @Column(columnDefinition = "TINYINT DEFAULT 0")
    private Integer status = 0;
    
    @Column(name = "confirm_user_id")
    private Long confirmUserId;
    
    @Column(name = "confirm_time")
    private LocalDateTime confirmTime;
    
    @Column(length = 500)
    private String feedback;
    
    @Column(name = "feedback_score")
    private Integer feedbackScore;
    
    @CreationTimestamp
    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;
    
    // 非数据库字段
    @Transient
    private String lostItemTitle;
    
    @Transient
    private String lostItemImage;
    
    @Transient
    private String lostItemLocation;
    
    @Transient
    private String foundItemTitle;
    
    @Transient
    private String foundItemImage;
    
    @Transient
    private String foundItemLocation;
    
    @Transient
    private String statusText;
    
    // 详情页面额外字段
    @Transient
    private java.util.List<String> lostItemImages;
    
    @Transient
    private String lostItemType;
    
    @Transient
    private String lostItemDescription;
    
    @Transient
    private LocalDateTime lostItemTime;
    
    @Transient
    private java.util.List<String> foundItemImages;
    
    @Transient
    private String foundItemType;
    
    @Transient
    private String foundItemDescription;
    
    @Transient
    private LocalDateTime foundItemTime;
}
