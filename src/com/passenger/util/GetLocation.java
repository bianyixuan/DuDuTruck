package com.passenger.util;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

public class GetLocation {
	private LocationManager lm;
	private Context context;
	private Double latitude;
	private Double longitude;

	public GetLocation(Context context) {
		this.context = context;
		// 获取当前位置的经纬度
		this.lm = (LocationManager) context
				.getSystemService(context.LOCATION_SERVICE);
		// 设置查询条件
		String provider = lm.getBestProvider(getCriteria(), true);
		// 获取位置信息
		Location location = lm.getLastKnownLocation(provider);
		// 更新textview控件的内容
		updateView(location);
		// 添加locationListener监听器
		lm.requestLocationUpdates(provider, 5000, 8, locationListener);
	}

	// 添加位置变化监听器
	LocationListener locationListener = new LocationListener() {

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {

		}

		@Override
		public void onProviderEnabled(String provider) {
			// 获取位置信息
			Location location = lm.getLastKnownLocation(provider);
			// 更新textview空间的内容
			updateView(location);
		}

		@Override
		public void onProviderDisabled(String provider) {
			updateView(null);
		}

		@Override
		public void onLocationChanged(Location location) {
			// 重写onLocationChanged
			updateView(location);
		}
	};

	// 更新textview中的显示内容

	public void updateView(Location location) {
		if (location != null) {
			// 判断是否为空
			// 获得纬度
			latitude = location.getLatitude();
			// 获得经度
			longitude = location.getLongitude();

		} else {
			// 如果传入的location对象为空则清空
			Toast.makeText(context, "无法获取位置信息", Toast.LENGTH_LONG).show();
		}
	}

	// 返回查询条件
	public Criteria getCriteria() {
		Criteria c = new Criteria();
		// 设置查询的精度
		c.setAccuracy(Criteria.ACCURACY_COARSE);
		// 设置是否要求速度
		c.setSpeedRequired(false);
		// 设置是否允许产生费用
		c.setCostAllowed(false);
		// 设置是否需要得到方向
		c.setBearingRequired(false);
		// 设置是否需要得到海拔高度
		c.setAltitudeRequired(false);
		// 设置允许的电池消耗级别
		c.setPowerRequirement(Criteria.POWER_LOW);
		// 返回查询的条件
		return c;
	}

	public Double getLatitude() {
		return latitude;
	}

	 
	public Double getLongitude() {
		return longitude;
	}

}
