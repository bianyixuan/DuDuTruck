package com.passenger.domain;

import java.io.Serializable;

/**
 * 司机实体类
 * 
 * @author bianyixuan
 * 
 */
public class DriverInfo implements Serializable{
	private String driverId; // 司机编号
	private String name; // 姓名
	private String number;// 车牌
	private String phone;// 联系方式
	private String totalWeight; // 限重
	private double longitude; // 经度
	private double latitude;// 纬度
	private double register_longitude;// 注册的经度
	private double register_latitude; // 注册的纬度
	private String register_address; // 注册地址
	private String truck_type; // 车型号
	private int stutas;// 状态 0表示忙碌，1表示空闲，2表示离线
	private String password; // 司机登录密码

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public double getRegister_longitude() {
		return register_longitude;
	}

	public void setRegister_longitude(double register_longitude) {
		this.register_longitude = register_longitude;
	}

	public double getRegister_latitude() {
		return register_latitude;
	}

	public void setRegister_latitude(double register_latitude) {
		this.register_latitude = register_latitude;
	}

	public String getRegister_address() {
		return register_address;
	}

	public void setRegister_address(String register_address) {
		if (register_address == null) {
			register_address = "未知";
		}
		this.register_address = register_address;
	}

	public String getTruck_type() {
		return truck_type;
	}

	public void setTruck_type(String truck_type) {
		if (truck_type == null) {
			truck_type = "未知";
		}
		this.truck_type = truck_type;
	}

	public String getDriverId() {
		return driverId;
	}

	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (name == null) {
			name = "未知";
		}
		this.name = name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getTotalWeight() {
		return totalWeight;
	}

	public void setTotalWeight(String totalWeight) {
		if (totalWeight == null) {
			totalWeight = "未知";
		}
		this.totalWeight = totalWeight;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public int getStutas() {
		return stutas;
	}

	public void setStutas(int stutas) {
		this.stutas = stutas;
	}

	@Override
	public String toString() {
		return "driver [driverId=" + driverId + ", name=" + name + ", number="
				+ number + ", phone=" + phone + ", totalWeight=" + totalWeight
				+ ", longitude=" + longitude + ", latitude=" + latitude
				+ ", register_longitude=" + register_longitude
				+ ", register_latitude=" + register_latitude
				+ ", register_address=" + register_address + ", truck_type="
				+ truck_type + ", stutas=" + stutas + "]";
	}

}
