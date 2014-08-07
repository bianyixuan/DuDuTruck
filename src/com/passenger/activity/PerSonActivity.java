package com.passenger.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.passenger.R;

public class PerSonActivity extends Activity {
	private Button person_bt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personinfo);
		person_bt = (Button) findViewById(R.id.person_bt);
		person_bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(PerSonActivity.this, "点击事件响应了", 1000).show();
			}
		});
	}
}
