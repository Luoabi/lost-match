package org.xingchang.brapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

/**
 * Web配置类
 * 配置静态资源访问
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    /**
     * 配置静态资源映射
     * 将 /uploads/** 请求映射到 uploads/ 目录
     * 注意：由于 context-path 是 /api，实际访问路径是 /api/uploads/**
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 获取项目根目录的绝对路径
        String uploadPath = new File("uploads").getAbsolutePath() + File.separator;
        
        System.out.println("配置静态资源路径: " + uploadPath);
        System.out.println("访问路径: /api/uploads/**");
        
        // 配置上传文件的访问路径
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadPath);
    }
}
