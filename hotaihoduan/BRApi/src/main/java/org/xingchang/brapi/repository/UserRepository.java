package org.xingchang.brapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.xingchang.brapi.entity.User;

import java.util.Optional;

/**
 * 用户Repository
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByPhone(String phone);
    
    Optional<User> findByWechatOpenId(String wechatOpenId);
    
    boolean existsByUsername(String username);
    
    boolean existsByPhone(String phone);
}
