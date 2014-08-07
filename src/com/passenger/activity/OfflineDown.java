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
	private ArrayList<MKOLSearchRecord> records;// ������Ϣ����
	private boolean forStart = false;
	private LocalMapAdapter lAdapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ActivityManager mam = ActivityManager.getInstance();
		mam.pushOneActivity(OfflineDown.this);//activity ����ջ��
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
		 * �˳�ʱ���������ߵ�ͼģ��
		 */
		mOffline.destroy();
		super.onDestroy();
	}

	/**
	 * ��ʼ����
	 * 
	 * @param view
	 */
	public void start() {
		mOffline.start(cityid);
		startdown.setText("��ͣ����");
		GoDownList();
		Toast.makeText(this, "��ʼ�������ߵ�ͼ. cityid: " + cityid, Toast.LENGTH_SHORT)
				.show();
	}

	/**
	 * ��ͣ����
	 * 
	 * @param view
	 */
	public void stop() {
		mOffline.pause(cityid);
		startdown.setText("��ʼ����");
		Toast.makeText(this, "��ͣ�������ߵ�ͼ. cityid: " + cityid, Toast.LENGTH_SHORT)
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

	// ��ȡ�����б�
	public ArrayList<String> getCitylist() {
		ArrayList<String> l = new ArrayList<String>();
		records = mOffline.getOfflineCityList();

		for (MKOLSearchRecord r : records) {
			l.add(r.cityName + "(" + r.cityID + ")" + "   --"
					+ this.formatDataSize(r.size));
		}
		return l;
	}

	// ��ȡ���ع������б�
	public ArrayList<MKOLUpdateElement> getCityDown() {
		// ��ȡ���¹������ߵ�ͼ��Ϣ
		ArrayList<MKOLUpdateElement> l = new ArrayList<MKOLUpdateElement>();
		l = mOffline.getAllUpdateInfo();
		if (l == null) {
			l = new ArrayList<MKOLUpdateElement>();
		}
		return l;
	}

	/**
	 * �л��������б�
	 * 
	 * @param view
	 */
	public void GoCityList() {

		downlistview.setVisibility(View.GONE);
		citylistview.setVisibility(View.VISIBLE);

	}

	/**
	 * ɾ�����ߵ�ͼ
	 * 
	 * @param view
	 */
	public void remove(View view) {
		mOffline.remove(cityid);
		Toast.makeText(this, "ɾ�����ߵ�ͼ. cityid: " + cityid, Toast.LENGTH_SHORT)
				.show();
	}

	/**
	 * �л������ع����б�
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
			// �������ؽ��ȸ�����ʾ
			if (update != null) {
				updateView();
			}
		}
			break;
		case MKOfflineMap.TYPE_NEW_OFFLINE:
			// �������ߵ�ͼ��װ
			Log.d("OfflineDemo", String.format("add offlinemap num:%d", state));
			break;
		case MKOfflineMap.TYPE_VER_UPDATE:
			// �汾������ʾ
			// MKOLUpdateElement e = mOffline.getUpdateInfo(state);
			break;
		}

	}

	/**
	 * ����״̬��ʾ
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
	 * ���ߵ�ͼ�����б�������
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
				update.setText("�ɸ���");
			} else {
				update.setText("����");
			}
			if (e.ratio != 100) {
				display.setEnabled(false);
			} else {
				display.setEnabled(true);
				i--;
				if(i==0){
					startdown.setText("��ʼ����");
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
