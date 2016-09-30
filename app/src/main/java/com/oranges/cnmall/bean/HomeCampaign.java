package com.oranges.cnmall.bean;

import java.io.Serializable;

/**
 * 首页热门活动 HomeCampaign
 * Created by oranges on 2016/9/22.
 */

public class HomeCampaign implements Serializable {

    private Long id; // id
    private String title; // 标题
    private Campaign cpOne; // 第一条Campaign
    private Campaign cpTwo; // 第二条Campaign
    private Campaign cpThree; // 第三条Campaign


    public Campaign getCpOne() {
        return cpOne;
    }

    public void setCpOne(Campaign cpOne) {
        this.cpOne = cpOne;
    }

    public Campaign getCpTwo() {
        return cpTwo;
    }

    public void setCpTwo(Campaign cpTwo) {
        this.cpTwo = cpTwo;
    }

    public Campaign getCpThree() {
        return cpThree;
    }

    public void setCpThree(Campaign cpThree) {
        this.cpThree = cpThree;
    }


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
}
