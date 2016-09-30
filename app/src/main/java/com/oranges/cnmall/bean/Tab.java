package com.oranges.cnmall.bean;

/**
 * 主页Tab封装
 * Created by oranges on 2016/9/21.
 */
public class Tab {
    private int title; // 文字
    private int icon; // 图标
    private Class fragment; // 对应fragment

    public Tab(int title, int icon, Class fragment) {
        this.title = title;
        this.icon = icon;
        this.fragment = fragment;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public Class getFragment() {
        return fragment;
    }

    public void setFragment(Class fragment) {
        this.fragment = fragment;
    }
}
