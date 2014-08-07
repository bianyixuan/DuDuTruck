package com.passenger.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

import com.passenger.R;

public class SettingActivity extends Activity {
	private Button setting_bt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_layout);
		setting_bt = (Button) findViewById(R.id.setting_bt);
//		setting_bt.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Toast.makeText(SettingActivity.this, "点击事件响应了", 1000).show();
//				
//			}
//		});
	}
}
