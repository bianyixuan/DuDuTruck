package com.passenger.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfigeration;
import com.baidu.mapapi.map.MyLocationConfigeration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.passenger.R;
import com.passenger.domain.DriverInfo;
import com.passenger.logic.DriverInfoJsonParse;
import com.passenger.logic.SendFormParamTask;
import com.passenger.util.DistanceUtil;
import com.passenger.util.GetLocation;
import com.passenger.util.ProgressDialogUtil;
import com.passenger.util.ShowDialogUtil;
import com.passenger.util.constants;

@SuppressLint("DefaultLocale")
public class MapActivity extends Activity {
	private MapView mMapView = null; // 地图视图
	private static final String NOCHECKPHONE = "nocheckphone";
	private BaiduMap mbaidumap = null; // 地图管理器
	ImageView refresh; // 刷新
	// 定位相关
	private LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();

	private LocationMode mCurrentMode = null;
	BitmapDescriptor mCurrentMarker = null; // 定位的标志图片
	boolean isFirstLoc = true;// 是否首次定位
	private GeoCoder mSearch = null; // 搜索具体位置
	private String divernomber = "未知信息";
	private String latitude, longitude; // 经纬度
	private static final String DRIVERINFOBYPHONEURL = "driverInfoByPhone.action";
	public static final String DRIVERINFOBYDISTANCE = "driverInfoByDistance.action";
	private static final int BUSY = 0;
	private static final int IDLE = 1;
	GetLocation location;
	private ArrayList<DriverInfo> diverlist;// 从服务器获得的司机位置数据集合
	private DriverInfo driverInfo; // 从服务器上获取司机的信息

	private String distance = "10000"; // 限定的距离

	ShowDialogUtil showdialogUtil; // 弹出框

	View main_list_bt;// 底部预定货车按钮

	// 控件初始化
	private void init() {
		main_list_bt = findViewById(R.id.main_list_bt);
		mMapView = (MapView) findViewById(R.id.bmapView);
		mMapView.removeViewAt(2);
		refresh = (ImageView) findViewById(R.id.imageView3);
		// 获取当前的经纬度
		location = new GetLocation(MapActivity.this);

		mCurrentMarker = BitmapDescriptorFactory
				.fromResource(R.drawable.icon_geo);// 定位显示的icon图片

		// 初始化地图管理器
		mbaidumap = mMapView.getMap();

		// 初始化搜索模块，注册事件监听
		mSearch = GeoCoder.newInstance();

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 在使用SDK各组件之前初始化context信息，传入ApplicationContext
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.map_layout);
		init();
		// 开始定位
		startLocation();
		// 开始搜索
		startSearch();

		// 设置搜索范围
		// settingLimit.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// ArrayAdapter<CharSequence> adapter = ArrayAdapter
		// .createFromResource(MapActivity.this,
		// R.array.distances,
		// android.R.layout.simple_spinner_item);
		// adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// selectDistanceDialog(adapter, latitude, longitude);
		// }
		// });

		// 底部预定货车按钮
		main_list_bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// 先判断是否手机验证
				String phone = getSharedPreferences("passengerPhone",
						MODE_PRIVATE).getString("phone", NOCHECKPHONE);
				// 表示已经验证过手机号
				if (!phone.equals(NOCHECKPHONE)) {
					Intent intent = new Intent();
					intent.setAction("goList");
					intent.putExtra("driverList", diverlist);
					sendBroadcast(intent);
				} else {
					if (showdialogUtil != null) {
						showdialogUtil.checkPhoneDialog();
					} else {
						showdialogUtil = ShowDialogUtil
								.getInstance(MapActivity.this);
						showdialogUtil.checkPhoneDialog();
					}
				}

			}
		});

		// 刷新数据
		refresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(MapActivity.this, "刷新数据", Toast.LENGTH_LONG)
						.show();
				// 开始定位
				startLocation();
				// 开始搜索
				startSearch();
			}
		});

		// 标志点击事件
		// 点击获取司机的电话号码，根据电话号码获取该司机的详细信息
		mbaidumap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker marker) {
				getDriverInfoByPhone(marker.getExtraInfo());
				return false;
			}
		});
	}

	// 选择距离的弹出框
	public void selectDistanceDialog(ArrayAdapter<CharSequence> adapter,
			final String latitude, final String longitude) {
		AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);
		builder.setTitle("请选择搜索范围(单位:米)");
		View view = LayoutInflater.from(MapActivity.this).inflate(
				R.layout.distance_spinner, null);
		builder.setView(view);
		Spinner spinner = (Spinner) view.findViewById(R.id.spinner1);
		spinner.setAdapter(adapter);

		// 该方法只是获取distance的值
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				distance = (String) parent.getItemAtPosition(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 将选择的距离值发送给服务器
				Map<String, String> params = new HashMap<String, String>();
				params.put("latitude", latitude);
				params.put("longitude", longitude);
				params.put("distance", distance);
				Map<String, String> map = new HashMap<String, String>();
				map.put("url", constants.BaseUrl + DRIVERINFOBYDISTANCE);
				new SendFormParamTask() {
					protected void onPostExecute(String result) {
						diverlist = DriverInfoJsonParse.getDriverInfos(result);
						mbaidumap.clear(); // 清除之前的图层
						startTag(diverlist);

					};
				}.execute(params, map);
			}
		});
		builder.setNegativeButton("取消", null);
		builder.show();
	}

	// 开启定位
	public void startLocation() {

		if (mLocClient != null) {
			// 退出时销毁定位
			mLocClient.stop();
		} else {
			// 开启定位图层
			mbaidumap.setMyLocationEnabled(true);
			// 定位初始化
			mLocClient = new LocationClient(getApplicationContext());
			mLocClient.registerLocationListener(myListener); // 设置定位监听器
			LocationClientOption option = new LocationClientOption();
			option.setOpenGps(true);// 打开gps
			option.setCoorType("bd09ll"); // 设置坐标类型
			option.setScanSpan(1000);
			mLocClient.setLocOption(option);
			mLocClient.start();
			mCurrentMode = LocationMode.NORMAL;// 定位类型
			mbaidumap.setMyLocationConfigeration(new MyLocationConfigeration(
					mCurrentMode, true, mCurrentMarker));

		}
	}

	/**
	 * 获取司机的信息通过经纬度,然后将获取到的司机显示到地图上
	 */
	public void startSearch() {

		diverlist = new ArrayList<DriverInfo>();
		latitude = location.getLatitude().toString();
		longitude = location.getLongitude().toString();
		Map<String, String> params = new HashMap<String, String>();
		params.put("distance", distance);
		params.put("latitude", latitude);
		params.put("longitude", longitude);
		Map<String, String> map = new HashMap<String, String>();
		map.put("url", constants.BaseUrl + DRIVERINFOBYDISTANCE);
		new SendFormParamTask() {
			// 解析json
			protected void onPostExecute(String result) {
				diverlist = DriverInfoJsonParse.getDriverInfos(result);
				mbaidumap.clear(); // 清除之前的图层
				startTag(diverlist);
			};
		}.execute(params, map);

	}

	/**
	 * 根据司机对象集合，开始在地图上标记
	 * 
	 * @param driverInfos
	 */
	public void startTag(ArrayList<DriverInfo> driverInfos) {
		for (int i = 0; i < driverInfos.size(); i++) {
			Bundle bundle = new Bundle();
			bundle.putString("phone", driverInfos.get(i).getPhone());
			bundle.putInt("stutas", driverInfos.get(i).getStutas());
			bundle.putDouble("latitude", driverInfos.get(i).getLatitude());
			bundle.putDouble("longitude", driverInfos.get(i).getLongitude());
			search(bundle, Double.valueOf(driverInfos.get(i).getLatitude()),
					Double.valueOf(driverInfos.get(i).getLongitude()));
		}
	}

	/**
	 * 定位SDK监听函数重复调用，时刻刷新
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mbaidumap.setMyLocationData(locData);

			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mbaidumap.animateMapStatus(u);
			}

		}

		@Override
		public void onReceivePoi(BDLocation arg0) {

		}
	}

	/**
	 * 获取司机的信息通过
	 * 
	 * @param phone
	 * @return
	 */
	public void getDriverInfoByPhone(final Bundle bundle) {
		final String phone = bundle.getString("phone");
		final int status = bundle.getInt("stutas");
		final ProgressDialogUtil dialogUtil = ProgressDialogUtil
				.getInstance(MapActivity.this);
		dialogUtil.showDialog("正在获取司机数据...");
		Map<String, String> params = new HashMap<String, String>();
		params.put("phone", phone);
		Map<String, String> map = new HashMap<String, String>();
		map.put("url", constants.BaseUrl + DRIVERINFOBYPHONEURL);
		new SendFormParamTask() {
			protected void onPostExecute(String result) {

				// 解析司机的信息
				driverInfo = DriverInfoJsonParse.getDriverInfo(result);
				// 获取两点之间的距离
				double distance = DistanceUtil.GetDistance(
						Double.valueOf(latitude), Double.valueOf(longitude),
						bundle.getDouble("latitude"),
						bundle.getDouble("longitude"));
				// 弹出框
				showdialogUtil = ShowDialogUtil.getInstance(MapActivity.this);
				showdialogUtil.showDialog(driverInfo, phone, status, distance);
				dialogUtil.closeDialog();
			};
		}.execute(params, map);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		// 退出时销毁定位
		mLocClient.stop();
		// 关闭定位图层
		mbaidumap.setMyLocationEnabled(false);
		mMapView.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 开始定位
		startLocation();
		// 开始搜索
		startSearch();
		// 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		mMapView.onResume();

	}

	@Override
	protected void onPause() {
		super.onPause();
		// 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
		mMapView.onPause();
	}

	/**
	 * 经纬度搜索司机并作标识
	 * 
	 * @param phone
	 *            //电话号码
	 * @param stutas
	 *            //状态
	 * @param latitude
	 *            //纬度
	 * @param longitude
	 *            //经度
	 */
	public void search(Bundle bundle, double latitude, double longitude) {
		Log.i("LL", bundle.toString());
		Log.i("LL", this.latitude + "::::" + this.longitude);
		divernomber = bundle.getString("phone");
		LatLng ptCenter = new LatLng(latitude, longitude);
		// 判断如果状态为0，表示显示灰色，如果是1，正常颜色。
		if (bundle.getInt("stutas") == IDLE) {
			Log.i("LL", "idle:" + IDLE);
			mbaidumap.addOverlay(new MarkerOptions()
					.position(ptCenter)
					.extraInfo(bundle)
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.icon_marka)));
		} else if (bundle.getInt("stutas") == BUSY) {
			Log.i("LL", "busy:" + BUSY);
			mbaidumap.addOverlay(new MarkerOptions()
					.position(ptCenter)
					.extraInfo(bundle)
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.icon_busymark)));
		}

	}

	/**
	 * 中文地址搜索经纬度做标识
	 * 
	 * @param title
	 * @param city
	 * @param address
	 */
	public void searchaddress(String title, String city, String address) {
		divernomber = title;
		mSearch.geocode(new GeoCodeOption().city(city).address(address));
	}

}
