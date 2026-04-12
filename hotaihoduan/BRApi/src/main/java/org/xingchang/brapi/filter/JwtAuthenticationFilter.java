package org.xingchang.brapi.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.xingchang.brapi.util.JwtUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * JWT认证过滤器
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    @Autowired
    private JwtUtil jwtUtil;
    
    // 不需要认证的路径
    private static final List<String> EXCLUDE_PATHS = Arrays.asList(
            "/api/user/login",
            "/api/user/register",
            "/api/test/",
            "/api/file/upload",  // 文件上传接口（临时开放，生产环境应该需要认证）
            "/uploads/",         // 静态资源访问
            "/swagger-ui",
            "/v3/api-docs",
            "/swagger-resources",
            "/webjars"
    );
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {
        
        String path = request.getRequestURI();
        
        // 检查是否是不需要认证的路径
        boolean shouldSkip = EXCLUDE_PATHS.stream().anyMatch(path::startsWith);
        
        if (shouldSkip) {
            filterChain.doFilter(request, response);
            return;
        }
        
        // 获取Token
        String token = request.getHeader("Authorization");
        
        if (token == null || !token.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"未登录或Token已过期\"}");
            return;
        }
        
        // 去掉Bearer前缀
        token = token.substring(7);
        
        // 验证Token
        if (!jwtUtil.validateToken(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"Token无效或已过期\"}");
            return;
        }
        
        // Token有效，继续处理请求
        // 可以将用户信息存入request attribute
        Long userId = jwtUtil.getUserIdFromToken(token);
        String username = jwtUtil.getUsernameFromToken(token);
        request.setAttribute("userId", userId);
        request.setAttribute("username", username);
        
        filterChain.doFilter(request, response);
    }
}
