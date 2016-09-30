package com.oranges.cnmall.bean.city;

import java.util.List;

/**
 * 市 CityModel
 * Created by oranges on 2016/9/27.
 */

public class CityModel {
	private String name; // 城市名
	private List<DistrictModel> districtList; // 地区列表
	
	public CityModel() {
		super();
	}

	public CityModel(String name, List<DistrictModel> districtList) {
		super();
		this.name = name;
		this.districtList = districtList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<DistrictModel> getDistrictList() {
		return districtList;
	}

	public void setDistrictList(List<DistrictModel> districtList) {
		this.districtList = districtList;
	}

	@Override
	public String toString() {
		return "CityModel [name=" + name + ", districtList=" + districtList
				+ "]";
	}
	
}
