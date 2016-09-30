package com.oranges.cnmall.bean;

import java.util.List;

/**
 * 请求分页的封装 Page
 * Created by oranges on 2016/9/21.
 */
public class Page<T> {
    private int currentPage; // 当前页码
    private int pageSize; // 指定返回条目
    private int totalPage; // 总页码
    private int totalCount; // 总条目
    private List<T> list; // 数据主体


    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
