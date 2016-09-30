package com.oranges.cnmall.bean.city;

/**
 * 县(区) DistrictModel
 * Created by oranges on 2016/9/27.
 */

public class DistrictModel {
	private String name; // 县(区)名
	private String zipcode; // 邮政编码
	
	public DistrictModel() {
		super();
	}

	public DistrictModel(String name, String zipcode) {
		super();
		this.name = name;
		this.zipcode = zipcode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	@Override
	public String toString() {
		return "DistrictModel [name=" + name + ", zipcode=" + zipcode + "]";
	}

}
