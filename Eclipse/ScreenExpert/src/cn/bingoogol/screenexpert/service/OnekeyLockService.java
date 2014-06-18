package cn.bingoogol.screenexpert.service;

import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import cn.bingoogol.screenexpert.R;
import cn.bingoogol.screenexpert.receiver.ScreenExpertReceiver;
import cn.bingoogol.screenexpert.util.Logger;
import cn.bingoogol.screenexpert.util.SpUtil;

public class OnekeyLockService extends Service {
	private static final String TAG = "OnekeyLockService";
	private WindowManager mWindowManager;
	private View mView;
	private LayoutParams mLayoutParams;

	@Override
	public void onCreate() {
		super.onCreate();
		Logger.i(TAG, "开启一键锁屏服务");
		mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
		showOnekeyView();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Logger.i(TAG, "关闭一键锁屏服务");
		dismissOnekeyView();
	}

	private void lockScreen() {
		Logger.i(TAG, "锁屏");
		DevicePolicyManager devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
		if (devicePolicyManager.isAdminActive(new ComponentName(this, ScreenExpertReceiver.class))) {
			devicePolicyManager.lockNow();
		}
	}

	private void showOnekeyView() {
		mView = View.inflate(this, R.layout.view_onkey_lock, null);
		mView.setOnTouchListener(new View.OnTouchListener() {
			int startX = 0;
			int startY = 0;

			@SuppressWarnings("deprecation")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					int newX = (int) event.getRawX();
					int newY = (int) event.getRawY();
					int dx = newX - startX;
					int dy = newY - startY;
					mLayoutParams.x += dx;
					mLayoutParams.y += dy;
					if (mLayoutParams.x < 0) {
						mLayoutParams.x = 0;
					}
					if (mLayoutParams.y < 0) {
						mLayoutParams.y = 0;
					}
					if (mLayoutParams.x > mWindowManager.getDefaultDisplay().getWidth()) {
						mLayoutParams.x = mWindowManager.getDefaultDisplay().getWidth();
					}
					if (mLayoutParams.y > mWindowManager.getDefaultDisplay().getHeight()) {
						mLayoutParams.y = mWindowManager.getDefaultDisplay().getHeight();
					}
					mWindowManager.updateViewLayout(mView, mLayoutParams);
					startX = newX;
					startY = newY;
					break;
				case MotionEvent.ACTION_UP:
					SpUtil.setOnekeyPosition(mLayoutParams.x, mLayoutParams.y);
					break;
				}
				return true;
			}
		});

		mView.findViewById(R.id.btn_onkey_lock).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				lockScreen();
			}
		});
		mLayoutParams = new LayoutParams();
		mLayoutParams.gravity = Gravity.LEFT + Gravity.TOP;
		mLayoutParams.x = SpUtil.getOnekeyX();
		mLayoutParams.y = SpUtil.getOnekeyY();
		mLayoutParams.height = LayoutParams.WRAP_CONTENT;
		mLayoutParams.width = LayoutParams.WRAP_CONTENT;
		mLayoutParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE;
		mLayoutParams.format = PixelFormat.TRANSLUCENT;
		mLayoutParams.type = LayoutParams.TYPE_SYSTEM_ALERT;
		mWindowManager.addView(mView, mLayoutParams);
		SpUtil.setOnekeyStatus(true);
	}

	private void dismissOnekeyView() {
		if (mView != null) {
			mWindowManager.removeView(mView);
			mView = null;
			SpUtil.setOnekeyStatus(false);
		}
	}
}
