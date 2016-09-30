package com.oranges.cnmall.bean;

import com.oranges.cnmall.bean.base.BaseBean;

/**
 * 首页轮播广告 Banner
 * Created by oranges on 2016/9/22.
 */
public class Banner extends BaseBean {

    private  String name; // 名称
    private  String imgUrl; // 图片
    private  String description; // 描述


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
