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
import org.xingchang.brapi.entity.LostItem;
import org.xingchang.brapi.repository.FoundItemRepository;
import org.xingchang.brapi.repository.LostItemRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 审核Service
 */
@Service
public class AuditService {
    
    @Autowired
    private LostItemRepository lostItemRepository;
    
    @Autowired
    private FoundItemRepository foundItemRepository;
    
    /**
     * 获取审核列表（包含失物和拾获）
     */
    public PageResult<Map<String, Object>> getAuditList(Integer page, Integer size, 
                                                         Integer auditStatus, String type) {
        System.out.println("=== AuditService.getAuditList ===");
        System.out.println("参数 - page: " + page + ", size: " + size + ", auditStatus: " + auditStatus + ", type: " + type);
        
        List<Map<String, Object>> allItems = new ArrayList<>();
        
        // 先查询所有失物（不带过滤条件）看看数据库里有没有数据
        List<LostItem> allLostItems = lostItemRepository.findAll();
        System.out.println("数据库中失物总数（不带过滤）: " + allLostItems.size());
        for (LostItem item : allLostItems) {
            System.out.println("  失物ID: " + item.getId() + ", 标题: " + item.getTitle() + ", 审核状态: " + item.getAuditStatus());
        }
        
        // 查询失物
        if (type == null || type.isEmpty() || "lost".equals(type)) {
            Specification<LostItem> lostSpec = (root, query, cb) -> {
                List<Predicate> predicates = new ArrayList<>();
                if (auditStatus != null) {
                    predicates.add(cb.equal(root.get("auditStatus"), auditStatus));
                }
                return cb.and(predicates.toArray(new Predicate[0]));
            };
            
            List<LostItem> lostItems = lostItemRepository.findAll(lostSpec, 
                Sort.by(Sort.Direction.DESC, "createTime"));
            System.out.println("查询到失物数量（带过滤 auditStatus=" + auditStatus + "）: " + lostItems.size());
            for (LostItem item : lostItems) {
                System.out.println("  失物ID: " + item.getId() + ", 标题: " + item.getTitle() + ", 审核状态: " + item.getAuditStatus());
                Map<String, Object> map = new HashMap<>();
                map.put("id", item.getId());
                map.put("itemType", "lost"); // 用于区分失物/拾获
                map.put("title", item.getTitle());
                map.put("type", item.getType()); // 物品类型（证件、电子产品等）
                map.put("description", item.getDescription());
                map.put("images", item.getImages() != null ? item.getImages() : new ArrayList<>());
                map.put("location", item.getLocation());
                map.put("auditStatus", item.getAuditStatus());
                map.put("auditStatusText", getAuditStatusText(item.getAuditStatus()));
                map.put("auditRemark", item.getAuditRemark());
                map.put("auditTime", item.getAuditTime());
                map.put("createTime", item.getCreateTime());
                map.put("userId", item.getUserId());
                map.put("userName", "用户" + item.getUserId()); // 简化处理，实际应该关联用户表
                map.put("userAvatar", ""); // 简化处理
                allItems.add(map);
            }
        }
        
        // 先查询所有拾获（不带过滤条件）
        List<FoundItem> allFoundItems = foundItemRepository.findAll();
        System.out.println("数据库中拾获总数（不带过滤）: " + allFoundItems.size());
        for (FoundItem item : allFoundItems) {
            System.out.println("  拾获ID: " + item.getId() + ", 标题: " + item.getTitle() + ", 审核状态: " + item.getAuditStatus());
        }
        
        // 查询拾获
        if (type == null || type.isEmpty() || "found".equals(type)) {
            Specification<FoundItem> foundSpec = (root, query, cb) -> {
                List<Predicate> predicates = new ArrayList<>();
                if (auditStatus != null) {
                    predicates.add(cb.equal(root.get("auditStatus"), auditStatus));
                }
                return cb.and(predicates.toArray(new Predicate[0]));
            };
            
            List<FoundItem> foundItems = foundItemRepository.findAll(foundSpec,
                Sort.by(Sort.Direction.DESC, "createTime"));
            System.out.println("查询到拾获数量（带过滤 auditStatus=" + auditStatus + "）: " + foundItems.size());
            for (FoundItem item : foundItems) {
                System.out.println("  拾获ID: " + item.getId() + ", 标题: " + item.getTitle() + ", 审核状态: " + item.getAuditStatus());
                Map<String, Object> map = new HashMap<>();
                map.put("id", item.getId());
                map.put("itemType", "found"); // 用于区分失物/拾获
                map.put("title", item.getTitle());
                map.put("type", item.getType()); // 物品类型（证件、电子产品等）
                map.put("description", item.getDescription());
                map.put("images", item.getImages() != null ? item.getImages() : new ArrayList<>());
                map.put("location", item.getLocation());
                map.put("auditStatus", item.getAuditStatus());
                map.put("auditStatusText", getAuditStatusText(item.getAuditStatus()));
                map.put("auditRemark", item.getAuditRemark());
                map.put("auditTime", item.getAuditTime());
                map.put("createTime", item.getCreateTime());
                map.put("userId", item.getUserId());
                map.put("userName", "用户" + item.getUserId()); // 简化处理
                map.put("userAvatar", ""); // 简化处理
                allItems.add(map);
            }
        }
        
        // 按创建时间倒序排序
        allItems.sort((a, b) -> {
            LocalDateTime timeA = (LocalDateTime) a.get("createTime");
            LocalDateTime timeB = (LocalDateTime) b.get("createTime");
            return timeB.compareTo(timeA);
        });
        
        System.out.println("合并后总数量: " + allItems.size());
        
        // 手动分页
        int start = (page - 1) * size;
        int end = Math.min(start + size, allItems.size());
        List<Map<String, Object>> pageItems = start < allItems.size() ? 
            allItems.subList(start, end) : new ArrayList<>();
        
        System.out.println("分页后数量: " + pageItems.size() + " (start: " + start + ", end: " + end + ")");
        
        return new PageResult<>(
                pageItems,
                (long) allItems.size(),
                page,
                size
        );
    }
    
    /**
     * 获取审核状态文本
     */
    private String getAuditStatusText(Integer status) {
        if (status == null) return "未知";
        switch (status) {
            case 0: return "待审核";
            case 1: return "已通过";
            case 2: return "已拒绝";
            default: return "未知";
        }
    }
    
    /**
     * 审核通过
     */
    @Transactional
    public void approve(String type, Long id, String remark, Long auditorId) {
        if ("lost".equals(type)) {
            LostItem item = lostItemRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("失物不存在"));
            item.setAuditStatus(1);
            item.setAuditRemark(remark);
            item.setAuditorId(auditorId);
            item.setAuditTime(LocalDateTime.now());
            lostItemRepository.save(item);
        } else if ("found".equals(type)) {
            FoundItem item = foundItemRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("拾获物不存在"));
            item.setAuditStatus(1);
            item.setAuditRemark(remark);
            item.setAuditorId(auditorId);
            item.setAuditTime(LocalDateTime.now());
            foundItemRepository.save(item);
        }
    }
    
    /**
     * 审核拒绝
     */
    @Transactional
    public void reject(String type, Long id, String remark, Long auditorId) {
        if ("lost".equals(type)) {
            LostItem item = lostItemRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("失物不存在"));
            item.setAuditStatus(2);
            item.setAuditRemark(remark);
            item.setAuditorId(auditorId);
            item.setAuditTime(LocalDateTime.now());
            lostItemRepository.save(item);
        } else if ("found".equals(type)) {
            FoundItem item = foundItemRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("拾获物不存在"));
            item.setAuditStatus(2);
            item.setAuditRemark(remark);
            item.setAuditorId(auditorId);
            item.setAuditTime(LocalDateTime.now());
            foundItemRepository.save(item);
        }
    }
}
