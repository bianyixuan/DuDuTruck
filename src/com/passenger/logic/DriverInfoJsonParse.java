package com.passenger.logic;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.JsonReader;

import com.passenger.domain.DriverInfo;

/**
 * 解析json字符串，获取司机的完整信息
 * 
 * @author bianyixuan
 * 
 */
public class DriverInfoJsonParse {

	public static ArrayList<DriverInfo> getDriverInfos(String result) {
		ArrayList<DriverInfo> driverInfos = null;
		try {
			JSONObject object = new JSONObject(result);
			JSONArray data = object.getJSONArray("driverInfo");
			driverInfos = new ArrayList<DriverInfo>();
			for (int i = 0; i < data.length(); i++) {
				// 获取json数组中的一个对象
				JSONObject jsonObject = (JSONObject) data.opt(i);
				// 获取data对象
				JSONObject userObject = jsonObject.getJSONObject("data");
				// 对数据进行解析
				DriverInfo driver = new DriverInfo();
				driver.setPhone(userObject.getString("phone"));
				driver.setLatitude(userObject.getDouble("latitude"));
				driver.setLongitude(userObject.getDouble("longitude"));
				driver.setStutas(userObject.getInt("stutas"));
				driverInfos.add(driver);
			}
		} catch (JSONException e) {
			System.out.println("解析异常");
		}
		return driverInfos;
	}

	public static DriverInfo getDriverInfo(String result) {
		DriverInfo driverInfo = null;
		try {
			JSONObject jsonObject = new JSONObject(result);
			JSONObject driverObject = jsonObject.getJSONObject("driverInfo");
			driverInfo = new DriverInfo();
			driverInfo.setName(driverObject.getString("name"));
			driverInfo.setNumber(driverObject.getString("number"));
			driverInfo.setRegister_address(driverObject
					.getString("register_address"));
			driverInfo.setTotalWeight(driverObject.getString("totalWeight"));
			driverInfo.setTruck_type(driverObject.getString("truck_type"));
		} catch (JSONException e) {
			System.out.println("json解析异常");
		}
		if (driverInfo != null) {
			return driverInfo;
		}
		return new DriverInfo();
	}

}
