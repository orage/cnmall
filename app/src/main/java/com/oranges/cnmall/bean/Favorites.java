
package com.oranges.cnmall.bean;

import java.io.Serializable;

/**
 * 我的收藏 Favorites
 * Created by oranges on 2016/9/22.
 */
public class Favorites implements Serializable {

    private Long id; // id
    private Long createTime; // 收藏时间
    private Wares wares; // 商品信息

    public Favorites(){}


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Wares getWares() {
        return wares;
    }

    public void setWares(Wares wares) {
        this.wares = wares;
    }
}
