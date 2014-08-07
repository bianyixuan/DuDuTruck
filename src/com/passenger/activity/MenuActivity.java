package com.passenger.activity;

import android.app.Activity;
import android.os.Bundle;

import com.passenger.R;

public class MenuActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ActivityManager mam = ActivityManager.getInstance();
		mam.pushOneActivity(MenuActivity.this);//activity º”»Î’ªƒ⁄
		setContentView(R.layout.menu_layout);
	}
}
