package com.oranges.cnmall.bean.base;

import java.io.Serializable;

/**
 * bean基类
 * Created by oranges on 2016/9/22.
 */
public class BaseBean implements Serializable {

    protected long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
