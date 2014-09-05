package cn.bingoogol.dmc.service;

import android.app.Service;
import android.content.Intent;
import android.net.wifi.WifiManager.MulticastLock;
import android.os.IBinder;
import cn.bingoogol.dmc.engine.MediaStoreCenter;
import cn.bingoogol.util.CommonUtil;
import cn.bingoogol.util.DlnaUtils;
import cn.bingoogol.util.Logger;

import com.github.mediaserver.server.jni.DMSJniInterface;

public class DMSService extends Service {
	private static final String TAG = DMSService.class.getSimpleName();
	private DMSWorkThread mWorkThread;
	private MulticastLock mMulticastLock;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mWorkThread = new DMSWorkThread();
		MediaStoreCenter.getInstance().startScanMedia();
		// Android的Wifi默认不处理组播流量，所以很多需要组播支持的服务就没法用。当我们接收完组播信息之后，要记得调用release()方法释放组播锁，节省资源。
		mMulticastLock = CommonUtil.openWifiBrocast();
		startWorkThread();
	}

	@Override
	public void onDestroy() {
		if (mWorkThread != null) {
			mWorkThread.stopThread();
			mWorkThread = null;
		}
		if (mMulticastLock != null) {
			mMulticastLock.release();
			mMulticastLock = null;
		}
		MediaStoreCenter.getInstance().stopScanMedia();
		super.onDestroy();

	}

	private void startWorkThread() {
		mWorkThread.setParam(MediaStoreCenter.getInstance().getRootDir());
		if (mWorkThread.isAlive()) {
			mWorkThread.awakeThread();
		} else {
			mWorkThread.start();
		}
	}

	private class DMSWorkThread extends Thread {
		private static final int CHECK_INTERVAL = 30000;
		private boolean mIsStart = false;
		private boolean mStartSuccess = false;
		private String mRootdir = null;

		public void setParam(String rootDir) {
			mRootdir = rootDir;
		}

		@Override
		public void run() {
			mIsStart = true;
			while (mIsStart) {

				if (!mStartSuccess) {
					DMSJniInterface.stopServer();
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (startEngine()) {
						mStartSuccess = true;
					}
				}
				synchronized (this) {
					try {
						wait(CHECK_INTERVAL);
					} catch (Exception e) {
						Logger.e(TAG, e.getMessage());
					}
				}
			}
			DMSJniInterface.stopServer();
		}

		public boolean startEngine() {
			return DMSJniInterface.startServer(mRootdir, DlnaUtils.creat12BitUUID(), DlnaUtils.creat12BitUUID());
		}

		@SuppressWarnings("unused")
		public boolean restartEngine() {
			mStartSuccess = false;
			awakeThread();
			return true;
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