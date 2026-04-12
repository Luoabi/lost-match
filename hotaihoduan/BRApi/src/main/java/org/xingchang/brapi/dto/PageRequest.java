package org.xingchang.brapi.dto;

import lombok.Data;

/**
 * 分页请求DTO
 */
@Data
public class PageRequest {
    
    private Integer page = 1;
    
    private Integer size = 10;
    
    private String keyword;
    
    private String sortBy;
    
    private String sortOrder = "desc";
    
    public int getOffset() {
        return (page - 1) * size;
    }
}
