package com.passenger.activity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.passenger.R;
import com.passenger.util.HttpUtil;
import com.passenger.util.ProgressDialogUtil;
import com.passenger.util.constants;

public class GoodsOrderActivity extends Activity {
	public static final String GOODSURL = "goods";
	private EditText goodsSendTime, goodsSendFrom, goodsSendTo, goodsDetail,
			wordToDriver;
	Button btn_sureSend;
	ProgressDialogUtil dialogUtil;

	private void init() {
		goodsDetail = (EditText) findViewById(R.id.goodsDetail);
		goodsSendFrom = (EditText) findViewById(R.id.goodsSendFrom);
		goodsSendTime = (EditText) findViewById(R.id.goodsSendTime);
		goodsSendTo = (EditText) findViewById(R.id.goodsSendTo);
		wordToDriver = (EditText) findViewById(R.id.wordToDriver);
		btn_sureSend = (Button) findViewById(R.id.btn_sureSend);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.goodsorder_layout);
		init();
		final String phone = getIntent().getStringExtra("phone");
		btn_sureSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialogUtil = ProgressDialogUtil
						.getInstance(GoodsOrderActivity.this);
				dialogUtil.showDialog("正在发送订单...");
				String detail = goodsDetail.getText().toString();
				String from = goodsSendFrom.getText().toString();
				String to = goodsSendTo.getText().toString();
				String time = goodsSendTime.getText().toString();
				String word = wordToDriver.getText().toString();
				final Map<String, String> params = new HashMap<String, String>();
				params.put("detail", detail);
				params.put("from", from);
				params.put("to", to);
				params.put("time", time);
				params.put("word", word);
				params.put("passengerPhone", phone);
				final String url = constants.BaseUrl + GOODSURL;

				try {
					new Thread(new Runnable() {

						@Override
						public void run() {
							try {
								HttpUtil.postUrl(url, params);
							} catch (ClientProtocolException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}).start();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				handler.sendEmptyMessage(0);

			}
		});
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Toast.makeText(GoodsOrderActivity.this, "订单已发送", Toast.LENGTH_LONG)
					.show();
			dialogUtil.closeDialog();
		};
	};
}
