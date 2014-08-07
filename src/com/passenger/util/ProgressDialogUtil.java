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

	//��
	public void showDialog(String msg) {
		dialog = new ProgressDialog(context);
		dialog.setMessage(msg);
		dialog.show();
	}

	//�رս��ȿ�
	public void closeDialog() {
		if (dialog != null) {
			dialog.dismiss();
		}
	}

}
