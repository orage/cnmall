package com.oranges.cnmall.bean;

import java.io.Serializable;

/**
 * 收货地址 Address
 * Created by oranges on 2016/9/27.
 */

public class Address implements Serializable,Comparable<Address>{

    private Long id; // id
    private String consignee; // 收货人
    private String phone; // 手机号
    private String addr; // 收货地址
    private String zipCode; // 邮编
    private Boolean isDefault; // 是否默认地址

    public Address(){}

    public Address(String consignee, String phone, String addr, String zipCode){
        this.consignee = consignee;
        this.phone=phone;
        this.addr= addr;
        this.zipCode = zipCode;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }


    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }


    @Override
    public int compareTo(Address another) {

        if(another.getIsDefault()!=null && this.getIsDefault() !=null)
            return another.getIsDefault().compareTo(this.getIsDefault());

        return -1;
    }
}
