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
    public Result<FileUploadResponse> uploadFile(
            @Parameter(description = "文件") @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {
        try {
            if (file.isEmpty()) {
                return Result.error("文件不能为空");
            }
            
            // 打印调试信息
            System.out.println("接收到文件上传请求");
            System.out.println("文件名: " + file.getOriginalFilename());
            System.out.println("文件大小: " + file.getSize());
            
            String relativePath = saveFile(file);
            
            // 构建完整的访问URL
            String baseUrl = getBaseUrl(request);
            String fullUrl = baseUrl + relativePath;
            
            System.out.println("文件保存成功");
            System.out.println("相对路径: " + relativePath);
            System.out.println("完整URL: " + fullUrl);
            
            FileUploadResponse response = new FileUploadResponse();
            response.setUrl(fullUrl);
            response.setRelativePath(relativePath);
            response.setFilename(file.getOriginalFilename());
            response.setSize(file.getSize());
            
            return Result.success(response);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("文件上传失败：" + e.getMessage());
        }
    }
    
    @Operation(summary = "多文件上传")
    @PostMapping("/upload-multiple")
    public Result<List<FileUploadResponse>> uploadMultipleFiles(
            @Parameter(description = "文件列表") @RequestParam("files") MultipartFile[] files,
            HttpServletRequest request) {
        try {
            List<FileUploadResponse> responses = new ArrayList<>();
            String baseUrl = getBaseUrl(request);
            
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    String relativePath = saveFile(file);
                    String fullUrl = baseUrl + relativePath;
                    
                    FileUploadResponse response = new FileUploadResponse();
                    response.setUrl(fullUrl);
                    response.setRelativePath(relativePath);
                    response.setFilename(file.getOriginalFilename());
                    response.setSize(file.getSize());
                    
                    responses.add(response);
                }
            }
            
            return Result.success(responses);
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
        
        // 返回相对路径（用于静态资源访问）
        return "/api/uploads/" + dateDir + "/" + newFilename;
    }
    
    /**
     * 获取基础URL
     */
    private String getBaseUrl(HttpServletRequest request) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        
        StringBuilder baseUrl = new StringBuilder();
        baseUrl.append(scheme).append("://").append(serverName);
        
        // 只有非标准端口才添加端口号
        if ((scheme.equals("http") && serverPort != 80) || 
            (scheme.equals("https") && serverPort != 443)) {
            baseUrl.append(":").append(serverPort);
        }
        
        return baseUrl.toString();
    }
    
    /**
     * 文件上传响应类
     */
    public static class FileUploadResponse {
        private String url;           // 完整访问URL
        private String relativePath;  // 相对路径
        private String filename;      // 原始文件名
        private Long size;           // 文件大小
        
        public String getUrl() {
            return url;
        }
        
        public void setUrl(String url) {
            this.url = url;
        }
        
        public String getRelativePath() {
            return relativePath;
        }
        
        public void setRelativePath(String relativePath) {
            this.relativePath = relativePath;
        }
        
        public String getFilename() {
            return filename;
        }
        
        public void setFilename(String filename) {
            this.filename = filename;
        }
        
        public Long getSize() {
            return size;
        }
        
        public void setSize(Long size) {
            this.size = size;
        }
    }
}
