package org.xingchang.brapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.xingchang.brapi.common.Result;
import org.xingchang.brapi.entity.ItemType;
import org.xingchang.brapi.entity.Location;
import org.xingchang.brapi.entity.SystemConfig;
import org.xingchang.brapi.service.SettingsService;

import java.util.List;
import java.util.Map;

/**
 * 系统设置Controller
 */
@Tag(name = "系统设置", description = "系统配置相关接口")
@RestController
@RequestMapping("/settings")
public class SettingsController {
    
    @Autowired
    private SettingsService settingsService;
    
    // ========== 基础设置 ==========
    
    @Operation(summary = "获取基础设置")
    @GetMapping("/basic")
    public Result<Map<String, String>> getBasicSettings() {
        try {
            Map<String, String> settings = settingsService.getBasicSettings();
            return Result.success(settings);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @Operation(summary = "更新基础设置")
    @PutMapping("/basic")
    public Result<Void> updateBasicSettings(@RequestBody Map<String, String> settings) {
        try {
            settingsService.updateBasicSettings(settings);
            return Result.success(null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    // ========== 物品类型管理 ==========
    
    @Operation(summary = "物品类型列表")
    @GetMapping("/item-types")
    public Result<List<ItemType>> getItemTypes() {
        try {
            List<ItemType> types = settingsService.getItemTypes();
            return Result.success(types);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @Operation(summary = "添加物品类型")
    @PostMapping("/item-types")
    public Result<ItemType> addItemType(@RequestBody ItemType itemType) {
        try {
            ItemType newType = settingsService.addItemType(itemType);
            return Result.success(newType);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @Operation(summary = "更新物品类型")
    @PutMapping("/item-types/{id}")
    public Result<ItemType> updateItemType(
            @Parameter(description = "类型ID") @PathVariable Long id,
            @RequestBody ItemType itemType) {
        try {
            itemType.setId(id);
            ItemType updatedType = settingsService.updateItemType(itemType);
            return Result.success(updatedType);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @Operation(summary = "删除物品类型")
    @DeleteMapping("/item-types/{id}")
    public Result<Void> deleteItemType(
            @Parameter(description = "类型ID") @PathVariable Long id) {
        try {
            settingsService.deleteItemType(id);
            return Result.success(null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    // ========== 地点管理 ==========
    
    @Operation(summary = "地点列表")
    @GetMapping("/locations")
    public Result<List<Location>> getLocations() {
        try {
            List<Location> locations = settingsService.getLocations();
            return Result.success(locations);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @Operation(summary = "添加地点")
    @PostMapping("/locations")
    public Result<Location> addLocation(@RequestBody Location location) {
        try {
            Location newLocation = settingsService.addLocation(location);
            return Result.success(newLocation);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @Operation(summary = "更新地点")
    @PutMapping("/locations/{id}")
    public Result<Location> updateLocation(
            @Parameter(description = "地点ID") @PathVariable Long id,
            @RequestBody Location location) {
        try {
            location.setId(id);
            Location updatedLocation = settingsService.updateLocation(location);
            return Result.success(updatedLocation);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @Operation(summary = "删除地点")
    @DeleteMapping("/locations/{id}")
    public Result<Void> deleteLocation(
            @Parameter(description = "地点ID") @PathVariable Long id) {
        try {
            settingsService.deleteLocation(id);
            return Result.success(null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    // ========== 匹配算法配置 ==========
    
    @Operation(summary = "获取匹配算法配置")
    @GetMapping("/match-algorithm")
    public Result<Map<String, Object>> getMatchAlgorithmConfig() {
        try {
            Map<String, Object> config = settingsService.getMatchAlgorithmConfig();
            return Result.success(config);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @Operation(summary = "更新匹配算法配置")
    @PutMapping("/match-algorithm")
    public Result<Void> updateMatchAlgorithmConfig(@RequestBody Map<String, Object> config) {
        try {
            settingsService.updateMatchAlgorithmConfig(config);
            return Result.success(null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
