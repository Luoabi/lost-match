package org.xingchang.brapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.xingchang.brapi.common.PageResult;
import org.xingchang.brapi.common.Result;
import org.xingchang.brapi.entity.FoundItem;
import org.xingchang.brapi.service.FoundItemService;

import java.util.List;
import java.util.Map;

/**
 * 拾获Controller
 */
@Tag(name = "拾获管理", description = "拾获物品相关接口")
@RestController
@RequestMapping("/found-item")
public class FoundItemController {
    
    @Autowired
    private FoundItemService foundItemService;
    
    @Operation(summary = "拾获列表")
    @GetMapping("/list")
    public Result<PageResult<FoundItem>> getFoundItemList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Integer auditStatus,
            @RequestParam(required = false) Long userId) {
        try {
            PageResult<FoundItem> result = foundItemService.getFoundItemList(page, size, keyword, type, status, auditStatus, userId);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @Operation(summary = "拾获详情")
    @GetMapping("/detail/{id}")
    public Result<FoundItem> getFoundItemDetail(@PathVariable Long id) {
        try {
            FoundItem item = foundItemService.getFoundItemDetail(id);
            return Result.success(item);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @Operation(summary = "添加拾获")
    @PostMapping("/add")
    public Result<FoundItem> addFoundItem(@RequestBody FoundItem item) {
        try {
            // 调试日志
            System.out.println("接收到拾获发布请求");
            System.out.println("标题: " + item.getTitle());
            System.out.println("类型: " + item.getType());
            System.out.println("图片数量: " + (item.getImages() != null ? item.getImages().size() : 0));
            System.out.println("图片列表: " + item.getImages());
            System.out.println("用户ID: " + item.getUserId());
            
            FoundItem newItem = foundItemService.addFoundItem(item);
            
            System.out.println("保存后的图片: " + newItem.getImages());
            
            return Result.success(newItem);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }
    
    @Operation(summary = "更新拾获")
    @PutMapping("/update")
    public Result<FoundItem> updateFoundItem(@RequestBody FoundItem item) {
        try {
            FoundItem updatedItem = foundItemService.updateFoundItem(item);
            return Result.success(updatedItem);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @Operation(summary = "删除拾获")
    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteFoundItem(@PathVariable Long id) {
        try {
            foundItemService.deleteFoundItem(id);
            return Result.success(null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @Operation(summary = "批量删除拾获")
    @DeleteMapping("/batch-delete")
    public Result<Void> batchDeleteFoundItems(@RequestBody List<Long> ids) {
        try {
            foundItemService.batchDeleteFoundItems(ids);
            return Result.success(null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @Operation(summary = "更新拾获状态")
    @PutMapping("/status/{id}")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        try {
            foundItemService.updateStatus(id, status);
            return Result.success(null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @Operation(summary = "批量审核")
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
            
            foundItemService.batchAudit(ids, auditStatus, auditRemark, auditorId);
            return Result.success(null);
        } catch (NumberFormatException e) {
            return Result.error("参数格式错误");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }
}
