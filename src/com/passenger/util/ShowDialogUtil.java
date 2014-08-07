package com.passenger.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.passenger.R;
import com.passenger.activity.PhoneCheckActivity;
import com.passenger.domain.DriverInfo;
import com.passenger.logic.DriverInfoJsonParse;
import com.passenger.logic.SendFormParamTask;

public class ShowDialogUtil {

	private Context context;

	private ShowDialogUtil(Context context) {
		this.context = context;
	}

	public static ShowDialogUtil getInstance(Context context) {
		return new ShowDialogUtil(context);
	}

	// 弹出框，显示司机的信息
	public void showDialog(final DriverInfo driverInfo, final String phone,
			int status, double distance) {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		View view = LayoutInflater.from(context).inflate(
				R.layout.driverinfo_dialog_layout, null);
		TextView name = (TextView) view.findViewById(R.id.dialog_drivername);
		TextView number = (TextView) view
				.findViewById(R.id.dialog_drivernumber);
		TextView driverphone = (TextView) view
				.findViewById(R.id.dialog_driverphone);
		TextView type = (TextView) view
				.findViewById(R.id.dialog_drivertrucktype);
		TextView stutas = (TextView) view
				.findViewById(R.id.dialog_driverstatus);
		TextView driverdistance = (TextView) view
				.findViewById(R.id.dialog_distance);

		name.setText(driverInfo.getName());
		number.setText(driverInfo.getNumber());
		driverphone.setText(phone);
		type.setText(driverInfo.getTruck_type());
		stutas.setText((1 == status) ? "空闲" : "忙碌");
		driverdistance.setText(distance + "米");
		builder.setView(view);
		builder.setTitle("司机信息");
		builder.setPositiveButton("立即拨打",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent phoneIntent = new Intent(
								"android.intent.action.CALL", Uri.parse("tel:"
										+ phone));
						context.startActivity(phoneIntent);
					}
				});
		builder.setNegativeButton("取消", null);
		builder.create();
		builder.show();
	}

	/**
	 * 验证手机号码的弹出框
	 */
	public void checkPhoneDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("提示");
		builder.setMessage("预约货车前需要验证手机号码，是否立即验证？");
		builder.setPositiveButton("是", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				context.startActivity(new Intent(context,
						PhoneCheckActivity.class));
			}
		});
		builder.setNegativeButton("否", null);
		builder.show();
	}

}
