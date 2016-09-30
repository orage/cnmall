package com.oranges.cnmall.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 订单封装 Order
 * Created by oranges on 2016/9/27.
 */
public class Order implements Serializable {

    public static final int STATUS_SUCCESS = 1; //支付成功的订单
    public static final int STATUS_PAY_FAIL = -2; //支付失败的订单
    public static final int STATUS_PAY_WAIT = 0; //：待支付的订单

    private Long id; // id
    private String orderNum; // 订单号
    private Long createdTime; // 下单时间
    private Float amount; // 总价
    private int status; // 订单状态
    private List<OrderItem> items; // 订单单个商品
    private Address address; // 收货地址

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
