package com.oranges.cnmall.bean;

import com.oranges.cnmall.bean.base.BaseBean;

/**
 * 商品一级分类 Category
 * Created by oranges on 2016/9/21.
 */
public class Category extends BaseBean {

    private String name; // 分类名

    public Category() {

    }

    public Category(String name) {
        this.name = name;
    }

    public Category(long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public long getId() {
        return super.getId();
    }

    @Override
    public void setId(long id) {
        super.setId(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
