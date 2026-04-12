package org.xingchang.brapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.xingchang.brapi.common.PageResult;
import org.xingchang.brapi.common.Result;
import org.xingchang.brapi.entity.OperationLog;
import org.xingchang.brapi.service.LogService;

import java.time.LocalDateTime;

/**
 * 日志Controller
 */
@Tag(name = "日志管理", description = "操作日志相关接口")
@RestController
@RequestMapping("/logs")
public class LogController {
    
    @Autowired
    private LogService logService;
    
    @Operation(summary = "日志列表", description = "分页查询操作日志")
    @GetMapping("/list")
    public Result<PageResult<OperationLog>> getLogList(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "搜索关键词") @RequestParam(required = false) String keyword,
            @Parameter(description = "操作类型") @RequestParam(required = false) String action,
            @Parameter(description = "模块") @RequestParam(required = false) String module,
            @Parameter(description = "状态") @RequestParam(required = false) String status,
            @Parameter(description = "开始时间") @RequestParam(required = false) 
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @Parameter(description = "结束时间") @RequestParam(required = false) 
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        try {
            PageResult<OperationLog> result = logService.getLogList(page, size, keyword, 
                    action, module, status, startTime, endTime);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @Operation(summary = "日志详情", description = "根据ID获取日志详细信息")
    @GetMapping("/detail/{id}")
    public Result<OperationLog> getLogDetail(
            @Parameter(description = "日志ID") @PathVariable Long id) {
        try {
            OperationLog log = logService.getLogDetail(id);
            return Result.success(log);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @Operation(summary = "清空日志", description = "清空指定时间之前的日志")
    @DeleteMapping("/clear")
    public Result<Void> clearLogs(
            @Parameter(description = "清空此时间之前的日志") @RequestParam(required = false) 
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime beforeTime) {
        try {
            logService.clearLogs(beforeTime);
            return Result.success(null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
