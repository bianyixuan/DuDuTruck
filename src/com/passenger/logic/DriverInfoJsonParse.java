package com.passenger.logic;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.JsonReader;

import com.passenger.domain.DriverInfo;

/**
 * ����json�ַ�������ȡ˾����������Ϣ
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
				// ��ȡjson�����е�һ������
				JSONObject jsonObject = (JSONObject) data.opt(i);
				// ��ȡdata����
				JSONObject userObject = jsonObject.getJSONObject("data");
				// �����ݽ��н���
				DriverInfo driver = new DriverInfo();
				driver.setPhone(userObject.getString("phone"));
				driver.setLatitude(userObject.getDouble("latitude"));
				driver.setLongitude(userObject.getDouble("longitude"));
				driver.setStutas(userObject.getInt("stutas"));
				driverInfos.add(driver);
			}
		} catch (JSONException e) {
			System.out.println("�����쳣");
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
			System.out.println("json�����쳣");
		}
		if (driverInfo != null) {
			return driverInfo;
		}
		return new DriverInfo();
	}

}
