package com.passenger.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.passenger.R;
import com.passenger.adapter.DriverInfoAdapter;
import com.passenger.domain.DriverInfo;
import com.passenger.logic.DriverInfoJsonParse;
import com.passenger.logic.SendFormParamTask;
import com.passenger.util.GetLocation;
import com.passenger.util.ProgressDialogUtil;
import com.passenger.util.ShowDialogUtil;
import com.passenger.util.constants;

public class DriverInfoListActivity extends Activity implements OnClickListener {
	ArrayList<DriverInfo> driverInfos;
	ListView driverList;
	private DriverInfoAdapter adapter;
	double latitude, longitude;
	GetLocation location;
	ImageView img_flush; // 返回，刷新按钮

	// 获取在线司机的信息
	private static final String DRIVERINFOURL = "driverInfo.action";
	// 通过电话号码找到司机的详细信息
	private static final String DRIVERINFOBYPHONEURL = "driverInfoByPhone.action";

	private void init() {
		img_flush = (ImageView) findViewById(R.id.img_flush);
		driverList = (ListView) findViewById(R.id.driverList);
		img_flush.setOnClickListener(this);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.driverinfolist_layout);
		init();
		// 获取本地的经纬度
		location = new GetLocation(DriverInfoListActivity.this);
		latitude = location.getLatitude();
		longitude = location.getLongitude();
		setAdapter();

		// 广播接收者，获取司机集合信息
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("goList");
		registerReceiver(new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.getAction().equals("goList")) {
					driverInfos = (ArrayList<DriverInfo>) intent.getSerializableExtra("driverList");
					setAdapter();
				}
			}
		}, intentFilter);
	}

	/**
	 * 适配器适配数据
	 */
	public void setAdapter() {
		if (driverInfos == null) {
			driverInfos = new ArrayList<DriverInfo>();
		}
		adapter = new DriverInfoAdapter(driverInfos,
				DriverInfoListActivity.this, latitude, longitude);
		driverList.setAdapter(adapter);
	}

	private DriverInfo driverInfo; // 司机对象

	public void getDriverByPhone(final String phone, final int status,
			final double distance) {
		final ProgressDialogUtil dialogUtil = ProgressDialogUtil
				.getInstance(DriverInfoListActivity.this);
		dialogUtil.showDialog("正在获取司机数据...");
		Map<String, String> params = new HashMap<String, String>();
		params.put("phone", phone);
		Map<String, String> map = new HashMap<String, String>();
		map.put("url", constants.BaseUrl + DRIVERINFOBYPHONEURL);
		new SendFormParamTask() {
			protected void onPostExecute(String result) {
				// 解析司机的信息
				driverInfo = DriverInfoJsonParse.getDriverInfo(result);
				ShowDialogUtil showDialogUtil = ShowDialogUtil
						.getInstance(DriverInfoListActivity.this);
				showDialogUtil.showDialog(driverInfo, phone, status, distance);
				dialogUtil.closeDialog();
			};
		}.execute(params, map);
	}

	// 返回司机的信息
	public DriverInfo getDriverInfo() {
		if (driverInfo == null) {
			driverInfo = new DriverInfo();
		}
		return driverInfo;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_flush: // 刷新
			flush();
			break;
		}
	}

	/**
	 * 获取司机的信息通过经纬度,然后将获取到的司机显示到地图上
	 */
	public void flush() {
		final ProgressDialogUtil dialogUtil = ProgressDialogUtil
				.getInstance(DriverInfoListActivity.this);
		dialogUtil.showDialog("正在更新数据...");
		Map<String, String> params = new HashMap<String, String>();
		params.put("latitude", latitude + "");
		params.put("longitude", longitude + "");
		Map<String, String> map = new HashMap<String, String>();
		map.put("url", constants.BaseUrl + DRIVERINFOURL);
		new SendFormParamTask() {
			// 解析json
			protected void onPostExecute(String result) {
				driverInfos = DriverInfoJsonParse.getDriverInfos(result);
				// 更新适配器
				adapter.notifyDataSetChanged();
				dialogUtil.closeDialog();
			};
		}.execute(params, map);

	}
}
