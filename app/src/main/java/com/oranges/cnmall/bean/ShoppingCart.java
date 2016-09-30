package com.oranges.cnmall.bean;

import java.io.Serializable;

/**
 * 购物车 ShoppingCart
 * Created by oranges on 2016/9/23.
 */
public class ShoppingCart extends Wares implements Serializable {

    private int count; // 总条目
    private boolean isChecked=true; // 是否选中

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }





}
