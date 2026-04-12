package org.xingchang.brapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.xingchang.brapi.common.Result;

import java.util.*;

/**
 * 统计Controller（示例）
 */
@Tag(name = "数据统计", description = "数据统计相关接口")
@RestController
@RequestMapping("/statistics")
public class StatisticsController {
    
    @Operation(summary = "统计概览", description = "获取系统整体统计数据")
    @GetMapping("/overview")
    public Result<Map<String, Object>> getOverview() {
        Map<String, Object> data = new HashMap<>();
        data.put("totalLost", 856);
        data.put("totalFound", 723);
        data.put("totalMatched", 542);
        data.put("recoveryRate", 73.5);
        data.put("todayLost", 12);
        data.put("todayFound", 15);
        data.put("todayMatched", 8);
        return Result.success(data);
    }
    
    @Operation(summary = "失物类型分布", description = "获取各类型失物的数量统计")
    @GetMapping("/item-type")
    public Result<List<Map<String, Object>>> getItemTypeDistribution() {
        List<Map<String, Object>> data = new ArrayList<>();
        
        String[] types = {"手机", "钱包", "钥匙", "书籍", "雨伞", "水杯", "耳机", "充电宝"};
        int[] counts = {156, 98, 145, 67, 189, 92, 78, 65};
        
        for (int i = 0; i < types.length; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("type", types[i]);
            item.put("count", counts[i]);
            data.add(item);
        }
        
        return Result.success(data);
    }
    
    @Operation(summary = "高频丢失区域", description = "获取失物高频区域统计")
    @GetMapping("/location")
    public Result<List<Map<String, Object>>> getLocationDistribution() {
        List<Map<String, Object>> data = new ArrayList<>();
        
        String[] locations = {"图书馆", "食堂", "教学楼A栋", "教学楼B栋", "宿舍楼", "操场", "体育馆", "实验楼"};
        int[] counts = {178, 134, 112, 98, 87, 65, 54, 43};
        
        for (int i = 0; i < locations.length; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("location", locations[i]);
            item.put("count", counts[i]);
            data.add(item);
        }
        
        return Result.success(data);
    }
    
    @Operation(summary = "找回率趋势", description = "获取按月统计的找回率趋势")
    @GetMapping("/recovery-trend")
    @Parameter(name = "months", description = "查询月份数", example = "6")
    public Result<List<Map<String, Object>>> getRecoveryTrend(
            @RequestParam(defaultValue = "6") Integer months) {
        List<Map<String, Object>> data = new ArrayList<>();
        
        String[] dates = {"2025-01", "2025-02", "2025-03", "2025-04", "2025-05", "2025-06"};
        double[] rates = {68.5, 70.2, 72.8, 71.5, 73.2, 74.8};
        
        int limit = Math.min(months, dates.length);
        for (int i = 0; i < limit; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("date", dates[i]);
            item.put("rate", rates[i]);
            data.add(item);
        }
        
        return Result.success(data);
    }
}
