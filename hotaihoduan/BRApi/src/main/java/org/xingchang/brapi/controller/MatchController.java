package org.xingchang.brapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.xingchang.brapi.common.PageResult;
import org.xingchang.brapi.common.Result;
import org.xingchang.brapi.entity.MatchRecord;
import org.xingchang.brapi.service.MatchService;

import java.util.Map;

/**
 * 匹配Controller
 */
@Tag(name = "匹配管理", description = "失物匹配相关接口")
@RestController
@RequestMapping("/match")
public class MatchController {
    
    @Autowired
    private MatchService matchService;
    
    @Operation(summary = "匹配记录列表")
    @GetMapping("/list")
    public Result<PageResult<MatchRecord>> getMatchList(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status,
            @Parameter(description = "匹配类型") @RequestParam(required = false) String matchType,
            @Parameter(description = "用户ID") @RequestParam(required = false) Long userId) {
        try {
            PageResult<MatchRecord> result = matchService.getMatchList(page, size, status, matchType, userId);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @Operation(summary = "匹配详情")
    @GetMapping("/detail/{id}")
    public Result<MatchRecord> getMatchDetail(
            @Parameter(description = "匹配记录ID") @PathVariable Long id) {
        try {
            MatchRecord record = matchService.getMatchDetail(id);
            return Result.success(record);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @Operation(summary = "手动匹配")
    @PostMapping("/manual")
    public Result<MatchRecord> manualMatch(@RequestBody Map<String, Object> params) {
        try {
            Long lostItemId = Long.valueOf(params.get("lostItemId").toString());
            Long foundItemId = Long.valueOf(params.get("foundItemId").toString());
            Long userId = Long.valueOf(params.get("userId").toString());
            
            MatchRecord record = matchService.manualMatch(lostItemId, foundItemId, userId);
            return Result.success(record);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @Operation(summary = "确认匹配")
    @PutMapping("/confirm/{id}")
    public Result<Void> confirmMatch(
            @Parameter(description = "匹配记录ID") @PathVariable Long id,
            @RequestBody Map<String, Object> params) {
        try {
            Long userId = params.get("userId") != null ? Long.valueOf(params.get("userId").toString()) : null;
            matchService.confirmMatch(id, userId);
            return Result.success(null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @Operation(summary = "拒绝匹配")
    @PutMapping("/reject/{id}")
    public Result<Void> rejectMatch(
            @Parameter(description = "匹配记录ID") @PathVariable Long id) {
        try {
            matchService.rejectMatch(id);
            return Result.success(null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @Operation(summary = "提交反馈")
    @PostMapping("/feedback")
    public Result<Void> submitFeedback(@RequestBody Map<String, Object> params) {
        try {
            Long id = Long.valueOf(params.get("id").toString());
            String feedback = (String) params.get("feedback");
            Integer feedbackScore = (Integer) params.get("feedbackScore");
            
            matchService.submitFeedback(id, feedback, feedbackScore);
            return Result.success(null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
