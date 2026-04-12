package org.xingchang.brapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xingchang.brapi.common.Result;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试Controller
 */
@Tag(name = "测试接口", description = "用于测试系统是否正常运行")
@RestController
@RequestMapping("/test")
public class TestController {
    
    @Operation(summary = "Hello测试", description = "测试接口是否可以正常访问")
    @GetMapping("/hello")
    public Result<String> hello() {
        return Result.success("Hello, Lost & Found API!");
    }
    
    @Operation(summary = "项目信息", description = "获取项目基本信息")
    @GetMapping("/info")
    public Result<Map<String, Object>> info() {
        Map<String, Object> data = new HashMap<>();
        data.put("projectName", "校园失物追寻平台");
        data.put("version", "1.0.0");
        data.put("author", "BRApi Team");
        data.put("description", "基于SpringBoot的失物招领系统后端API");
        data.put("swagger", "http://localhost:8080/swagger-ui.html");
        return Result.success(data);
    }
}
