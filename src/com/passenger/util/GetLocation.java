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
		// ��ȡ��ǰλ�õľ�γ��
		this.lm = (LocationManager) context
				.getSystemService(context.LOCATION_SERVICE);
		// ���ò�ѯ����
		String provider = lm.getBestProvider(getCriteria(), true);
		// ��ȡλ����Ϣ
		Location location = lm.getLastKnownLocation(provider);
		// ����textview�ؼ�������
		updateView(location);
		// ���locationListener������
		lm.requestLocationUpdates(provider, 5000, 8, locationListener);
	}

	// ���λ�ñ仯������
	LocationListener locationListener = new LocationListener() {

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {

		}

		@Override
		public void onProviderEnabled(String provider) {
			// ��ȡλ����Ϣ
			Location location = lm.getLastKnownLocation(provider);
			// ����textview�ռ������
			updateView(location);
		}

		@Override
		public void onProviderDisabled(String provider) {
			updateView(null);
		}

		@Override
		public void onLocationChanged(Location location) {
			// ��дonLocationChanged
			updateView(location);
		}
	};

	// ����textview�е���ʾ����

	public void updateView(Location location) {
		if (location != null) {
			// �ж��Ƿ�Ϊ��
			// ���γ��
			latitude = location.getLatitude();
			// ��þ���
			longitude = location.getLongitude();

		} else {
			// ��������location����Ϊ�������
			Toast.makeText(context, "�޷���ȡλ����Ϣ", Toast.LENGTH_LONG).show();
		}
	}

	// ���ز�ѯ����
	public Criteria getCriteria() {
		Criteria c = new Criteria();
		// ���ò�ѯ�ľ���
		c.setAccuracy(Criteria.ACCURACY_COARSE);
		// �����Ƿ�Ҫ���ٶ�
		c.setSpeedRequired(false);
		// �����Ƿ������������
		c.setCostAllowed(false);
		// �����Ƿ���Ҫ�õ�����
		c.setBearingRequired(false);
		// �����Ƿ���Ҫ�õ����θ߶�
		c.setAltitudeRequired(false);
		// ��������ĵ�����ļ���
		c.setPowerRequirement(Criteria.POWER_LOW);
		// ���ز�ѯ������
		return c;
	}

	public Double getLatitude() {
		return latitude;
	}

	 
	public Double getLongitude() {
		return longitude;
	}

}
