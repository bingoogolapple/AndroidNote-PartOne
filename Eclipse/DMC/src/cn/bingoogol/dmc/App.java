package cn.bingoogol.dmc;

import java.util.LinkedList;

import android.app.Activity;
import android.app.Application;
import cn.bingoogol.util.Logger;
import cn.bingoogol.util.ToastUtil;

public class App extends Application {
	private static final String TAG = App.class.getSimpleName();
	private static App mInstance;
	private static LinkedList<Activity> mActivities = new LinkedList<Activity>();

	@Override
	public void onCreate() {
		super.onCreate();
		Logger.i(TAG, "数字媒体客户端启动");
		ToastUtil.init(this);
		mInstance = this;
	}

	public static App getInstance() {
		return mInstance;
	}

	public static void addActivity(Activity activity) {
		mActivities.add(activity);
	}

	public static void removeActivity(Activity activity) {
		mActivities.remove(activity);
	}

	public static void exit() {
		Activity activity;
		while (mActivities.size() != 0) {
			activity = mActivities.poll();
			if (!activity.isFinishing()) {
				activity.finish();
			}
		}
		System.exit(0);
	}
}
