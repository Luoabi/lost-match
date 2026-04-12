package org.xingchang.brapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xingchang.brapi.common.Result;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 文件上传Controller
 */
@Tag(name = "文件管理", description = "文件上传下载相关接口")
@RestController
@RequestMapping("/file")
public class FileController {
    
    // 文件上传根目录
    private static final String UPLOAD_DIR = "uploads/";
    
    @Operation(summary = "单文件上传")
    @PostMapping("/upload")
    public Result<String> uploadFile(
            @Parameter(description = "文件") @RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return Result.error("文件不能为空");
            }
            
            // 打印调试信息
            System.out.println("接收到文件上传请求");
            System.out.println("文件名: " + file.getOriginalFilename());
            System.out.println("文件大小: " + file.getSize());
            
            String url = saveFile(file);
            
            System.out.println("文件保存成功，URL: " + url);
            
            return Result.success(url);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("文件上传失败：" + e.getMessage());
        }
    }
    
    @Operation(summary = "多文件上传")
    @PostMapping("/upload-multiple")
    public Result<List<String>> uploadMultipleFiles(
            @Parameter(description = "文件列表") @RequestParam("files") MultipartFile[] files) {
        try {
            List<String> urls = new ArrayList<>();
            
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    String url = saveFile(file);
                    urls.add(url);
                }
            }
            
            return Result.success(urls);
        } catch (Exception e) {
            return Result.error("文件上传失败：" + e.getMessage());
        }
    }
    
    @Operation(summary = "删除文件")
    @DeleteMapping("/delete")
    public Result<Void> deleteFile(
            @Parameter(description = "文件URL") @RequestParam String url) {
        try {
            // 从URL中提取文件路径
            String filePath = url.replace("/uploads/", "");
            Path path = Paths.get(UPLOAD_DIR + filePath);
            
            Files.deleteIfExists(path);
            return Result.success(null);
        } catch (Exception e) {
            return Result.error("文件删除失败：" + e.getMessage());
        }
    }
    
    @Operation(summary = "检查文件是否存在")
    @GetMapping("/check")
    public Result<String> checkFile(
            @Parameter(description = "文件URL") @RequestParam String url) {
        try {
            // 从URL中提取文件路径
            String filePath = url.replace("/uploads/", "");
            Path path = Paths.get(UPLOAD_DIR + filePath);
            
            boolean exists = Files.exists(path);
            String absolutePath = path.toAbsolutePath().toString();
            
            if (exists) {
                return Result.success("文件存在，路径: " + absolutePath);
            } else {
                return Result.error("文件不存在，查找路径: " + absolutePath);
            }
        } catch (Exception e) {
            return Result.error("检查失败：" + e.getMessage());
        }
    }
    
    @Operation(summary = "获取文件（备用方案）")
    @GetMapping("/get/**")
    public void getFile(HttpServletRequest request, HttpServletResponse response) {
        try {
            // 获取文件路径
            String requestPath = request.getRequestURI();
            String filePath = requestPath.replace("/api/file/get/", "");
            Path path = Paths.get(UPLOAD_DIR + filePath);
            
            if (!Files.exists(path)) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("文件不存在");
                return;
            }
            
            // 设置响应类型
            String contentType = Files.probeContentType(path);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
            response.setContentType(contentType);
            
            // 读取并返回文件
            Files.copy(path, response.getOutputStream());
            response.getOutputStream().flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 保存文件
     */
    private String saveFile(MultipartFile file) throws IOException {
        // 获取原始文件名
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new RuntimeException("文件名不能为空");
        }
        
        // 获取文件扩展名
        String extension = "";
        int dotIndex = originalFilename.lastIndexOf(".");
        if (dotIndex > 0) {
            extension = originalFilename.substring(dotIndex);
        }
        
        // 生成新文件名（UUID + 扩展名）
        String newFilename = UUID.randomUUID().toString() + extension;
        
        // 按日期创建子目录
        String dateDir = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String dirPath = UPLOAD_DIR + dateDir;
        
        // 创建目录
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        // 保存文件
        Path filePath = Paths.get(dirPath, newFilename);
        Files.write(filePath, file.getBytes());
        
        // 返回访问URL（包含 /api 前缀，因为 context-path 是 /api）
        return "/api/uploads/" + dateDir + "/" + newFilename;
    }
}
