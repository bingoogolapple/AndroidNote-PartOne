package cn.bingoogol.screenexpert.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import cn.bingoogol.screenexpert.util.Logger;

public class ShakeUnlockService extends Service {
	private static final String TAG = "ScreenExpertService";
	private ScreenLockReceiver mScreenLockReceiver;
	private ScreenUnLockReceiver mScreenUnLockReceiver;
	private SensorManager mSensorManager;
	private long mLastShakeTime = 0;

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
				// 一秒以内连续两次摇晃手机才点亮屏幕
				if (System.currentTimeMillis() - mLastShakeTime < 1000) {
					Logger.d(TAG, "点亮屏幕");
					wakeupScreen();
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

	@SuppressWarnings("deprecation")
	private void wakeupScreen() {
		PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
		WakeLock wakeLock = powerManager.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, TAG);
		wakeLock.acquire();
		wakeLock.release();
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Logger.i(TAG, "开启屏幕专家服务");
		mScreenLockReceiver = new ScreenLockReceiver();
		registerReceiver(mScreenLockReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
		mScreenUnLockReceiver = new ScreenUnLockReceiver();
		registerReceiver(mScreenUnLockReceiver, new IntentFilter(Intent.ACTION_SCREEN_ON));

		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Logger.i(TAG, "停止屏幕专家服务");
		unregisterReceiver(mScreenLockReceiver);
		mScreenLockReceiver = null;
		unregisterReceiver(mScreenUnLockReceiver);
		mScreenUnLockReceiver = null;
		mSensorManager.unregisterListener(mSensorEventListener);
	}

	private class ScreenLockReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Logger.i(TAG, "屏幕锁定，注册传感器");
			mSensorManager.registerListener(mSensorEventListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
		}

	}

	private class ScreenUnLockReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Logger.i(TAG, "屏幕解锁，取消注册传感器");
			mSensorManager.unregisterListener(mSensorEventListener);
		}

	}

}
