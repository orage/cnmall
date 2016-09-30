package com.oranges.cnmall.msg;

import com.oranges.cnmall.msg.base.BaseRespMsg;

/**
 * 登录返回信息 LoginRespMsg
 * Created by oranges on 2016/9/25.
 */
public class LoginRespMsg<T> extends BaseRespMsg {

    private String token; // 身份令牌(10天的有效期)

    private T data; // 数据主体

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
