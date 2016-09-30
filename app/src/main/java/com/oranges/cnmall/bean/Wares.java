package com.oranges.cnmall.bean;

import java.io.Serializable;

/**
 * 商品 Wares
 * Created by oranges on 2016/9/21.
 */
public class Wares implements Serializable {
    private Long id; // id
    private String name; // 商品名
    private String imgUrl; // 图片
    private String description; // 描述
    private Float price; // 价格

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}
