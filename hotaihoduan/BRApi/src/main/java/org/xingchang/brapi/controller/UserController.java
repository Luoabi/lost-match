package org.xingchang.brapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.xingchang.brapi.common.PageResult;
import org.xingchang.brapi.common.Result;
import org.xingchang.brapi.dto.ChangePasswordRequest;
import org.xingchang.brapi.dto.LoginRequest;
import org.xingchang.brapi.dto.LoginResponse;
import org.xingchang.brapi.entity.User;
import org.xingchang.brapi.service.UserService;

import java.util.List;

/**
 * 用户Controller
 */
@Tag(name = "用户管理", description = "用户相关接口")
@RestController
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @Operation(summary = "用户登录", description = "用户名密码登录，返回Token")
    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            LoginResponse response = userService.login(request);
            return Result.success(response);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @Operation(summary = "用户注册", description = "注册新用户账号")
    @PostMapping("/register")
    public Result<User> register(@Valid @RequestBody User user) {
        try {
            User newUser = userService.register(user);
            return Result.success(newUser);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @Operation(summary = "获取用户信息", description = "根据用户ID获取用户详细信息")
    @GetMapping("/info/{id}")
    public Result<User> getUserInfo(
            @Parameter(description = "用户ID") @PathVariable Long id) {
        try {
            User user = userService.getUserInfo(id);
            return Result.success(user);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @Operation(summary = "用户列表", description = "分页查询用户列表")
    @GetMapping("/list")
    public Result<PageResult<User>> getUserList(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "搜索关键词") @RequestParam(required = false) String keyword,
            @Parameter(description = "角色") @RequestParam(required = false) String role,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status) {
        try {
            PageResult<User> result = userService.getUserList(page, size, keyword, role, status);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @Operation(summary = "添加用户", description = "创建新用户")
    @PostMapping("/add")
    public Result<User> addUser(@Valid @RequestBody User user) {
        try {
            User newUser = userService.addUser(user);
            return Result.success(newUser);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @Operation(summary = "更新用户", description = "更新用户信息")
    @PutMapping("/update")
    public Result<User> updateUser(@Valid @RequestBody User user) {
        try {
            User updatedUser = userService.updateUser(user);
            return Result.success(updatedUser);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @Operation(summary = "删除用户", description = "根据ID删除用户")
    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteUser(
            @Parameter(description = "用户ID") @PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return Result.success(null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @Operation(summary = "批量删除用户", description = "批量删除多个用户")
    @DeleteMapping("/batch-delete")
    public Result<Void> batchDeleteUsers(@RequestBody List<Long> ids) {
        try {
            userService.batchDeleteUsers(ids);
            return Result.success(null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @Operation(summary = "更新个人信息", description = "用户更新自己的个人信息")
    @PutMapping("/profile")
    public Result<User> updateProfile(@Valid @RequestBody User user) {
        try {
            User updatedUser = userService.updateProfile(user);
            return Result.success(updatedUser);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @Operation(summary = "修改密码", description = "用户修改自己的密码")
    @PutMapping("/change-password")
    public Result<Void> changePassword(@RequestBody ChangePasswordRequest request) {
        try {
            userService.changePassword(request);
            return Result.success(null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
