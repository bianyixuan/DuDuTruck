package com.passenger.util;

import android.app.ProgressDialog;
import android.content.Context;

public class ProgressDialogUtil {

	private Context context;
	ProgressDialog dialog;

	private ProgressDialogUtil(Context context) {
		this.context = context;
	}

	public static ProgressDialogUtil getInstance(Context context) {
		return new ProgressDialogUtil(context);
	}

	//打开
	public void showDialog(String msg) {
		dialog = new ProgressDialog(context);
		dialog.setMessage(msg);
		dialog.show();
	}

	//关闭进度框
	public void closeDialog() {
		if (dialog != null) {
			dialog.dismiss();
		}
	}

}
