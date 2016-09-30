package com.oranges.cnmall.msg.base;

import java.io.Serializable;

/**
 * 请求返回信息的基类 BaseRespMsg
 * Created by oranges on 2016/9/25.
 */
public class BaseRespMsg implements Serializable {

    public final static int STATUS_SUCCESS = 1; // 成功
    public final static int STATUS_ERROR = 0; // 错误
    public final static String MSG_SUCCESS = "success";
    protected int status = STATUS_SUCCESS;
    protected String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
