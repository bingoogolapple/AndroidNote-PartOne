package cn.bingoogol.screenexpert.service;

import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import cn.bingoogol.screenexpert.R;
import cn.bingoogol.screenexpert.receiver.DeviceKeeperReceiver;
import cn.bingoogol.screenexpert.util.Logger;
import cn.bingoogol.screenexpert.util.SpUtil;

@SuppressWarnings("deprecation")
public class ScreenService extends Service {
	private static final String TAG = ScreenService.class.getSimpleName();

	private DevicePolicyManager mDevicePolicyManager;
	private WindowManager mWindowManager;
	private View mView;
	private LayoutParams mLayoutParams;
	private SensorManager mSensorManager;
	private IntentFilter mScreenActionIntentFilter;

	private ScreenActionBinder mScreenActionBinder;
	private WakeLock mWakeLock;
	private long mLastShakeTime = 0;

	private BroadcastReceiver mScreenActionReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Logger.i(TAG, "屏幕锁定，注册传感器");
			if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
				mSensorManager.registerListener(mSensorEventListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
			} else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
				mSensorManager.unregisterListener(mSensorEventListener);
			}
		}
	};

	private BroadcastReceiver mDeviceAdminReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction() == DeviceKeeperReceiver.REMOVE_DEVICE_ADMIN) {
				mScreenActionBinder.closeOnekeyLockScreen();
			} else if (intent.getAction() == DeviceKeeperReceiver.ADD_DEVICE_ADMIN) {
				mScreenActionBinder.openOnekeyLockScreen();
			}
		}
	};

	private SensorEventListener mSensorEventListener = new SensorEventListener() {
		@Override
		public void onSensorChanged(SensorEvent event) {
			float[] values = event.values;
			// x轴方向的重力加速度，向右为正
			float x = values[0];
			// y轴方向的重力加速度，向前为正
			float y = values[1];
			// z轴方向的重力加速度，向上为正
			float z = values[2];
			int medumValue = 17;
			if (Math.abs(x) > medumValue || Math.abs(y) > medumValue || Math.abs(z) > medumValue) {
				if (System.currentTimeMillis() - mLastShakeTime < 2000) {
					Logger.d(TAG, "点亮屏幕");
					mWakeLock.acquire();
					mWakeLock.release();
				} else {
					Logger.d(TAG, "摇晃了一次屏幕");
					mLastShakeTime = System.currentTimeMillis();
				}
			}
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}
	};

	@Override
	public void onCreate() {
		super.onCreate();
		Logger.i(TAG, "onCreate");
		mScreenActionBinder = new ScreenActionBinder();
		mDevicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
		mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mScreenActionIntentFilter = new IntentFilter();
		mScreenActionIntentFilter.addAction(Intent.ACTION_SCREEN_OFF);
		mScreenActionIntentFilter.addAction(Intent.ACTION_SCREEN_ON);

		PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
		mWakeLock = powerManager.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, TAG);

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(DeviceKeeperReceiver.ADD_DEVICE_ADMIN);
		intentFilter.addAction(DeviceKeeperReceiver.REMOVE_DEVICE_ADMIN);
		registerReceiver(mDeviceAdminReceiver, intentFilter);

		if (SpUtil.getOnekeyLockScreen()) {
			mScreenActionBinder.openOnekeyLockScreen();
		}
		if (SpUtil.getShakeUnlockScreen()) {
			mScreenActionBinder.openShakeUnlockScreen();
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mScreenActionBinder;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Logger.i(TAG, "onDestroy");
		mScreenActionBinder.closeOnekeyLockScreen();
		mScreenActionBinder.closeShakeUnlockScreen();
		unregisterReceiver(mDeviceAdminReceiver);
	}

	private void initOnekeyLockScreenView() {
		mLayoutParams = new LayoutParams();
		mLayoutParams.gravity = Gravity.LEFT + Gravity.TOP;
		mLayoutParams.x = SpUtil.getOnekeyX();
		mLayoutParams.y = SpUtil.getOnekeyY();
		mLayoutParams.height = LayoutParams.WRAP_CONTENT;
		mLayoutParams.width = LayoutParams.WRAP_CONTENT;
		mLayoutParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE;
		mLayoutParams.format = PixelFormat.TRANSLUCENT;
		mLayoutParams.type = LayoutParams.TYPE_SYSTEM_ALERT;
		mView = View.inflate(this, R.layout.view_onkey_lock, null);
		mView.setOnTouchListener(new View.OnTouchListener() {
			int startX = 0;
			int startY = 0;

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
				Logger.i(TAG, "锁屏");
				mDevicePolicyManager.lockNow();
			}
		});
	}

	private class ScreenActionBinder extends Binder implements ScreenAction {

		@Override
		public void openOnekeyLockScreen() {
			if (mView == null) {
				initOnekeyLockScreenView();
			}
			mWindowManager.addView(mView, mLayoutParams);
			SpUtil.putOnekeyLockScreen(true);
		}

		@Override
		public void closeOnekeyLockScreen() {
			if (mView != null) {
				mWindowManager.removeView(mView);
				SpUtil.putOnekeyLockScreen(false);
				mView = null;
			}
		}

		@Override
		public void openShakeUnlockScreen() {
			registerReceiver(mScreenActionReceiver, mScreenActionIntentFilter);
			SpUtil.putShakeUnlockScreen(true);
		}

		@Override
		public void closeShakeUnlockScreen() {
			unregisterReceiver(mScreenActionReceiver);
			mSensorManager.unregisterListener(mSensorEventListener);
			SpUtil.putShakeUnlockScreen(false);
		}
	}

	public interface ScreenAction {
		public void openOnekeyLockScreen();

		public void closeOnekeyLockScreen();

		public void openShakeUnlockScreen();

		public void closeShakeUnlockScreen();
	}
}
