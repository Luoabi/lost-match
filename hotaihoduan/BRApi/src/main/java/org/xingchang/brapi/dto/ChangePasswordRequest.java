package org.xingchang.brapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 修改密码请求DTO
 */
@Data
public class ChangePasswordRequest {
    
    @NotNull(message = "用户ID不能为空")
    private Long id;
    
    @NotBlank(message = "原密码不能为空")
    private String oldPassword;
    
    @NotBlank(message = "新密码不能为空")
    private String newPassword;
}
