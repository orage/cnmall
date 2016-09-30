package com.oranges.cnmall.msg;


import com.oranges.cnmall.msg.base.BaseRespMsg;

/**
 * 创建订单返回信息 OrderRespMsg
 * Created by oranges on 2016/9/26.
 */
public class CreateOrderRespMsg extends BaseRespMsg {

    private OrderRespMsg data;

    public OrderRespMsg getData() {
        return data;
    }

    public void setData(OrderRespMsg data) {
        this.data = data;
    }

}


