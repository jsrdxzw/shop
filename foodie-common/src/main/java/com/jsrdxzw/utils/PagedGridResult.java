package com.jsrdxzw.utils;

import java.util.List;

/**
 * @Author: xuzhiwei
 * @Date: 2019/11/10
 * @Description: 自定义返回每次分页的数据格式
 */
public class PagedGridResult {

    /**
     * 当前页数
     */
    private Integer page;

    /**
     * 总页数
     */
    private Integer total;

    /**
     * 总记录数
     */
    private Long records;

    /**
     * 每行显示的内容
     */
    private List<?> rows;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Long getRecords() {
        return records;
    }

    public void setRecords(Long records) {
        this.records = records;
    }

    public List<?> getRows() {
        return rows;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
    }
}
