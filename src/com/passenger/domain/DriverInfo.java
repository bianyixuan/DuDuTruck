package com.passenger.domain;

import java.io.Serializable;

/**
 * ˾��ʵ����
 * 
 * @author bianyixuan
 * 
 */
public class DriverInfo implements Serializable{
	private String driverId; // ˾�����
	private String name; // ����
	private String number;// ����
	private String phone;// ��ϵ��ʽ
	private String totalWeight; // ����
	private double longitude; // ����
	private double latitude;// γ��
	private double register_longitude;// ע��ľ���
	private double register_latitude; // ע���γ��
	private String register_address; // ע���ַ
	private String truck_type; // ���ͺ�
	private int stutas;// ״̬ 0��ʾæµ��1��ʾ���У�2��ʾ����
	private String password; // ˾����¼����

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
			register_address = "δ֪";
		}
		this.register_address = register_address;
	}

	public String getTruck_type() {
		return truck_type;
	}

	public void setTruck_type(String truck_type) {
		if (truck_type == null) {
			truck_type = "δ֪";
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
			name = "δ֪";
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
			totalWeight = "δ֪";
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
