package org.xingchang.brapi.service;

import cn.hutool.crypto.digest.DigestUtil;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xingchang.brapi.common.PageResult;
import org.xingchang.brapi.dto.LoginRequest;
import org.xingchang.brapi.dto.LoginResponse;
import org.xingchang.brapi.entity.User;
import org.xingchang.brapi.repository.UserRepository;
import org.xingchang.brapi.util.JwtUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 用户Service
 */
@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    /**
     * 用户登录
     */
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("用户名或密码错误"));
        
        // 验证密码（使用MD5加密）
        String encryptedPassword = DigestUtil.md5Hex(request.getPassword());
        if (!encryptedPassword.equals(user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }
        
        // 检查用户状态
        if (user.getStatus() == 0) {
            throw new RuntimeException("账号已被禁用");
        }
        
        // 更新最后登录时间
        user.setLastLoginTime(LocalDateTime.now());
        userRepository.save(user);
        
        // 生成Token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());
        
        return new LoginResponse(token, LoginResponse.UserInfo.fromUser(user));
    }
    
    /**
     * 用户注册
     */
    @Transactional
    public User register(User user) {
        // 验证必填字段
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new RuntimeException("用户名不能为空");
        }
        
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new RuntimeException("密码不能为空");
        }
        
        // 检查用户名是否已存在
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("用户名已存在");
        }
        
        // 检查手机号是否已存在
        if (user.getPhone() != null && !user.getPhone().isEmpty()) {
            Optional<User> existingUser = userRepository.findByPhone(user.getPhone());
            if (existingUser.isPresent()) {
                throw new RuntimeException("手机号已被注册");
            }
        }
        
        // 密码加密（确保密码不为空）
        String rawPassword = user.getPassword().trim();
        user.setPassword(DigestUtil.md5Hex(rawPassword));
        
        // 设置默认值
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("student"); // 默认角色为学生
        }
        if (user.getStatus() == null) {
            user.setStatus(1); // 默认状态为启用
        }
        if (user.getCreditScore() == null) {
            user.setCreditScore(100); // 默认信用分100
        }
        
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        
        return userRepository.save(user);
    }
    
    /**
     * 获取用户信息
     */
    public User getUserInfo(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
    }
    
    /**
     * 获取用户列表（分页）
     */
    public PageResult<User> getUserList(Integer page, Integer size, String keyword, String role, Integer status) {
        Specification<User> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (keyword != null && !keyword.isEmpty()) {
                Predicate p1 = cb.like(root.get("username"), "%" + keyword + "%");
                Predicate p2 = cb.like(root.get("realName"), "%" + keyword + "%");
                Predicate p3 = cb.like(root.get("phone"), "%" + keyword + "%");
                predicates.add(cb.or(p1, p2, p3));
            }
            
            if (role != null && !role.isEmpty()) {
                predicates.add(cb.equal(root.get("role"), role));
            }
            
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createTime"));
        Page<User> pageData = userRepository.findAll(spec, pageRequest);
        
        return new PageResult<>(
                pageData.getContent(),
                pageData.getTotalElements(),
                page,
                size
        );
    }
    
    /**
     * 添加用户
     */
    @Transactional
    public User addUser(User user) {
        // 检查用户名是否已存在
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("用户名已存在");
        }
        
        // 密码加密
        user.setPassword(DigestUtil.md5Hex(user.getPassword()));
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        
        return userRepository.save(user);
    }
    
    /**
     * 更新用户
     */
    @Transactional
    public User updateUser(User user) {
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        // 更新字段
        existingUser.setRealName(user.getRealName());
        existingUser.setPhone(user.getPhone());
        existingUser.setEmail(user.getEmail());
        existingUser.setAvatar(user.getAvatar());
        existingUser.setRole(user.getRole());
        existingUser.setStudentId(user.getStudentId());
        existingUser.setDepartment(user.getDepartment());
        existingUser.setStatus(user.getStatus());
        existingUser.setUpdateTime(LocalDateTime.now());
        
        // 如果密码不为空，则更新密码
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existingUser.setPassword(DigestUtil.md5Hex(user.getPassword()));
        }
        
        return userRepository.save(existingUser);
    }
    
    /**
     * 删除用户
     */
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("用户不存在");
        }
        userRepository.deleteById(id);
    }
    
    /**
     * 批量删除用户
     */
    @Transactional
    public void batchDeleteUsers(List<Long> ids) {
        userRepository.deleteAllById(ids);
    }
    
    /**
     * 更新个人信息
     */
    @Transactional
    public User updateProfile(User user) {
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        // 只允许更新部分字段
        if (user.getUsername() != null && !user.getUsername().isEmpty()) {
            // 检查用户名是否被其他用户占用
            Optional<User> userWithSameUsername = userRepository.findByUsername(user.getUsername());
            if (userWithSameUsername.isPresent() && !userWithSameUsername.get().getId().equals(user.getId())) {
                throw new RuntimeException("用户名已被占用");
            }
            existingUser.setUsername(user.getUsername());
        }
        
        if (user.getEmail() != null) {
            existingUser.setEmail(user.getEmail());
        }
        
        if (user.getPhone() != null) {
            // 检查手机号是否被其他用户占用
            if (!user.getPhone().isEmpty()) {
                Optional<User> userWithSamePhone = userRepository.findByPhone(user.getPhone());
                if (userWithSamePhone.isPresent() && !userWithSamePhone.get().getId().equals(user.getId())) {
                    throw new RuntimeException("手机号已被占用");
                }
            }
            existingUser.setPhone(user.getPhone());
        }
        
        if (user.getAvatar() != null) {
            existingUser.setAvatar(user.getAvatar());
        }
        
        existingUser.setUpdateTime(LocalDateTime.now());
        
        return userRepository.save(existingUser);
    }
    
    /**
     * 修改密码
     */
    @Transactional
    public void changePassword(org.xingchang.brapi.dto.ChangePasswordRequest request) {
        User user = userRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        // 验证原密码
        String encryptedOldPassword = DigestUtil.md5Hex(request.getOldPassword());
        if (!encryptedOldPassword.equals(user.getPassword())) {
            throw new RuntimeException("原密码错误");
        }
        
        // 验证新密码长度
        if (request.getNewPassword().length() < 6) {
            throw new RuntimeException("新密码长度不能少于6位");
        }
        
        // 更新密码
        user.setPassword(DigestUtil.md5Hex(request.getNewPassword()));
        user.setUpdateTime(LocalDateTime.now());
        
        userRepository.save(user);
    }
}
