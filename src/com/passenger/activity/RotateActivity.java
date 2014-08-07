package com.passenger.activity;

import android.app.Activity;
import android.app.ListActivity;
import android.app.LocalActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.baidu.mapapi.map.MapView;
import com.passenger.R;
import com.passenger.util.MyAnimations;
import com.passenger.util.Rotate3D;
import com.passenger.util.RotateVir3D;
import com.passenger.util.ShowDialogUtil;

public class RotateActivity extends Activity implements OnTouchListener {

	private static final String NOCHECKPHONE = "nocheckphone";
	ShowDialogUtil showdialogUtil; // 弹出框

	private ViewGroup layoutmain;// 中间
	private View layoutnext;// 左边
	private View layoutlast;// 右边

	private View layoutup;// 上面activity
	private View layoutdown;// 下面activity

	private Rotate3D rotate3d;
	private Rotate3D rotate3d2;
	private Rotate3D rotate3d3;// 水平切换

	private RotateVir3D rotvir3d;
	private RotateVir3D rotvir3d2;
	private RotateVir3D rotvir3d3;// 垂直切换

	private int mCenterX;
	private int mCenterY;
	private float degree = (float) 0.0;
	private int currentTab = 0;
	private int currentTabV = 0;
	private float perDegree;
	private VelocityTracker mVelocityTracker;
	private FrameLayout framelayout;
	private LocalActivityManager manager = null;
	private ImageView main_menu_bt;// 菜单
	private ImageView main_list_bt;// 列表
	private ImageView img_back; // 列表的返回键
	private LinearLayout bottom_setting_bt;// 范围设置
	private LinearLayout bottom_book_bt;// 预订
	private Button book_bt;// 预约返回
	private Button setting_bt;// 设置返回
	MapView mapView;

	// ImageView img_logo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		manager = new LocalActivityManager(this, true);
		manager.dispatchCreate(savedInstanceState);
		initMain();
		Log.i("LL", "RotateActivity:Oncreate()");
		MyAnimations.initOffset(this);
		DisplayMetrics dm = new DisplayMetrics();
		dm = getResources().getDisplayMetrics();
		mCenterX = dm.widthPixels / 2;
		mCenterY = dm.heightPixels / 2;
		perDegree = (float) (90.0 / dm.widthPixels);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		manager.dispatchPause(isFinishing());
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		manager.dispatchResume();
		super.onResume();
	}

	@SuppressWarnings("deprecation")
	private void initMain() {
		setContentView(R.layout.main_layout);
		framelayout = (FrameLayout) findViewById(R.id.framelayout);
		Intent main = new Intent(RotateActivity.this, MapActivity.class);
		layoutmain = (ViewGroup) getView("MAIN", main);

		Intent personinfo = new Intent(RotateActivity.this,
				PerSonActivity.class);
		layoutlast = getView("PERSONINFO", personinfo);
		layoutlast.setOnTouchListener(this);
		framelayout.addView(layoutlast);

		Intent list = new Intent(RotateActivity.this,
				DriverInfoListActivity.class);
		layoutnext = getView("LIST", list);
		layoutnext.setOnTouchListener(this);
		framelayout.addView(layoutnext);

		Intent setting = new Intent(RotateActivity.this, SettingActivity.class);
		layoutdown = getView("SETTING", setting);
		framelayout.addView(layoutdown);

		Intent book = new Intent(RotateActivity.this, GoodsOrderActivity.class);
		layoutup = getView("BOOK", book);
		framelayout.addView(layoutup);

		img_back = (ImageView) layoutnext.findViewById(R.id.img_back);

		main_menu_bt = (ImageView) layoutmain.findViewById(R.id.main_menu_bt);
		main_list_bt = (ImageView) layoutmain.findViewById(R.id.main_list_bt);
		bottom_setting_bt = (LinearLayout) layoutmain
				.findViewById(R.id.bottom_setting_bt);
		bottom_book_bt = (LinearLayout) layoutmain
				.findViewById(R.id.bottom_book_bt);
		setting_bt = (Button) layoutdown.findViewById(R.id.setting_bt);
		book_bt = (Button) layoutup.findViewById(R.id.book_bt);

		layoutmain.setOnTouchListener(this);
		framelayout.addView(layoutmain);

		mapView = (MapView) layoutmain.findViewById(R.id.bmapView);
		// img_logo = (ImageView) layoutmain.findViewById(R.id.img_logo);

		// 列表的返回键
		img_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				degree = 45;
				endRotateByVelocity();
			}
		});

		// 菜单
		main_menu_bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				degree = 45;
				endRotateByVelocity();
			}
		});
		bottom_book_bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				degree = 45;
				endRotateByVelocityForVir();

			}
		});
		// 设置界面
		bottom_setting_bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				degree = -45;
				endRotateByVelocityForVir();
			}
		});

		// 广播接受者
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("goList");
		registerReceiver(new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.getAction().equals("goList")) {
					degree = -45;
					endRotateByVelocity();
				}
			}
		}, intentFilter);

		book_bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				degree = -45;
				endRotateByVelocityForVir();
			}
		});

		setting_bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				degree = 45;
				endRotateByVelocityForVir();
			}
		});

	}

	// 通过activity获取视图

	@SuppressWarnings("deprecation")
	private View getView(String id, Intent intent) {
		return manager.startActivity(id, intent).getDecorView();
	}

	private int mLastMotionX;

	// 水平滑动监听
	public boolean onTouch(View arg0, MotionEvent event) {
		int x = (int) event.getX();
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mLastMotionX = x;
			break;
		case MotionEvent.ACTION_MOVE:
			mVelocityTracker.computeCurrentVelocity(1000, 1000);
			Log.i("test", "velocityTraker :" + mVelocityTracker.getXVelocity());
			int dx = x - mLastMotionX;
			if (dx != 0) {
				doRotate(dx);
				if (degree > 90) {
					degree = 0;
					break;
				}
			} else {
				return false;
			}
			mLastMotionX = x;
			break;
		case MotionEvent.ACTION_UP:
			mVelocityTracker.computeCurrentVelocity(1000);
			float VelocityX = mVelocityTracker.getXVelocity();
			if (VelocityX > 500 || VelocityX < -500) {
				endRotateByVelocity();
			} else {
				endRotate();
			}
			releaseVelocityTracker();

			break;

		case MotionEvent.ACTION_CANCEL:
			releaseVelocityTracker();
			break;
		}

		return true;
	}

	// 取消切换
	private void releaseVelocityTracker() {
		if (null != mVelocityTracker) {
			mVelocityTracker.clear();
			mVelocityTracker.recycle();
			mVelocityTracker = null;
		}

	}

	// 主要动画执行方法（如果不需要滑动时间，可以直接掉此方法执行旋转切换）
	private void endRotateByVelocity() {

		if (degree > 0) {
			rotate3d = new Rotate3D(degree, 90, 0, mCenterX, mCenterY);
			rotate3d3 = new Rotate3D(-90 + degree, 0, 0, mCenterX, mCenterY);
			rotate3d.setDuration(500);
			rotate3d3.setDuration(500);
			if (currentTab == 0) {
				layoutmain.startAnimation(rotate3d);
				layoutlast.startAnimation(rotate3d3);
			} else if (currentTab == 2) {
				layoutlast.startAnimation(rotate3d);
				layoutnext.startAnimation(rotate3d3);
			} else if (currentTab == 1) {
				layoutnext.startAnimation(rotate3d);
				layoutmain.startAnimation(rotate3d3);
			}

			currentTab = (currentTab - 1) % 3;
			if (currentTab < 0) {
				currentTab = 2;
			}
		} else if (degree < 0) {
			rotate3d = new Rotate3D(degree, -90, 0, mCenterX, mCenterY);
			rotate3d2 = new Rotate3D(90 + degree, 0, 0, mCenterX, mCenterY);
			rotate3d.setDuration(500);
			rotate3d2.setDuration(500);
			if (currentTab == 0) {
				layoutmain.startAnimation(rotate3d);
				layoutnext.startAnimation(rotate3d2);
			} else if (currentTab == 1) {
				layoutnext.startAnimation(rotate3d);
				layoutlast.startAnimation(rotate3d2);
			} else if (currentTab == 2) {
				layoutlast.startAnimation(rotate3d);
				layoutmain.startAnimation(rotate3d2);
			}

			currentTab = (currentTab + 1) % 3;
		}

		setViewVisibile();

		degree = 0;

	}

	// 主要动画执行方法（如果不需要滑动时间，可以直接掉此方法执行旋转切换）
	private void endRotateByVelocityForVir() {
		if (degree > 0) {

			rotvir3d = new RotateVir3D(degree, 90, 0, mCenterX, mCenterY);
			rotvir3d3 = new RotateVir3D(-90 + degree, 0, 0, mCenterX, mCenterY);
			rotvir3d.setDuration(500);
			rotvir3d3.setDuration(500);
			if (currentTabV == 0) {
				layoutmain.startAnimation(rotvir3d);
				layoutup.startAnimation(rotvir3d3);
			} else if (currentTabV == 2) {
				layoutup.startAnimation(rotvir3d);
				layoutdown.startAnimation(rotvir3d3);
			} else if (currentTabV == 1) {
				layoutdown.startAnimation(rotvir3d);
				layoutmain.startAnimation(rotvir3d3);
			}

			currentTabV = (currentTabV - 1) % 3;
			if (currentTabV < 0) {
				currentTabV = 2;
			}
		} else if (degree < 0) {
			rotvir3d = new RotateVir3D(degree, -90, 0, mCenterX, mCenterY);
			rotvir3d2 = new RotateVir3D(90 + degree, 0, 0, mCenterX, mCenterY);
			rotvir3d.setDuration(500);
			rotvir3d2.setDuration(500);
			if (currentTabV == 0) {
				layoutmain.startAnimation(rotvir3d);
				layoutdown.startAnimation(rotvir3d2);
			} else if (currentTabV == 1) {
				layoutdown.startAnimation(rotvir3d);
				layoutmain.startAnimation(rotvir3d2);
			} else if (currentTabV == 2) {
				layoutup.startAnimation(rotvir3d);
				layoutmain.startAnimation(rotvir3d2);
			}

			currentTabV = (currentTabV + 1) % 3;
		}

		System.out.println(">>>>>>>>degree:" + degree + " currentTab:"
				+ currentTabV);
		setViewVisibileForVir();

		degree = 0;

	}

	// 水平切换动画执行
	private void endRotate() {
		if (degree > 45) {
			rotate3d = new Rotate3D(degree, 90, 0, mCenterX, mCenterY);
			rotate3d3 = new Rotate3D(-90 + degree, 0, 0, mCenterX, mCenterY);
			rotate3d.setDuration(300);
			rotate3d3.setDuration(300);
			if (currentTab == 0) {
				layoutmain.startAnimation(rotate3d);
				layoutlast.startAnimation(rotate3d3);
			} else if (currentTab == 2) {
				layoutlast.startAnimation(rotate3d);
				layoutnext.startAnimation(rotate3d3);
			} else if (currentTab == 1) {
				layoutnext.startAnimation(rotate3d);
				layoutmain.startAnimation(rotate3d3);
			}

			currentTab = (currentTab - 1) % 3;
			if (currentTab < 0) {
				currentTab = 2;
			}
		} else if (degree < -45) {
			rotate3d = new Rotate3D(degree, -90, 0, mCenterX, mCenterY);
			rotate3d2 = new Rotate3D(90 + degree, 0, 0, mCenterX, mCenterY);
			rotate3d.setDuration(300);
			rotate3d2.setDuration(300);
			if (currentTab == 0) {
				layoutmain.startAnimation(rotate3d);
				layoutnext.startAnimation(rotate3d2);
			} else if (currentTab == 1) {
				layoutnext.startAnimation(rotate3d);
				layoutlast.startAnimation(rotate3d2);
			} else if (currentTab == 2) {
				layoutlast.startAnimation(rotate3d);
				layoutmain.startAnimation(rotate3d2);
			}

			currentTab = (currentTab + 1) % 3;
		} else {
			rotate3d = new Rotate3D(degree, 0, 0, mCenterX, mCenterY);
			rotate3d2 = new Rotate3D(90 + degree, 90, 0, mCenterX, mCenterY);
			rotate3d3 = new Rotate3D(-90 + degree, -90, 0, mCenterX, mCenterY);
			rotate3d.setDuration(500);
			rotate3d2.setDuration(500);
			rotate3d3.setDuration(500);
			if (currentTab == 0) {
				layoutmain.startAnimation(rotate3d);
				layoutnext.startAnimation(rotate3d2);
				layoutlast.startAnimation(rotate3d3);
			} else if (currentTab == 1) {
				layoutnext.startAnimation(rotate3d);
				layoutlast.startAnimation(rotate3d2);
				layoutmain.startAnimation(rotate3d3);
			} else if (currentTab == 2) {
				layoutlast.startAnimation(rotate3d);
				layoutmain.startAnimation(rotate3d2);
				layoutnext.startAnimation(rotate3d3);
			}
		}

		System.out.println(">>>>>>>>degree:" + degree + " currentTab:"
				+ currentTab);
		setViewVisibile();
		degree = 0;

	}

	// 水平视图的隐藏显示
	private void setViewVisibile() {
		if (currentTab == 0) {
			layoutmain.setVisibility(View.VISIBLE);
			layoutnext.setVisibility(View.GONE);
			layoutlast.setVisibility(View.GONE);
			layoutdown.setVisibility(View.GONE);
			layoutup.setVisibility(View.GONE);
		} else if (currentTab == 1) {
			layoutmain.setVisibility(View.GONE);
			layoutnext.setVisibility(View.VISIBLE);
			layoutlast.setVisibility(View.GONE);
			layoutdown.setVisibility(View.GONE);
			layoutup.setVisibility(View.GONE);
		} else if (currentTab == 2) {
			layoutmain.setVisibility(View.GONE);
			layoutnext.setVisibility(View.GONE);
			layoutlast.setVisibility(View.VISIBLE);
			layoutdown.setVisibility(View.GONE);
			layoutup.setVisibility(View.GONE);
		}
	}

	private void setViewVisibileForVir() {
		if (currentTabV == 0) {
			layoutmain.setVisibility(View.VISIBLE);
			layoutnext.setVisibility(View.GONE);
			layoutlast.setVisibility(View.GONE);
			layoutdown.setVisibility(View.GONE);
			layoutup.setVisibility(View.GONE);
		} else if (currentTabV == 1) {
			layoutmain.setVisibility(View.GONE);
			layoutnext.setVisibility(View.GONE);
			layoutlast.setVisibility(View.GONE);
			layoutup.setVisibility(View.GONE);
			layoutdown.setVisibility(View.VISIBLE);
		} else if (currentTabV == 2) {
			layoutmain.setVisibility(View.GONE);
			layoutnext.setVisibility(View.GONE);
			layoutlast.setVisibility(View.GONE);
			layoutup.setVisibility(View.VISIBLE);
			layoutdown.setVisibility(View.GONE);
		}
	}

	// 水平
	private void doRotate(int dx) {
		float xd = degree;
		layoutnext.setVisibility(View.VISIBLE);
		layoutmain.setVisibility(View.VISIBLE);
		layoutlast.setVisibility(View.VISIBLE);

		degree += perDegree * dx;
		System.out.println(">>>>>>>>>degree:" + degree);
		rotate3d = new Rotate3D(xd, degree, 0, mCenterX, mCenterY);
		rotate3d2 = new Rotate3D(90 + xd, 90 + degree, 0, mCenterX, mCenterY);
		rotate3d3 = new Rotate3D(-90 + xd, -90 + degree, 0, mCenterX, mCenterY);
		if (currentTab == 0) {
			layoutmain.startAnimation(rotate3d);
			layoutnext.startAnimation(rotate3d2);
			layoutlast.startAnimation(rotate3d3);
		} else if (currentTab == 1) {
			layoutmain.startAnimation(rotate3d3);
			layoutnext.startAnimation(rotate3d);
			layoutlast.startAnimation(rotate3d2);
		} else if (currentTab == 2) {
			layoutmain.startAnimation(rotate3d2);
			layoutnext.startAnimation(rotate3d3);
			layoutlast.startAnimation(rotate3d);
		}
		rotate3d.setFillAfter(true);
		rotate3d2.setFillAfter(true);
		rotate3d3.setFillAfter(true);
	}
}
