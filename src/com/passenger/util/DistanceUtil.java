package com.passenger.util;

/**
 * 计算地球上两点之间的距离
 * 
 * @author bianyixuan
 * 
 */
public class DistanceUtil {
	private final static double EARTH_RADIUS = 6378137.000;// 地球半径

	private static double rad(double d) {
		return d * Math.PI / 180.000000;
	}

	public static double GetDistance(double lat1, double lng1, double lat2,
			double lng2) {
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lng1) - rad(lng2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		return s;
	}
}
