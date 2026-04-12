package org.xingchang.brapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.xingchang.brapi.common.PageResult;
import org.xingchang.brapi.common.Result;
import org.xingchang.brapi.service.AuditService;

import java.util.Map;

/**
 * 审核Controller
 */
@Tag(name = "审核管理", description = "内容审核相关接口")
@RestController
@RequestMapping("/audit")
public class AuditController {
    
    @Autowired
    private AuditService auditService;
    
    @Operation(summary = "审核列表", description = "获取待审核、已通过、已拒绝的内容列表")
    @GetMapping("/list")
    public Result<PageResult<Map<String, Object>>> getAuditList(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "审核状态：0-待审核，1-已通过，2-已拒绝") @RequestParam(required = false) Integer auditStatus,
            @Parameter(description = "类型：lost-失物，found-拾获") @RequestParam(required = false) String type) {
        try {
            System.out.println("=== AuditController.getAuditList ===");
            System.out.println("接收参数 - page: " + page + ", size: " + size + ", auditStatus: " + auditStatus + ", type: " + type);
            PageResult<Map<String, Object>> result = auditService.getAuditList(page, size, auditStatus, type);
            System.out.println("返回结果 - total: " + result.getTotal() + ", list size: " + result.getList().size());
            return Result.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }
    
    @Operation(summary = "审核通过", description = "审核通过失物或拾获信息")
    @PostMapping("/approve")
    public Result<Void> approve(@RequestBody Map<String, Object> params) {
        try {
            String type = (String) params.get("type");
            if (type == null || type.isEmpty()) {
                return Result.error("类型不能为空");
            }
            
            Object idObj = params.get("id");
            if (idObj == null) {
                return Result.error("ID不能为空");
            }
            Long id = Long.valueOf(idObj.toString());
            
            String remark = params.get("remark") != null ? params.get("remark").toString() : "";
            
            Object auditorIdObj = params.get("auditorId");
            if (auditorIdObj == null) {
                return Result.error("审核人ID不能为空");
            }
            Long auditorId = Long.valueOf(auditorIdObj.toString());
            
            auditService.approve(type, id, remark, auditorId);
            return Result.success(null);
        } catch (NumberFormatException e) {
            return Result.error("ID格式错误");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }
    
    @Operation(summary = "审核拒绝", description = "审核拒绝失物或拾获信息")
    @PostMapping("/reject")
    public Result<Void> reject(@RequestBody Map<String, Object> params) {
        try {
            String type = (String) params.get("type");
            if (type == null || type.isEmpty()) {
                return Result.error("类型不能为空");
            }
            
            Object idObj = params.get("id");
            if (idObj == null) {
                return Result.error("ID不能为空");
            }
            Long id = Long.valueOf(idObj.toString());
            
            String remark = params.get("remark") != null ? params.get("remark").toString() : "审核不通过";
            
            Object auditorIdObj = params.get("auditorId");
            if (auditorIdObj == null) {
                return Result.error("审核人ID不能为空");
            }
            Long auditorId = Long.valueOf(auditorIdObj.toString());
            
            auditService.reject(type, id, remark, auditorId);
            return Result.success(null);
        } catch (NumberFormatException e) {
            return Result.error("ID格式错误");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }
}
