package com.passenger.activity;

import java.util.Stack;

import android.app.Activity;

public class ActivityManager {
	private static ActivityManager instance;
	private Stack<Activity> activityStack;// activityջ

	private ActivityManager() {
	}

	// ����ģʽ
	public static ActivityManager getInstance() {
		if (instance == null) {
			instance = new ActivityManager();
		}
		return instance;
	}

	// ��һ��activityѹ��ջ��
	public void pushOneActivity(Activity actvity) {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		activityStack.add(actvity);

	}

	// ��ȡջ����activity���Ƚ����ԭ��
	public Activity getLastActivity() {
		return activityStack.lastElement();
	}

	// �Ƴ�һ��activity
	public void popOneActivity(Activity activity) {
		if (activityStack != null && activityStack.size() > 0) {
			if (activity != null) {
				activity.finish();
				activityStack.remove(activity);
				activity = null;
			}
		}
	}

	// �˳�����activity
	public void finishAllActivity() {
		if (activityStack != null) {
			while (activityStack.size() > 0) {
				Activity activity = getLastActivity();
				if (activity == null)
					break;
				popOneActivity(activity);
			}
		}
	}
}