package com.oranges.cnmall.bean.city;

import java.util.List;

/**
 * 省(直辖市) ProvinceModel
 * Created by oranges on 2016/9/27.
 */

public class ProvinceModel {
	private String name; // 省(直辖市)名
	private List<CityModel> cityList; // 市列表
	
	public ProvinceModel() {
		super();
	}

	public ProvinceModel(String name, List<CityModel> cityList) {
		super();
		this.name = name;
		this.cityList = cityList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<CityModel> getCityList() {
		return cityList;
	}

	public void setCityList(List<CityModel> cityList) {
		this.cityList = cityList;
	}

	@Override
	public String toString() {
//		return "ProvinceModel [name=" + name + ", cityList=" + cityList + "]";

		return  name;
	}
	
}
