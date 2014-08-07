package com.passenger.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.passenger.R;
import com.passenger.activity.DriverInfoListActivity;
import com.passenger.domain.DriverInfo;
import com.passenger.util.DistanceUtil;

public class DriverInfoAdapter extends BaseAdapter {

	private List<DriverInfo> drivers;
	private LayoutInflater inflater;
	private DriverInfo driver;
	private DriverInfoListActivity mContext;
	double latitude, longitude;

	public DriverInfoAdapter(List<DriverInfo> drivers,
			DriverInfoListActivity context, double latitude, double longtitude) {
		this.drivers = drivers;
		this.inflater = LayoutInflater.from(context);
		this.mContext = context;
		this.latitude = latitude;
		this.longitude = longtitude;
	}

	@Override
	public int getCount() {
		return drivers.size();
	}

	@Override
	public Object getItem(int position) {
		return drivers.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		driver = drivers.get(position);
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.driverinfolist_item, null);
			viewHolder.dName = (TextView) convertView.findViewById(R.id.dName);
			viewHolder.dSend = (Button) convertView
					.findViewById(R.id.dSendInfo);
			viewHolder.driverPhoto = (ImageView) convertView
					.findViewById(R.id.driverPhoto);
			viewHolder.dType = (TextView) convertView.findViewById(R.id.dType);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		// 表示在线
		if (driver.getStutas() == 1) {
			viewHolder.driverPhoto.setImageResource(R.drawable.trucklogo6);
			viewHolder.dName.setTextColor(Color.BLUE);
			viewHolder.dType.setTextColor(Color.BLUE);
		} else {
			viewHolder.driverPhoto.setImageResource(R.drawable.trucklogo6);
		}
		viewHolder.dName.setText("手机号:" + driver.getPhone());
		double distance = DistanceUtil.GetDistance(latitude, longitude,
				driver.getLatitude(), driver.getLongitude());
		viewHolder.dType.setText("距离:" + distance + "米");
		addListener(viewHolder.dSend, driver, distance);

		return convertView;
	}

	public void addListener(View view, final DriverInfo driver,
			final double distance) {
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mContext.getDriverByPhone(driver.getPhone(),
						driver.getStutas(), distance);
			}
		});
	}

	class ViewHolder {
		TextView dName, dType;
		Button dSend;
		ImageView driverPhoto;
	}
}
