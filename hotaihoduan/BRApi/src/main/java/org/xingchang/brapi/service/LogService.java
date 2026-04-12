package org.xingchang.brapi.service;

import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xingchang.brapi.common.PageResult;
import org.xingchang.brapi.entity.OperationLog;
import org.xingchang.brapi.repository.OperationLogRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 日志Service
 */
@Service
public class LogService {
    
    @Autowired
    private OperationLogRepository operationLogRepository;
    
    /**
     * 获取日志列表
     */
    public PageResult<OperationLog> getLogList(Integer page, Integer size, String keyword, 
                                                String action, String module, String status,
                                                LocalDateTime startTime, LocalDateTime endTime) {
        Specification<OperationLog> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (keyword != null && !keyword.isEmpty()) {
                Predicate p1 = cb.like(root.get("username"), "%" + keyword + "%");
                Predicate p2 = cb.like(root.get("content"), "%" + keyword + "%");
                predicates.add(cb.or(p1, p2));
            }
            
            if (action != null && !action.isEmpty()) {
                predicates.add(cb.equal(root.get("action"), action));
            }
            
            if (module != null && !module.isEmpty()) {
                predicates.add(cb.equal(root.get("module"), module));
            }
            
            if (status != null && !status.isEmpty()) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            
            if (startTime != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createTime"), startTime));
            }
            
            if (endTime != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createTime"), endTime));
            }
            
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createTime"));
        Page<OperationLog> pageData = operationLogRepository.findAll(spec, pageRequest);
        
        return new PageResult<>(
                pageData.getContent(),
                pageData.getTotalElements(),
                page,
                size
        );
    }
    
    /**
     * 获取日志详情
     */
    public OperationLog getLogDetail(Long id) {
        return operationLogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("日志不存在"));
    }
    
    /**
     * 记录操作日志
     */
    @Transactional
    public void log(Long userId, String username, String action, String module, 
                    String content, String ip, String status, String params, String response) {
        OperationLog log = new OperationLog();
        log.setUserId(userId);
        log.setUsername(username);
        log.setAction(action);
        log.setModule(module);
        log.setContent(content);
        log.setIp(ip);
        log.setStatus(status);
        log.setParams(params);
        log.setResponse(response);
        log.setCreateTime(LocalDateTime.now());
        
        operationLogRepository.save(log);
    }
    
    /**
     * 清空日志
     */
    @Transactional
    public void clearLogs(LocalDateTime beforeTime) {
        if (beforeTime != null) {
            operationLogRepository.deleteByCreateTimeBefore(beforeTime);
        }
    }
}
