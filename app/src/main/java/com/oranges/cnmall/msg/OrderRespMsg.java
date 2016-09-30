package com.oranges.cnmall.msg;

import com.oranges.cnmall.bean.Charge;

/**
 * 订单返回信息 OrderRespMsg
 * Created by oranges on 2016/9/26.
 */
public class OrderRespMsg{

    private String orderNum;

    private Charge charge;

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public Charge getCharge() {
        return charge;
    }

    public void setCharge(Charge charge) {
        this.charge = charge;
    }

}