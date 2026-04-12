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
import org.xingchang.brapi.entity.FoundItem;
import org.xingchang.brapi.repository.FoundItemRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 拾获Service
 */
@Service
public class FoundItemService {
    
    @Autowired
    private FoundItemRepository foundItemRepository;
    
    @Autowired
    private MatchService matchService;
    
    public PageResult<FoundItem> getFoundItemList(Integer page, Integer size, String keyword, 
                                                   String type, Integer status, Integer auditStatus, Long userId) {
        Specification<FoundItem> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (keyword != null && !keyword.isEmpty()) {
                Predicate p1 = cb.like(root.get("title"), "%" + keyword + "%");
                Predicate p2 = cb.like(root.get("description"), "%" + keyword + "%");
                predicates.add(cb.or(p1, p2));
            }
            
            if (type != null && !type.isEmpty()) {
                predicates.add(cb.equal(root.get("type"), type));
            }
            
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            
            if (auditStatus != null) {
                predicates.add(cb.equal(root.get("auditStatus"), auditStatus));
            }
            
            if (userId != null) {
                predicates.add(cb.equal(root.get("userId"), userId));
            }
            
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createTime"));
        Page<FoundItem> pageData = foundItemRepository.findAll(spec, pageRequest);
        
        return new PageResult<>(
                pageData.getContent(),
                pageData.getTotalElements(),
                page,
                size
        );
    }
    
    public FoundItem getFoundItemDetail(Long id) {
        FoundItem item = foundItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("拾获物不存在"));
        item.setViewCount(item.getViewCount() + 1);
        foundItemRepository.save(item);
        return item;
    }
    
    @Transactional
    public FoundItem addFoundItem(FoundItem item) {
        item.setCreateTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());
        item.setViewCount(0);
        item.setAuditStatus(0);
        FoundItem saved = foundItemRepository.save(item);
        
        // 触发自动匹配
        try {
            matchService.autoMatchForFoundItem(saved.getId());
        } catch (Exception e) {
            // 匹配失败不影响拾获物发布
            System.err.println("自动匹配失败: " + e.getMessage());
        }
        
        return saved;
    }
    
    @Transactional
    public FoundItem updateFoundItem(FoundItem item) {
        FoundItem existingItem = foundItemRepository.findById(item.getId())
                .orElseThrow(() -> new RuntimeException("拾获物不存在"));
        
        existingItem.setTitle(item.getTitle());
        existingItem.setType(item.getType());
        existingItem.setDescription(item.getDescription());
        existingItem.setImages(item.getImages());
        existingItem.setLocation(item.getLocation());
        existingItem.setLocationDetail(item.getLocationDetail());
        existingItem.setLongitude(item.getLongitude());
        existingItem.setLatitude(item.getLatitude());
        existingItem.setFoundTime(item.getFoundTime());
        existingItem.setContactName(item.getContactName());
        existingItem.setContactPhone(item.getContactPhone());
        existingItem.setKeywords(item.getKeywords());
        existingItem.setUpdateTime(LocalDateTime.now());
        
        return foundItemRepository.save(existingItem);
    }
    
    @Transactional
    public void deleteFoundItem(Long id) {
        if (!foundItemRepository.existsById(id)) {
            throw new RuntimeException("拾获物不存在");
        }
        foundItemRepository.deleteById(id);
    }
    
    @Transactional
    public void batchDeleteFoundItems(List<Long> ids) {
        foundItemRepository.deleteAllById(ids);
    }
    
    @Transactional
    public void updateStatus(Long id, Integer status) {
        FoundItem item = foundItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("拾获物不存在"));
        item.setStatus(status);
        item.setUpdateTime(LocalDateTime.now());
        foundItemRepository.save(item);
    }
    
    @Transactional
    public void batchAudit(List<Long> ids, Integer auditStatus, String auditRemark, Long auditorId) {
        List<FoundItem> items = foundItemRepository.findAllById(ids);
        for (FoundItem item : items) {
            item.setAuditStatus(auditStatus);
            item.setAuditRemark(auditRemark);
            item.setAuditorId(auditorId);
            item.setAuditTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
        }
        foundItemRepository.saveAll(items);
    }
}
