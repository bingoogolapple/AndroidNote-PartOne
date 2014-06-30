package cn.bingoogol.screenexpert.ui.activity;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import cn.bingoogol.screenexpert.App;
import cn.bingoogol.screenexpert.R;
import cn.bingoogol.screenexpert.ui.view.BtnCallback;
import cn.bingoogol.screenexpert.ui.view.ConfirmDialog;
import cn.bingoogol.screenexpert.ui.view.PDialog;
import cn.bingoogol.screenexpert.util.ConnectivityUtil;
import cn.bingoogol.screenexpert.util.Constants;
import cn.bingoogol.screenexpert.util.Logger;
import cn.bingoogol.screenexpert.util.StorageUtil;
import cn.bingoogol.screenexpert.util.ToastUtil;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

public class SplashActivity extends BaseActivity {
	private static final String TAG = "SplashActivity";
	// 新版本apk文件路径
	private String mApkUrl;
	// 新版本名称
	private String mVersionName;

	private PDialog mPDialog;
	private boolean mIsLoadMainActivity = false;

	@Override
	protected void initView() {
		setContentView(R.layout.activity_splash);
	}

	@Override
	protected void setListener() {
	}

	@Override
	protected void afterViews(Bundle savedInstanceState) {
		((TextView) findViewById(R.id.tv_splash_versionName)).setText("Version " + mApp.getCurrentVersionName());
		Animation rocket = AnimationUtils.loadAnimation(mApp, R.anim.rocket);
		rocket.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				if (mIsLoadMainActivity) {
					loadMainActivity();
				}
			}
		});
		findViewById(R.id.iv_rocket).startAnimation(rocket);
		checkVersion();
	}

	private void checkVersion() {
		if (ConnectivityUtil.isWifiConnected() && StorageUtil.isExternalStorageWritable()) {
			new AsyncHttpClient().get(Constants.config.UPGRADE_URL, new JsonHttpResponseHandler("UTF-8") {
				@Override
				public void onSuccess(JSONObject jsonObject) {
					try {
						if (mApp.getCurrentVersionCode() < jsonObject.getInt("versionCode")) {
							mApkUrl = jsonObject.getString("apkUrl");
							mVersionName = jsonObject.getString("versionName");
							showUpgradDialog();
						} else {
							// 没有新版本，不用升级，进入主界面
							loadMainActivityDelay();
						}
					} catch (JSONException e) {
						// 解析升级信息失败，进入主界面
						Logger.e(TAG, "解析升级信息异常");
						loadMainActivityDelay();
					}
				}

				@Override
				public void onFailure(Throwable e, JSONObject errorResponse) {
					Logger.e(TAG, "获取升级信息失败");
					loadMainActivityDelay();
				}
			});
		} else {
			mIsLoadMainActivity = true;
		}
	}

	/**
	 * 延时加载主界面
	 */
	private void loadMainActivityDelay() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				loadMainActivity();
			}
		}, 1500);
	}

	/**
	 * 显示升级对话框
	 * 
	 * @param versionName
	 */
	public void showUpgradDialog() {
		StringBuilder sb = new StringBuilder();
		sb.append(getString(R.string.current_version_tips) + mApp.getCurrentVersionName() + "\n");
		sb.append(getString(R.string.new_version_tips) + mVersionName + "\n\n");
		sb.append(getString(R.string.whether_upgrade_tips));
		final ConfirmDialog confirmDialog = new ConfirmDialog(SplashActivity.this, R.string.find_new_version, sb.toString(), R.string.upgrade_later, R.string.upgrade_now);
		confirmDialog.setBtnCallback(new BtnCallback() {
			@Override
			public void onClickRight() {
				mPDialog = new PDialog(SplashActivity.this);
				mPDialog.show();
				upgrade();
			}

			@Override
			public void onClickLeft() {
				loadMainActivity();
			}
		});
		confirmDialog.show();
	}

	private void upgrade() {
		File apkFile = new File(StorageUtil.getDownloadDir(), Constants.file.NEW_APK_NAME);
		apkFile.deleteOnExit();
		new AsyncHttpClient().get(mApkUrl, new FileAsyncHttpResponseHandler(apkFile) {
			@Override
			public void onProgress(int bytesWritten, int totalSize) {
				mPDialog.setProgress(totalSize, bytesWritten, Math.round(((float) bytesWritten / totalSize) * 100) + "%");
			}

			@Override
			public void onSuccess(File file) {
				mPDialog.dismiss();
				install(file);
			}

			@Override
			public void onFailure(Throwable e, File response) {
				mPDialog.dismiss();
				response.deleteOnExit();
				Logger.e(TAG, "下载apk文件出错：" + e.getMessage());
				ToastUtil.makeText(R.string.download_apk_error);
				loadMainActivity();
			}
		});
	}

	/**
	 * 加载应用程序主界面
	 */
	private void loadMainActivity() {
		startActivity(new Intent(mApp, MainActivity.class));
		overridePendingTransition(R.anim.tran_next_in, R.anim.tran_next_out);
		finish();
	}

	private void install(File apkFile) {
		startActivity(App.getInstallApkIntent(apkFile));
		// 销毁当前应用
		finish();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}