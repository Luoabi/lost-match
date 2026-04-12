package org.xingchang.brapi.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * 操作日志实体类
 */
@Data
@Entity
@Table(name = "operation_logs")
public class OperationLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id")
    private Long userId;
    
    @Column(length = 50)
    private String username;
    
    @Column(nullable = false, length = 20)
    private String action;
    
    @Column(nullable = false, length = 20)
    private String module;
    
    @Column(length = 500)
    private String content;
    
    @Column(length = 50)
    private String ip;
    
    @Column(length = 20)
    private String status = "success";
    
    @Column(columnDefinition = "TEXT")
    private String params;
    
    @Column(columnDefinition = "TEXT")
    private String response;
    
    @CreationTimestamp
    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;
    
    // 非数据库字段
    @Transient
    private String userAvatar;
    
    @Transient
    private String actionText;
    
    @Transient
    private String moduleText;
}
