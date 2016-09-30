package com.oranges.cnmall.bean;

import java.io.Serializable;

/**
 * 热门活动 Campaign
 * Created by oranges on 2016/9/22.
 */

public class Campaign implements Serializable {

    private Long id; // id
    private String title; // 标题
    private String imgUrl; // 图片


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
