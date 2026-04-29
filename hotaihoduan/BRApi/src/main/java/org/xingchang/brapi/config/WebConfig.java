package org.xingchang.brapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

/**
 * Web配置类
 * 配置静态资源访问和跨域
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    /**
     * 配置静态资源映射
     * 将 /uploads/** 请求映射到 uploads/ 目录
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 获取项目根目录的绝对路径
        String uploadPath = new File("uploads").getAbsolutePath() + File.separator;
        
        System.out.println("===========================================");
        System.out.println("配置静态资源路径: " + uploadPath);
        System.out.println("访问路径: /api/uploads/**");
        System.out.println("示例: http://localhost:8080/api/uploads/2024/01/01/xxx.jpg");
        System.out.println("===========================================");
        
        // 配置上传文件的访问路径
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadPath)
                .setCachePeriod(3600); // 缓存1小时
    }
    
    /**
     * 配置跨域（如果CorsConfig不生效，这里作为备用）
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
