package org.xingchang.brapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.xingchang.brapi.common.PageResult;
import org.xingchang.brapi.common.Result;
import org.xingchang.brapi.entity.LostItem;
import org.xingchang.brapi.service.LostItemService;

import java.util.List;
import java.util.Map;

/**
 * 失物Controller
 */
@Tag(name = "失物管理", description = "失物相关接口")
@RestController
@RequestMapping("/lost-item")
public class LostItemController {
    
    @Autowired
    private LostItemService lostItemService;
    
    @Operation(summary = "失物列表", description = "分页查询失物列表")
    @GetMapping("/list")
    public Result<PageResult<LostItem>> getLostItemList(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "搜索关键词") @RequestParam(required = false) String keyword,
            @Parameter(description = "物品类型") @RequestParam(required = false) String type,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status,
            @Parameter(description = "审核状态") @RequestParam(required = false) Integer auditStatus,
            @Parameter(description = "用户ID") @RequestParam(required = false) Long userId) {
        try {
            PageResult<LostItem> result = lostItemService.getLostItemList(page, size, keyword, type, status, auditStatus, userId);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @Operation(summary = "失物详情", description = "根据ID获取失物详细信息")
    @GetMapping("/detail/{id}")
    public Result<LostItem> getLostItemDetail(
            @Parameter(description = "失物ID") @PathVariable Long id) {
        try {
            LostItem item = lostItemService.getLostItemDetail(id);
            return Result.success(item);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @Operation(summary = "添加失物", description = "发布新的失物信息")
    @PostMapping("/add")
    public Result<LostItem> addLostItem(@RequestBody LostItem item) {
        try {
            // 调试日志
            System.out.println("接收到失物发布请求");
            System.out.println("标题: " + item.getTitle());
            System.out.println("类型: " + item.getType());
            System.out.println("图片数量: " + (item.getImages() != null ? item.getImages().size() : 0));
            System.out.println("图片列表: " + item.getImages());
            System.out.println("用户ID: " + item.getUserId());
            
            LostItem newItem = lostItemService.addLostItem(item);
            
            System.out.println("保存后的图片: " + newItem.getImages());
            
            return Result.success(newItem);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }
    
    @Operation(summary = "更新失物", description = "更新失物信息")
    @PutMapping("/update")
    public Result<LostItem> updateLostItem(@RequestBody LostItem item) {
        try {
            LostItem updatedItem = lostItemService.updateLostItem(item);
            return Result.success(updatedItem);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @Operation(summary = "删除失物", description = "根据ID删除失物")
    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteLostItem(
            @Parameter(description = "失物ID") @PathVariable Long id) {
        try {
            lostItemService.deleteLostItem(id);
            return Result.success(null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @Operation(summary = "批量删除失物", description = "批量删除多个失物")
    @DeleteMapping("/batch-delete")
    public Result<Void> batchDeleteLostItems(@RequestBody List<Long> ids) {
        try {
            lostItemService.batchDeleteLostItems(ids);
            return Result.success(null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @Operation(summary = "更新失物状态", description = "更新失物的找回状态")
    @PutMapping("/status/{id}")
    public Result<Void> updateStatus(
            @Parameter(description = "失物ID") @PathVariable Long id,
            @Parameter(description = "状态") @RequestParam Integer status) {
        try {
            lostItemService.updateStatus(id, status);
            return Result.success(null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @Operation(summary = "批量审核", description = "批量审核失物信息")
    @PostMapping("/batch-audit")
    public Result<Void> batchAudit(@RequestBody Map<String, Object> params) {
        try {
            @SuppressWarnings("unchecked")
            List<Long> ids = (List<Long>) params.get("ids");
            if (ids == null || ids.isEmpty()) {
                return Result.error("请选择要审核的记录");
            }
            
            Object auditStatusObj = params.get("auditStatus");
            if (auditStatusObj == null) {
                return Result.error("审核状态不能为空");
            }
            Integer auditStatus = Integer.valueOf(auditStatusObj.toString());
            
            String auditRemark = params.get("auditRemark") != null ? params.get("auditRemark").toString() : "";
            
            Object auditorIdObj = params.get("auditorId");
            if (auditorIdObj == null) {
                return Result.error("审核人ID不能为空");
            }
            Long auditorId = Long.valueOf(auditorIdObj.toString());
            
            lostItemService.batchAudit(ids, auditStatus, auditRemark, auditorId);
            return Result.success(null);
        } catch (NumberFormatException e) {
            return Result.error("参数格式错误");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }
}
