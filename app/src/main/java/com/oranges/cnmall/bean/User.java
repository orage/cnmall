package com.oranges.cnmall.bean;

import java.io.Serializable;

/**
 * 用户 User
 * Created by oranges on 2016/9/25.
 */
public class User implements Serializable {

    private Long id; // 用户id
    private String email; // 邮箱
    private String logo_url; // 头像地址
    private String username; // 用户名
    private String mobi; // 手机号

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogo_url() {
        return logo_url;
    }

    public void setLogo_url(String logo_url) {
        this.logo_url = logo_url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobi() {
        return mobi;
    }

    public void setMobi(String mobi) {
        this.mobi = mobi;
    }
}
