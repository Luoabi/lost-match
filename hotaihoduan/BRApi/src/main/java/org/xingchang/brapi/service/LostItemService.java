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
import org.xingchang.brapi.entity.LostItem;
import org.xingchang.brapi.repository.LostItemRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 失物Service
 */
@Service
public class LostItemService {
    
    @Autowired
    private LostItemRepository lostItemRepository;
    
    @Autowired
    private MatchService matchService;
    
    /**
     * 获取失物列表（分页）
     */
    public PageResult<LostItem> getLostItemList(Integer page, Integer size, String keyword, 
                                                 String type, Integer status, Integer auditStatus, Long userId) {
        Specification<LostItem> spec = (root, query, cb) -> {
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
        Page<LostItem> pageData = lostItemRepository.findAll(spec, pageRequest);
        
        return new PageResult<>(
                pageData.getContent(),
                pageData.getTotalElements(),
                page,
                size
        );
    }
    
    /**
     * 获取失物详情
     */
    public LostItem getLostItemDetail(Long id) {
        LostItem item = lostItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("失物不存在"));
        
        // 增加浏览次数
        item.setViewCount(item.getViewCount() + 1);
        lostItemRepository.save(item);
        
        return item;
    }
    
    /**
     * 添加失物
     */
    @Transactional
    public LostItem addLostItem(LostItem item) {
        item.setCreateTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());
        item.setViewCount(0);
        item.setAuditStatus(0); // 待审核
        LostItem saved = lostItemRepository.save(item);
        
        // 触发自动匹配
        try {
            matchService.autoMatchForLostItem(saved.getId());
        } catch (Exception e) {
            // 匹配失败不影响失物发布
            System.err.println("自动匹配失败: " + e.getMessage());
        }
        
        return saved;
    }
    
    /**
     * 更新失物
     */
    @Transactional
    public LostItem updateLostItem(LostItem item) {
        LostItem existingItem = lostItemRepository.findById(item.getId())
                .orElseThrow(() -> new RuntimeException("失物不存在"));
        
        existingItem.setTitle(item.getTitle());
        existingItem.setType(item.getType());
        existingItem.setDescription(item.getDescription());
        existingItem.setImages(item.getImages());
        existingItem.setLocation(item.getLocation());
        existingItem.setLocationDetail(item.getLocationDetail());
        existingItem.setLongitude(item.getLongitude());
        existingItem.setLatitude(item.getLatitude());
        existingItem.setLostTime(item.getLostTime());
        existingItem.setContactName(item.getContactName());
        existingItem.setContactPhone(item.getContactPhone());
        existingItem.setReward(item.getReward());
        existingItem.setKeywords(item.getKeywords());
        existingItem.setUpdateTime(LocalDateTime.now());
        
        return lostItemRepository.save(existingItem);
    }
    
    /**
     * 删除失物
     */
    @Transactional
    public void deleteLostItem(Long id) {
        if (!lostItemRepository.existsById(id)) {
            throw new RuntimeException("失物不存在");
        }
        lostItemRepository.deleteById(id);
    }
    
    /**
     * 批量删除失物
     */
    @Transactional
    public void batchDeleteLostItems(List<Long> ids) {
        lostItemRepository.deleteAllById(ids);
    }
    
    /**
     * 更新失物状态
     */
    @Transactional
    public void updateStatus(Long id, Integer status) {
        LostItem item = lostItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("失物不存在"));
        item.setStatus(status);
        item.setUpdateTime(LocalDateTime.now());
        lostItemRepository.save(item);
    }
    
    /**
     * 批量审核
     */
    @Transactional
    public void batchAudit(List<Long> ids, Integer auditStatus, String auditRemark, Long auditorId) {
        List<LostItem> items = lostItemRepository.findAllById(ids);
        for (LostItem item : items) {
            item.setAuditStatus(auditStatus);
            item.setAuditRemark(auditRemark);
            item.setAuditorId(auditorId);
            item.setAuditTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
        }
        lostItemRepository.saveAll(items);
    }
}
