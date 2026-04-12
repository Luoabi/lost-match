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
import org.xingchang.brapi.entity.MatchRecord;
import org.xingchang.brapi.repository.FoundItemRepository;
import org.xingchang.brapi.repository.LostItemRepository;
import org.xingchang.brapi.repository.MatchRecordRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 匹配Service
 */
@Service
public class MatchService {
    
    @Autowired
    private MatchRecordRepository matchRecordRepository;
    
    @Autowired
    private LostItemRepository lostItemRepository;
    
    @Autowired
    private FoundItemRepository foundItemRepository;
    
    /**
     * 获取匹配记录列表
     */
    public PageResult<MatchRecord> getMatchList(Integer page, Integer size, Integer status, String matchType, Long userId) {
        Specification<MatchRecord> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            
            if (matchType != null && !matchType.isEmpty()) {
                predicates.add(cb.equal(root.get("matchType"), matchType));
            }
            
            // 如果指定了userId，查询与该用户相关的匹配记录
            if (userId != null) {
                // 获取用户的失物ID列表
                List<Long> userLostItemIds = lostItemRepository.findAll((lostRoot, lostQuery, lostCb) -> 
                    lostCb.equal(lostRoot.get("userId"), userId)
                ).stream().map(LostItem::getId).toList();
                
                // 获取用户的拾获物ID列表
                List<Long> userFoundItemIds = foundItemRepository.findAll((foundRoot, foundQuery, foundCb) -> 
                    foundCb.equal(foundRoot.get("userId"), userId)
                ).stream().map(FoundItem::getId).toList();
                
                // 匹配记录的失物或拾获物属于该用户
                List<Predicate> userPredicates = new ArrayList<>();
                if (!userLostItemIds.isEmpty()) {
                    userPredicates.add(root.get("lostItemId").in(userLostItemIds));
                }
                if (!userFoundItemIds.isEmpty()) {
                    userPredicates.add(root.get("foundItemId").in(userFoundItemIds));
                }
                
                if (!userPredicates.isEmpty()) {
                    predicates.add(cb.or(userPredicates.toArray(new Predicate[0])));
                } else {
                    // 如果用户没有任何失物或拾获物，返回空结果
                    predicates.add(cb.disjunction());
                }
            }
            
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createTime"));
        Page<MatchRecord> pageData = matchRecordRepository.findAll(spec, pageRequest);
        
        // 填充失物和拾获物的详细信息
        List<MatchRecord> enrichedList = pageData.getContent().stream().map(record -> {
            // 获取失物信息
            lostItemRepository.findById(record.getLostItemId()).ifPresent(lostItem -> {
                record.setLostItemTitle(lostItem.getTitle());
                // 设置失物的第一张图片
                if (lostItem.getImages() != null && !lostItem.getImages().isEmpty()) {
                    record.setLostItemImage(lostItem.getImages().get(0));
                }
                record.setLostItemLocation(lostItem.getLocation());
            });
            
            // 获取拾获物信息
            foundItemRepository.findById(record.getFoundItemId()).ifPresent(foundItem -> {
                record.setFoundItemTitle(foundItem.getTitle());
                // 设置拾获物的第一张图片
                if (foundItem.getImages() != null && !foundItem.getImages().isEmpty()) {
                    record.setFoundItemImage(foundItem.getImages().get(0));
                }
                record.setFoundItemLocation(foundItem.getLocation());
            });
            
            return record;
        }).toList();
        
        return new PageResult<>(
                enrichedList,
                pageData.getTotalElements(),
                page,
                size
        );
    }
    
    /**
     * 获取匹配详情
     */
    public MatchRecord getMatchDetail(Long id) {
        MatchRecord record = matchRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("匹配记录不存在"));
        
        // 填充失物详细信息
        lostItemRepository.findById(record.getLostItemId()).ifPresent(lostItem -> {
            record.setLostItemTitle(lostItem.getTitle());
            record.setLostItemImages(lostItem.getImages());
            record.setLostItemImage(lostItem.getImages() != null && !lostItem.getImages().isEmpty() 
                ? lostItem.getImages().get(0) : null);
            record.setLostItemLocation(lostItem.getLocation());
            record.setLostItemType(lostItem.getType());
            record.setLostItemDescription(lostItem.getDescription());
            record.setLostItemTime(lostItem.getLostTime());
        });
        
        // 填充拾获物详细信息
        foundItemRepository.findById(record.getFoundItemId()).ifPresent(foundItem -> {
            record.setFoundItemTitle(foundItem.getTitle());
            record.setFoundItemImages(foundItem.getImages());
            record.setFoundItemImage(foundItem.getImages() != null && !foundItem.getImages().isEmpty() 
                ? foundItem.getImages().get(0) : null);
            record.setFoundItemLocation(foundItem.getLocation());
            record.setFoundItemType(foundItem.getType());
            record.setFoundItemDescription(foundItem.getDescription());
            record.setFoundItemTime(foundItem.getFoundTime());
        });
        
        return record;
    }
    
    /**
     * 手动匹配
     */
    @Transactional
    public MatchRecord manualMatch(Long lostItemId, Long foundItemId, Long userId) {
        // 验证失物和拾获物是否存在
        LostItem lostItem = lostItemRepository.findById(lostItemId)
                .orElseThrow(() -> new RuntimeException("失物不存在"));
        FoundItem foundItem = foundItemRepository.findById(foundItemId)
                .orElseThrow(() -> new RuntimeException("拾获物不存在"));
        
        // 创建匹配记录
        MatchRecord record = new MatchRecord();
        record.setLostItemId(lostItemId);
        record.setFoundItemId(foundItemId);
        record.setMatchType("manual");
        record.setStatus(0); // 待确认
        
        // 简单的匹配得分计算（实际应该更复杂）
        double imageScore = calculateImageScore(lostItem, foundItem);
        double locationScore = calculateLocationScore(lostItem, foundItem);
        double textScore = calculateTextScore(lostItem, foundItem);
        
        record.setImageScore(BigDecimal.valueOf(imageScore));
        record.setLocationScore(BigDecimal.valueOf(locationScore));
        record.setTextScore(BigDecimal.valueOf(textScore));
        record.setMatchScore(BigDecimal.valueOf(imageScore * 0.4 + locationScore * 0.3 + textScore * 0.3));
        
        record.setCreateTime(LocalDateTime.now());
        
        return matchRecordRepository.save(record);
    }
    
    /**
     * 确认匹配
     */
    @Transactional
    public void confirmMatch(Long id, Long userId) {
        MatchRecord record = matchRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("匹配记录不存在"));
        
        record.setStatus(1); // 已确认
        record.setConfirmUserId(userId);
        record.setConfirmTime(LocalDateTime.now());
        
        matchRecordRepository.save(record);
        
        // 更新失物和拾获物状态
        LostItem lostItem = lostItemRepository.findById(record.getLostItemId()).orElse(null);
        if (lostItem != null) {
            lostItem.setStatus(1); // 已找回
            lostItemRepository.save(lostItem);
        }
        
        FoundItem foundItem = foundItemRepository.findById(record.getFoundItemId()).orElse(null);
        if (foundItem != null) {
            foundItem.setStatus(1); // 已归还
            foundItemRepository.save(foundItem);
        }
    }
    
    /**
     * 拒绝匹配
     */
    @Transactional
    public void rejectMatch(Long id) {
        MatchRecord record = matchRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("匹配记录不存在"));
        
        record.setStatus(2); // 不匹配
        matchRecordRepository.save(record);
    }
    
    /**
     * 提交反馈
     */
    @Transactional
    public void submitFeedback(Long id, String feedback, Integer feedbackScore) {
        MatchRecord record = matchRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("匹配记录不存在"));
        
        record.setFeedback(feedback);
        record.setFeedbackScore(feedbackScore);
        matchRecordRepository.save(record);
    }
    
    // ========== 私有方法：匹配得分计算 ==========
    
    /**
     * 为失物自动匹配拾获物
     */
    @Transactional
    public void autoMatchForLostItem(Long lostItemId) {
        LostItem lostItem = lostItemRepository.findById(lostItemId)
                .orElseThrow(() -> new RuntimeException("失物不存在"));
        
        // 查找所有未归还的拾获物（状态为0）
        List<FoundItem> foundItems = foundItemRepository.findAll((root, query, cb) -> 
            cb.equal(root.get("status"), 0)
        );
        
        for (FoundItem foundItem : foundItems) {
            // 检查是否已存在匹配记录
            boolean exists = matchRecordRepository.exists((root, query, cb) -> 
                cb.and(
                    cb.equal(root.get("lostItemId"), lostItemId),
                    cb.equal(root.get("foundItemId"), foundItem.getId())
                )
            );
            
            if (exists) {
                continue; // 跳过已存在的匹配
            }
            
            // 计算匹配分数
            double matchScore = calculateSmartMatchScore(lostItem, foundItem);
            
            // 如果匹配度超过阈值（60%），创建匹配记录
            if (matchScore >= 0.6) {
                createAutoMatchRecord(lostItem, foundItem, matchScore);
            }
        }
    }
    
    /**
     * 为拾获物自动匹配失物
     */
    @Transactional
    public void autoMatchForFoundItem(Long foundItemId) {
        FoundItem foundItem = foundItemRepository.findById(foundItemId)
                .orElseThrow(() -> new RuntimeException("拾获物不存在"));
        
        // 查找所有未找回的失物（状态为0）
        List<LostItem> lostItems = lostItemRepository.findAll((root, query, cb) -> 
            cb.equal(root.get("status"), 0)
        );
        
        for (LostItem lostItem : lostItems) {
            // 检查是否已存在匹配记录
            boolean exists = matchRecordRepository.exists((root, query, cb) -> 
                cb.and(
                    cb.equal(root.get("lostItemId"), lostItem.getId()),
                    cb.equal(root.get("foundItemId"), foundItemId)
                )
            );
            
            if (exists) {
                continue; // 跳过已存在的匹配
            }
            
            // 计算匹配分数
            double matchScore = calculateSmartMatchScore(lostItem, foundItem);
            
            // 如果匹配度超过阈值（60%），创建匹配记录
            if (matchScore >= 0.6) {
                createAutoMatchRecord(lostItem, foundItem, matchScore);
            }
        }
    }
    
    /**
     * 创建自动匹配记录
     */
    private void createAutoMatchRecord(LostItem lostItem, FoundItem foundItem, double matchScore) {
        MatchRecord record = new MatchRecord();
        record.setLostItemId(lostItem.getId());
        record.setFoundItemId(foundItem.getId());
        record.setMatchType("auto");
        record.setStatus(0); // 待确认
        
        // 计算各维度分数
        double typeScore = calculateTypeScore(lostItem.getType(), foundItem.getType());
        double locationScore = calculateSmartLocationScore(
            lostItem.getLocation(), lostItem.getLongitude(), lostItem.getLatitude(),
            foundItem.getLocation(), foundItem.getLongitude(), foundItem.getLatitude()
        );
        double timeScore = calculateTimeScore(lostItem.getLostTime(), foundItem.getFoundTime());
        double keywordScore = calculateKeywordScore(lostItem.getKeywords(), foundItem.getKeywords());
        
        record.setImageScore(BigDecimal.valueOf(0.5)); // 暂时使用默认值，后续可接入图像识别
        record.setLocationScore(BigDecimal.valueOf(locationScore));
        record.setTextScore(BigDecimal.valueOf((typeScore + keywordScore) / 2));
        record.setMatchScore(BigDecimal.valueOf(matchScore));
        
        matchRecordRepository.save(record);
    }
    
    /**
     * 智能计算综合匹配分数
     */
    private double calculateSmartMatchScore(LostItem lostItem, FoundItem foundItem) {
        // 1. 类型匹配（30%权重）
        double typeScore = calculateTypeScore(lostItem.getType(), foundItem.getType());
        
        // 2. 位置匹配（30%权重）
        double locationScore = calculateSmartLocationScore(
            lostItem.getLocation(), lostItem.getLongitude(), lostItem.getLatitude(),
            foundItem.getLocation(), foundItem.getLongitude(), foundItem.getLatitude()
        );
        
        // 3. 时间匹配（20%权重）
        double timeScore = calculateTimeScore(lostItem.getLostTime(), foundItem.getFoundTime());
        
        // 4. 关键词匹配（20%权重）
        double keywordScore = calculateKeywordScore(lostItem.getKeywords(), foundItem.getKeywords());
        
        return typeScore * 0.3 + locationScore * 0.3 + timeScore * 0.2 + keywordScore * 0.2;
    }
    
    /**
     * 计算类型匹配分数
     */
    private double calculateTypeScore(String type1, String type2) {
        if (type1 == null || type2 == null) return 0.0;
        return type1.equals(type2) ? 1.0 : 0.0;
    }
    
    /**
     * 计算智能位置匹配分数（考虑经纬度）
     */
    private double calculateSmartLocationScore(
        String loc1, BigDecimal lon1, BigDecimal lat1,
        String loc2, BigDecimal lon2, BigDecimal lat2
    ) {
        // 如果大致位置相同，得分1.0
        if (loc1 != null && loc2 != null && loc1.equals(loc2)) {
            return 1.0;
        }
        
        // 如果有经纬度，计算实际距离
        if (lon1 != null && lat1 != null && lon2 != null && lat2 != null) {
            double distance = calculateDistance(
                lat1.doubleValue(), lon1.doubleValue(),
                lat2.doubleValue(), lon2.doubleValue()
            );
            
            // 距离越近分数越高（500米内满分，2000米外0分）
            if (distance <= 500) return 1.0;
            if (distance >= 2000) return 0.0;
            return 1.0 - (distance - 500) / 1500;
        }
        
        return 0.3; // 默认分数
    }
    
    /**
     * 计算两点间距离（米）- Haversine公式
     */
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371000; // 地球半径（米）
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
    
    /**
     * 计算时间匹配分数
     */
    private double calculateTimeScore(LocalDateTime lostTime, LocalDateTime foundTime) {
        if (lostTime == null || foundTime == null) return 0.5;
        
        // 拾获时间应该在丢失时间之后
        if (foundTime.isBefore(lostTime)) return 0.0;
        
        // 计算时间差（小时）
        long hoursDiff = java.time.Duration.between(lostTime, foundTime).toHours();
        
        // 24小时内满分，7天（168小时）外0分
        if (hoursDiff <= 24) return 1.0;
        if (hoursDiff >= 168) return 0.0;
        return 1.0 - (hoursDiff - 24) / 144.0;
    }
    
    /**
     * 计算关键词匹配分数
     */
    private double calculateKeywordScore(List<String> keywords1, List<String> keywords2) {
        if (keywords1 == null || keywords2 == null || 
            keywords1.isEmpty() || keywords2.isEmpty()) {
            return 0.5;
        }
        
        // 计算Jaccard相似度（交集/并集）
        java.util.Set<String> set1 = new java.util.HashSet<>(keywords1);
        java.util.Set<String> set2 = new java.util.HashSet<>(keywords2);
        
        java.util.Set<String> intersection = new java.util.HashSet<>(set1);
        intersection.retainAll(set2);
        
        java.util.Set<String> union = new java.util.HashSet<>(set1);
        union.addAll(set2);
        
        return union.size() > 0 ? (double) intersection.size() / union.size() : 0.0;
    }
    
    // ========== 原有的简化版本（保留用于手动匹配） ==========
    
    private double calculateImageScore(LostItem lostItem, FoundItem foundItem) {
        // 简化版本：如果都有图片，返回0.8，否则返回0.5
        if (lostItem.getImages() != null && foundItem.getImages() != null) {
            return 0.8;
        }
        return 0.5;
    }
    
    private double calculateLocationScore(LostItem lostItem, FoundItem foundItem) {
        // 简化版本：如果位置相同，返回1.0，否则返回0.3
        if (lostItem.getLocation() != null && lostItem.getLocation().equals(foundItem.getLocation())) {
            return 1.0;
        }
        return 0.3;
    }
    
    private double calculateTextScore(LostItem lostItem, FoundItem foundItem) {
        // 简化版本：如果类型相同，返回0.9，否则返回0.2
        if (lostItem.getType() != null && lostItem.getType().equals(foundItem.getType())) {
            return 0.9;
        }
        return 0.2;
    }
}
