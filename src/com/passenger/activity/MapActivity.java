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
	private MapView mMapView = null; // ��ͼ��ͼ
	private static final String NOCHECKPHONE = "nocheckphone";
	private BaiduMap mbaidumap = null; // ��ͼ������
	ImageView refresh; // ˢ��
	// ��λ���
	private LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();

	private LocationMode mCurrentMode = null;
	BitmapDescriptor mCurrentMarker = null; // ��λ�ı�־ͼƬ
	boolean isFirstLoc = true;// �Ƿ��״ζ�λ
	private GeoCoder mSearch = null; // ��������λ��
	private String divernomber = "δ֪��Ϣ";
	private String latitude, longitude; // ��γ��
	private static final String DRIVERINFOBYPHONEURL = "driverInfoByPhone.action";
	public static final String DRIVERINFOBYDISTANCE = "driverInfoByDistance.action";
	private static final int BUSY = 0;
	private static final int IDLE = 1;
	GetLocation location;
	private ArrayList<DriverInfo> diverlist;// �ӷ�������õ�˾��λ�����ݼ���
	private DriverInfo driverInfo; // �ӷ������ϻ�ȡ˾������Ϣ

	private String distance = "10000"; // �޶��ľ���

	ShowDialogUtil showdialogUtil; // ������

	View main_list_bt;// �ײ�Ԥ��������ť

	// �ؼ���ʼ��
	private void init() {
		main_list_bt = findViewById(R.id.main_list_bt);
		mMapView = (MapView) findViewById(R.id.bmapView);
		mMapView.removeViewAt(2);
		refresh = (ImageView) findViewById(R.id.imageView3);
		// ��ȡ��ǰ�ľ�γ��
		location = new GetLocation(MapActivity.this);

		mCurrentMarker = BitmapDescriptorFactory
				.fromResource(R.drawable.icon_geo);// ��λ��ʾ��iconͼƬ

		// ��ʼ����ͼ������
		mbaidumap = mMapView.getMap();

		// ��ʼ������ģ�飬ע���¼�����
		mSearch = GeoCoder.newInstance();

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ��ʹ��SDK�����֮ǰ��ʼ��context��Ϣ������ApplicationContext
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.map_layout);
		init();
		// ��ʼ��λ
		startLocation();
		// ��ʼ����
		startSearch();

		// ����������Χ
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

		// �ײ�Ԥ��������ť
		main_list_bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// ���ж��Ƿ��ֻ���֤
				String phone = getSharedPreferences("passengerPhone",
						MODE_PRIVATE).getString("phone", NOCHECKPHONE);
				// ��ʾ�Ѿ���֤���ֻ���
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

		// ˢ������
		refresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(MapActivity.this, "ˢ������", Toast.LENGTH_LONG)
						.show();
				// ��ʼ��λ
				startLocation();
				// ��ʼ����
				startSearch();
			}
		});

		// ��־����¼�
		// �����ȡ˾���ĵ绰���룬���ݵ绰�����ȡ��˾������ϸ��Ϣ
		mbaidumap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker marker) {
				getDriverInfoByPhone(marker.getExtraInfo());
				return false;
			}
		});
	}

	// ѡ�����ĵ�����
	public void selectDistanceDialog(ArrayAdapter<CharSequence> adapter,
			final String latitude, final String longitude) {
		AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);
		builder.setTitle("��ѡ��������Χ(��λ:��)");
		View view = LayoutInflater.from(MapActivity.this).inflate(
				R.layout.distance_spinner, null);
		builder.setView(view);
		Spinner spinner = (Spinner) view.findViewById(R.id.spinner1);
		spinner.setAdapter(adapter);

		// �÷���ֻ�ǻ�ȡdistance��ֵ
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
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// ��ѡ��ľ���ֵ���͸�������
				Map<String, String> params = new HashMap<String, String>();
				params.put("latitude", latitude);
				params.put("longitude", longitude);
				params.put("distance", distance);
				Map<String, String> map = new HashMap<String, String>();
				map.put("url", constants.BaseUrl + DRIVERINFOBYDISTANCE);
				new SendFormParamTask() {
					protected void onPostExecute(String result) {
						diverlist = DriverInfoJsonParse.getDriverInfos(result);
						mbaidumap.clear(); // ���֮ǰ��ͼ��
						startTag(diverlist);

					};
				}.execute(params, map);
			}
		});
		builder.setNegativeButton("ȡ��", null);
		builder.show();
	}

	// ������λ
	public void startLocation() {

		if (mLocClient != null) {
			// �˳�ʱ���ٶ�λ
			mLocClient.stop();
		} else {
			// ������λͼ��
			mbaidumap.setMyLocationEnabled(true);
			// ��λ��ʼ��
			mLocClient = new LocationClient(getApplicationContext());
			mLocClient.registerLocationListener(myListener); // ���ö�λ������
			LocationClientOption option = new LocationClientOption();
			option.setOpenGps(true);// ��gps
			option.setCoorType("bd09ll"); // ������������
			option.setScanSpan(1000);
			mLocClient.setLocOption(option);
			mLocClient.start();
			mCurrentMode = LocationMode.NORMAL;// ��λ����
			mbaidumap.setMyLocationConfigeration(new MyLocationConfigeration(
					mCurrentMode, true, mCurrentMarker));

		}
	}

	/**
	 * ��ȡ˾������Ϣͨ����γ��,Ȼ�󽫻�ȡ����˾����ʾ����ͼ��
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
			// ����json
			protected void onPostExecute(String result) {
				diverlist = DriverInfoJsonParse.getDriverInfos(result);
				mbaidumap.clear(); // ���֮ǰ��ͼ��
				startTag(diverlist);
			};
		}.execute(params, map);

	}

	/**
	 * ����˾�����󼯺ϣ���ʼ�ڵ�ͼ�ϱ��
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
	 * ��λSDK���������ظ����ã�ʱ��ˢ��
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view ���ٺ��ڴ����½��յ�λ��
			if (location == null || mMapView == null)
				return;
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// �˴����ÿ����߻�ȡ���ķ�����Ϣ��˳ʱ��0-360
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
	 * ��ȡ˾������Ϣͨ��
	 * 
	 * @param phone
	 * @return
	 */
	public void getDriverInfoByPhone(final Bundle bundle) {
		final String phone = bundle.getString("phone");
		final int status = bundle.getInt("stutas");
		final ProgressDialogUtil dialogUtil = ProgressDialogUtil
				.getInstance(MapActivity.this);
		dialogUtil.showDialog("���ڻ�ȡ˾������...");
		Map<String, String> params = new HashMap<String, String>();
		params.put("phone", phone);
		Map<String, String> map = new HashMap<String, String>();
		map.put("url", constants.BaseUrl + DRIVERINFOBYPHONEURL);
		new SendFormParamTask() {
			protected void onPostExecute(String result) {

				// ����˾������Ϣ
				driverInfo = DriverInfoJsonParse.getDriverInfo(result);
				// ��ȡ����֮��ľ���
				double distance = DistanceUtil.GetDistance(
						Double.valueOf(latitude), Double.valueOf(longitude),
						bundle.getDouble("latitude"),
						bundle.getDouble("longitude"));
				// ������
				showdialogUtil = ShowDialogUtil.getInstance(MapActivity.this);
				showdialogUtil.showDialog(driverInfo, phone, status, distance);
				dialogUtil.closeDialog();
			};
		}.execute(params, map);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// ��activityִ��onDestroyʱִ��mMapView.onDestroy()��ʵ�ֵ�ͼ�������ڹ���
		// �˳�ʱ���ٶ�λ
		mLocClient.stop();
		// �رն�λͼ��
		mbaidumap.setMyLocationEnabled(false);
		mMapView.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// ��ʼ��λ
		startLocation();
		// ��ʼ����
		startSearch();
		// ��activityִ��onResumeʱִ��mMapView. onResume ()��ʵ�ֵ�ͼ�������ڹ���
		mMapView.onResume();

	}

	@Override
	protected void onPause() {
		super.onPause();
		// ��activityִ��onPauseʱִ��mMapView. onPause ()��ʵ�ֵ�ͼ�������ڹ���
		mMapView.onPause();
	}

	/**
	 * ��γ������˾��������ʶ
	 * 
	 * @param phone
	 *            //�绰����
	 * @param stutas
	 *            //״̬
	 * @param latitude
	 *            //γ��
	 * @param longitude
	 *            //����
	 */
	public void search(Bundle bundle, double latitude, double longitude) {
		Log.i("LL", bundle.toString());
		Log.i("LL", this.latitude + "::::" + this.longitude);
		divernomber = bundle.getString("phone");
		LatLng ptCenter = new LatLng(latitude, longitude);
		// �ж����״̬Ϊ0����ʾ��ʾ��ɫ�������1��������ɫ��
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
	 * ���ĵ�ַ������γ������ʶ
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
