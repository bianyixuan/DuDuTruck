package com.passenger.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.offline.MKOLSearchRecord;
import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;
import com.passenger.R;

public class OfflineDown extends Activity implements MKOfflineMapListener {
	private EditText city;
	private Button startdown, citylistbt, downlodebt;
	private ListView citylistview, downlistview;
	private ArrayList<String> citylist;
	private ArrayList<MKOLUpdateElement> downlist;
	private MKOfflineMap mOffline = null;
	private int cityid;
	private ArrayList<MKOLSearchRecord> records;// 城市信息集合
	private boolean forStart = false;
	private LocalMapAdapter lAdapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ActivityManager mam = ActivityManager.getInstance();
		mam.pushOneActivity(OfflineDown.this);//activity 加入栈内
		setContentView(R.layout.offline_search_layout);
		init();
		startdown.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!forStart) {
					start();
					forStart = true;
				} else {
					stop();
					forStart = false;
				}
			}
		});

		citylistview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				city.setText(records.get(arg2).cityName);
				cityid = records.get(arg2).cityID;
			}
		});

		citylistbt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				GoCityList();
			}
		});
		downlodebt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				GoDownList();
			}
		});

	}

	public void init() {
		mOffline = new MKOfflineMap();
		mOffline.init(this);
		city = (EditText) findViewById(R.id.city);
		startdown = (Button) findViewById(R.id.startdown);
		citylistbt = (Button) findViewById(R.id.citylistbt);
		downlodebt = (Button) findViewById(R.id.downlodebt);
		citylistview = (ListView) findViewById(R.id.citylistview);
		downlistview = (ListView) findViewById(R.id.downlistview);
		citylist = getCitylist();
		ListAdapter aAdapter = (ListAdapter) new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, citylist);
		citylistview.setAdapter(aAdapter);
		downlist = getCityDown();
		lAdapter = new LocalMapAdapter(OfflineDown.this, downlist);
		downlistview.setAdapter(lAdapter);
	}

	@Override
	protected void onPause() {
		mOffline.pause(cityid);
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		/**
		 * 退出时，销毁离线地图模块
		 */
		mOffline.destroy();
		super.onDestroy();
	}

	/**
	 * 开始下载
	 * 
	 * @param view
	 */
	public void start() {
		mOffline.start(cityid);
		startdown.setText("暂停下载");
		GoDownList();
		Toast.makeText(this, "开始下载离线地图. cityid: " + cityid, Toast.LENGTH_SHORT)
				.show();
	}

	/**
	 * 暂停下载
	 * 
	 * @param view
	 */
	public void stop() {
		mOffline.pause(cityid);
		startdown.setText("开始下载");
		Toast.makeText(this, "暂停下载离线地图. cityid: " + cityid, Toast.LENGTH_SHORT)
				.show();
	}

	public String formatDataSize(int size) {
		String ret = "";
		if (size < (1024 * 1024)) {
			ret = String.format("%dK", size / 1024);
		} else {
			ret = String.format("%.1fM", size / (1024 * 1024.0));
		}
		return ret;
	}

	// 获取城市列表
	public ArrayList<String> getCitylist() {
		ArrayList<String> l = new ArrayList<String>();
		records = mOffline.getOfflineCityList();

		for (MKOLSearchRecord r : records) {
			l.add(r.cityName + "(" + r.cityID + ")" + "   --"
					+ this.formatDataSize(r.size));
		}
		return l;
	}

	// 获取下载管理器列表
	public ArrayList<MKOLUpdateElement> getCityDown() {
		// 获取已下过的离线地图信息
		ArrayList<MKOLUpdateElement> l = new ArrayList<MKOLUpdateElement>();
		l = mOffline.getAllUpdateInfo();
		if (l == null) {
			l = new ArrayList<MKOLUpdateElement>();
		}
		return l;
	}

	/**
	 * 切换至城市列表
	 * 
	 * @param view
	 */
	public void GoCityList() {

		downlistview.setVisibility(View.GONE);
		citylistview.setVisibility(View.VISIBLE);

	}

	/**
	 * 删除离线地图
	 * 
	 * @param view
	 */
	public void remove(View view) {
		mOffline.remove(cityid);
		Toast.makeText(this, "删除离线地图. cityid: " + cityid, Toast.LENGTH_SHORT)
				.show();
	}

	/**
	 * 切换至下载管理列表
	 * 
	 * @param view
	 */
	public void GoDownList() {
		downlistview.setVisibility(View.VISIBLE);
		citylistview.setVisibility(View.GONE);
		updateView();
	}

	@Override
	public void onGetOfflineMapState(int type, int state) {
		// TODO Auto-generated method stub

		switch (type) {
		case MKOfflineMap.TYPE_DOWNLOAD_UPDATE: {
			MKOLUpdateElement update = mOffline.getUpdateInfo(state);
			// 处理下载进度更新提示
			if (update != null) {
				updateView();
			}
		}
			break;
		case MKOfflineMap.TYPE_NEW_OFFLINE:
			// 有新离线地图安装
			Log.d("OfflineDemo", String.format("add offlinemap num:%d", state));
			break;
		case MKOfflineMap.TYPE_VER_UPDATE:
			// 版本更新提示
			// MKOLUpdateElement e = mOffline.getUpdateInfo(state);
			break;
		}

	}

	/**
	 * 更新状态显示
	 */
	public void updateView() {
		downlist = getCityDown();
		if (downlist == null) {
			downlist = new ArrayList<MKOLUpdateElement>();
		}
		lAdapter.setList(downlist);
		lAdapter.notifyDataSetChanged();
	}

	/**
	 * 离线地图管理列表适配器
	 */
	public class LocalMapAdapter extends BaseAdapter {
		private Context context;
		private ArrayList<MKOLUpdateElement> localMapList;
		private int i = 0;

		LocalMapAdapter(Context context,
				ArrayList<MKOLUpdateElement> localMapList) {
			this.context = context;
			this.localMapList = localMapList;
			i  = localMapList.size();
		}

		public void setList(ArrayList<MKOLUpdateElement> localMapList) {
			this.localMapList = localMapList;
		}

		@Override
		public int getCount() {
			return localMapList.size();
		}

		@Override
		public Object getItem(int index) {
			return localMapList.get(index);
		}

		@Override
		public long getItemId(int index) {
			return index;
		}

		@Override
		public View getView(int index, View view, ViewGroup arg2) {
			MKOLUpdateElement e = (MKOLUpdateElement) getItem(index);
			view = View.inflate(context, R.layout.offline_localmap_list, null);
			initViewItem(view, e);
			return view;
		}

		void initViewItem(View view, final MKOLUpdateElement e) {
			Button display = (Button) view.findViewById(R.id.display);
			Button remove = (Button) view.findViewById(R.id.remove);
			TextView title = (TextView) view.findViewById(R.id.title);
			TextView update = (TextView) view.findViewById(R.id.update);
			TextView ratio = (TextView) view.findViewById(R.id.ratio);
			ratio.setText(e.ratio + "%");
			title.setText(e.cityName);
			if (e.update) {
				update.setText("可更新");
			} else {
				update.setText("最新");
			}
			if (e.ratio != 100) {
				display.setEnabled(false);
			} else {
				display.setEnabled(true);
				i--;
				if(i==0){
					startdown.setText("开始下载");
				}
			}
			remove.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					mOffline.remove(e.cityID);
					updateView();
				}
			});
		}

	}
}
