package org.xingchang.brapi.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * 用户实体类
 */
@Data
@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 50)
    private String username;
    
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    private String password;
    
    @Column(name = "real_name", length = 50)
    private String realName;
    
    @Column(length = 20)
    private String phone;
    
    @Column(length = 100)
    private String email;
    
    @Column(length = 500)
    private String avatar;
    
    @Column(nullable = false, length = 20)
    private String role = "student";
    
    @Column(name = "student_id", length = 50)
    private String studentId;
    
    @Column(length = 100)
    private String department;
    
    @Column(name = "wechat_open_id", length = 100)
    private String wechatOpenId;
    
    @Column(name = "wechat_union_id", length = 100)
    private String wechatUnionId;
    
    @Column(columnDefinition = "TINYINT DEFAULT 1")
    private Integer status = 1;
    
    @Column(name = "lost_count", columnDefinition = "INT DEFAULT 0")
    private Integer lostCount = 0;
    
    @Column(name = "found_count", columnDefinition = "INT DEFAULT 0")
    private Integer foundCount = 0;
    
    @Column(name = "match_success_count", columnDefinition = "INT DEFAULT 0")
    private Integer matchSuccessCount = 0;
    
    @Column(name = "credit_score", columnDefinition = "INT DEFAULT 100")
    private Integer creditScore = 100;
    
    @Column(name = "last_login_time")
    private LocalDateTime lastLoginTime;
    
    @CreationTimestamp
    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;
    
    @UpdateTimestamp
    @Column(name = "update_time")
    private LocalDateTime updateTime;
}
