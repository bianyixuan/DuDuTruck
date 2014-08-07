package com.passenger.activity;

import com.passenger.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PhoneCheckActivity extends Activity {

	EditText et_phone;
	Button btn_sure;

	private void init() {
		et_phone = (EditText) findViewById(R.id.et_phone);
		btn_sure = (Button) findViewById(R.id.btn_sure);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.phonecheck_layout);
		init();
		btn_sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String phone = et_phone.getText().toString();
				saveSharePrefrence(phone);
				Toast.makeText(PhoneCheckActivity.this, "验证成功！",
						Toast.LENGTH_LONG).show();
				finish();
			}
		});
	}

	private void saveSharePrefrence(String phone) {
		SharedPreferences spf = getSharedPreferences("passengerPhone",
				MODE_PRIVATE);
		Editor editor = spf.edit();
		editor.putString("phone", phone);
		editor.commit();
	}
}
