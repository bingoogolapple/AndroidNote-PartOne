package cn.bingoogol.dmc.service;

import org.cybergarage.upnp.ControlPoint;
import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.device.DeviceChangeListener;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.IBinder;
import cn.bingoogol.dmc.App;
import cn.bingoogol.dmc.engine.ItemHelper;
import cn.bingoogol.dmc.model.MediaRenderDevices;
import cn.bingoogol.dmc.model.MediaServerDevices;
import cn.bingoogol.util.Logger;

public class DMCService extends Service {
	private static final String TAG = DMCService.class.getSimpleName();

	private SearchThread mSearchThread;
	private BroadcastReceiver mWifiStateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context c, Intent intent) {
			Bundle bundle = intent.getExtras();
			int statusInt = bundle.getInt("wifi_state");
			switch (statusInt) {
			case WifiManager.WIFI_STATE_ENABLED:
				startSearchThread();
				break;
			default:
				break;
			}
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mSearchThread = new SearchThread();
		registerReceiver(mWifiStateReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mSearchThread != null) {
			mSearchThread.stopThread();
			mSearchThread = null;
		}
		if (mWifiStateReceiver != null) {
			unregisterReceiver(mWifiStateReceiver);
			mWifiStateReceiver = null;
		}
		MediaRenderDevices.getInstance().clear();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		startSearchThread();
		return super.onStartCommand(intent, flags, startId);
	}

	private void startSearchThread() {
		if (mSearchThread != null) {
			// 设置已经搜索次数为0，促使刚开始的时候缩短搜索时间
			mSearchThread.setSearcTimes(0);
		} else {
			mSearchThread = new SearchThread();
		}
		if (mSearchThread.isAlive()) {
			mSearchThread.awakeThread();
		} else {
			mSearchThread.start();
		}
	}

	private class SearchThread extends Thread {
		private static final int mFastInternalTime = 5000;
		private static final int mNormalInternalTime = 3600000;
		private boolean mIsStart = false;
		private boolean mStartComplete = false;
		private ControlPoint mControlPoint;
		private int mSearchTimes = 0;
		private DeviceChangeListener mDeviceChangeListener = new DeviceChangeListener() {
			@Override
			public void deviceRemoved(Device device) {
				MediaRenderDevices.getInstance().removeDevice(device);
			}

			@Override
			public void deviceAdded(Device device) {
				if ("urn:schemas-upnp-org:device:MediaServer:1".equals(device.getDeviceType())) {
					MediaServerDevices.getInstance().addDevice(device);
					// TODO ?
					App.getInstance().sendBroadcast(new Intent(ItemHelper.NEW_DEVICES_FOUND));
				} else {
					MediaRenderDevices.getInstance().addDevice(device);
				}
			}
		};

		public SearchThread() {
			super();
			mControlPoint = new ControlPoint();
			mControlPoint.addDeviceChangeListener(mDeviceChangeListener);
		}

		@Override
		public void run() {
			mIsStart = true;
			while (mIsStart) {
				try {
					if (mStartComplete) {
						mControlPoint.search();
					} else {
						mControlPoint.stop();
						if (mControlPoint.start()) {
							mStartComplete = true;
						}
					}
					synchronized (this) {
						// 节约用电
						mSearchTimes++;
						if (mSearchTimes >= 5) {
							wait(mNormalInternalTime);
						} else {
							wait(mFastInternalTime);
						}
					}
				} catch (Exception e) {
					Logger.e(TAG, e.getMessage());
				}
			}
			mControlPoint.stop();
		}

		public synchronized void setSearcTimes(int searchTimes) {
			mSearchTimes = searchTimes;
		}

		public void awakeThread() {
			synchronized (this) {
				notifyAll();
			}
		}

		public void stopThread() {
			// 先把状态置为false，然后再唤醒，使其run方法运行结束
			mIsStart = false;
			awakeThread();
		}
	}
}
