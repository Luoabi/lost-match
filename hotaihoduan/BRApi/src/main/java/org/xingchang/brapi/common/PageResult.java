package org.xingchang.brapi.common;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * 分页响应结果
 */
@Data
public class PageResult<T> implements Serializable {
    
    private List<T> list;
    private Long total;
    private Integer page;
    private Integer pageSize;
    private Integer totalPages;
    
    public PageResult() {
    }
    
    public PageResult(List<T> list, Long total, Integer page, Integer pageSize) {
        this.list = list;
        this.total = total;
        this.page = page;
        this.pageSize = pageSize;
        this.totalPages = (int) Math.ceil((double) total / pageSize);
    }
}
