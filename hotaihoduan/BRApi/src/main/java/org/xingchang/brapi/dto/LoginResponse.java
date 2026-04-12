package org.xingchang.brapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.xingchang.brapi.entity.User;

/**
 * 登录响应DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    
    private String token;
    
    private UserInfo userInfo;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfo {
        private Long id;
        private String username;
        private String realName;
        private String phone;
        private String email;
        private String avatar;
        private String role;
        private String studentId;
        private String department;
        private Integer creditScore;
        
        public static UserInfo fromUser(User user) {
            return new UserInfo(
                user.getId(),
                user.getUsername(),
                user.getRealName(),
                user.getPhone(),
                user.getEmail(),
                user.getAvatar(),
                user.getRole(),
                user.getStudentId(),
                user.getDepartment(),
                user.getCreditScore()
            );
        }
    }
}
