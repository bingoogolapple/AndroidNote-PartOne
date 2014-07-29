package cn.bingoogol.bingo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.LinkedList;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Process;
import cn.bingoogol.bingo.util.Constants;
import cn.bingoogol.bingo.util.DateUtil;
import cn.bingoogol.bingo.util.Logger;
import cn.bingoogol.bingo.util.SpUtil;
import cn.bingoogol.bingo.util.StorageUtil;

public class App extends Application {
	private static final String TAG = App.class.getSimpleName();
	private static App sInstance;
	private static LinkedList<Activity> sActivities = new LinkedList<Activity>();

	@Override
	public void onCreate() {
		super.onCreate();
		sInstance = this;
		SpUtil.init();
		Thread.setDefaultUncaughtExceptionHandler(CrashHandler.getInstance());
	}

	public static App getInstance() {
		return sInstance;
	}

	public static void addActivity(Activity activity) {
		sActivities.add(activity);
	}

	public static void removeActivity(Activity activity) {
		sActivities.remove(activity);
	}

	public static void exit() {
		Activity activity;
		while (sActivities.size() != 0) {
			activity = sActivities.poll();
			if (!activity.isFinishing()) {
				activity.finish();
			}
		}
	}

	/**
	 * 获取当前版本名称
	 * 
	 * @return
	 */
	public String getCurrentVersionName() {
		try {
			return getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
		} catch (Exception e) {
			// 利用系统api getPackageName()得到的包名，这个异常根本不可能发生
			return null;
		}
	}

	/**
	 * 获取当前版本号
	 * 
	 * @return
	 */
	public int getCurrentVersionCode() {
		try {
			return getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
		} catch (Exception e) {
			// 利用系统api getPackageName()得到的包名，这个异常根本不可能发生
			return 0;
		}
	}

	/**
	 * 获取安装apk文件的意图对象
	 * 
	 * @param apkFile
	 *            要安装的apk文件
	 * @return
	 */
	public static Intent getInstallApkIntent(File apkFile) {
		Intent installApkIntent = new Intent();
		installApkIntent.setAction(Intent.ACTION_VIEW);
		installApkIntent.addCategory(Intent.CATEGORY_DEFAULT);
		installApkIntent.setDataAndType(Uri.fromFile(apkFile), Constants.mime.APK);
		return installApkIntent;
	}

	private static class CrashHandler implements UncaughtExceptionHandler {
		private static CrashHandler sSingleton;

		private CrashHandler() {
		}

		public static CrashHandler getInstance() {
			// 单例模式之双重检测
			if (sSingleton == null) {
				// [1]
				synchronized (CrashHandler.class) {
					// 线程一到此之前线程二到达了位置[1]，如果此处不二次判断，那么线程二到达这里的时候还会重新new
					if (sSingleton == null) {
						sSingleton = new CrashHandler();
					}
				}
			}
			return sSingleton;
		}

		@Override
		public void uncaughtException(Thread thread, Throwable ex) {
			FileWriter fw = null;
			try {
				File file = new File(StorageUtil.getFeedbackDir(), DateUtil.dateToDayString(new Date()) + ".log");
				boolean flag = file.exists();
				// 这行执行完，file就存在了，所以得在这之前判断文件是否已经存在
				fw = new FileWriter(file, true);
				if (!flag) {
					fw.write("当前应用版本：" + sInstance.getCurrentVersionName() + "\n");
					fw.write("当前设备信息：\n");
					fw.write(getMobileInfo());
					fw.write("----------------------------------------------------------------------------\n");
				}
				fw.write(getErrorInfo(ex));
				fw.write("----------------------------------------------------------------------------\n");
				fw.flush();
				// TODO 正式发布后删掉
				ex.printStackTrace();
			} catch (Exception e) {
				Logger.e(TAG, e.getMessage());
			} finally {
				if (fw != null) {
					try {
						fw.close();
					} catch (IOException e) {
						Logger.e(TAG, e.getMessage());
					}
				}
			}
			Process.killProcess(Process.myPid());
			// TODO 定期上传错误日志到服务器
		}

		private String getErrorInfo(Throwable throwable) {
			Writer writer = new StringWriter();
			PrintWriter pw = new PrintWriter(writer);
			throwable.printStackTrace(pw);
			pw.close();
			return writer.toString();
		}

		private String getMobileInfo() {
			StringBuffer sb = new StringBuffer();
			// 通过反射获取系统的硬件信息
			try {
				Field[] fields = Build.class.getDeclaredFields();
				for (Field field : fields) {
					// 暴力反射 ,获取私有的信息
					field.setAccessible(true);
					sb.append(field.getName() + "=" + field.get(null).toString());
					sb.append("\n");
				}
			} catch (Exception e) {
				Logger.e(TAG, e.getMessage());
			}
			return sb.toString();
		}

	}
}
