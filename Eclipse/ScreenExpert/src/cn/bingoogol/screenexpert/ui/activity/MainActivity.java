package cn.bingoogol.screenexpert.ui.activity;

import net.youmi.android.AdManager;
import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;
import net.youmi.android.spot.SpotManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import cn.bingoogol.screenexpert.R;
import cn.bingoogol.screenexpert.receiver.DeviceKeeperReceiver;
import cn.bingoogol.screenexpert.service.ScreenService;
import cn.bingoogol.screenexpert.service.ScreenService.ScreenAction;
import cn.bingoogol.screenexpert.ui.view.SettingView;
import cn.bingoogol.screenexpert.util.Constants;
import cn.bingoogol.screenexpert.util.Logger;
import cn.bingoogol.screenexpert.util.SpUtil;

import com.baidu.android.feedback.FeedbackManager;

public class MainActivity extends BaseActivity implements OnClickListener {
	private static final String TAG = MainActivity.class.getSimpleName();
	private DevicePolicyManager mDevicePolicyManager;
	private ComponentName mComponentName;
	private SettingView mShakeSv;
	private SettingView mOnekeySv;
	private SettingView mAdSv;
	private ScreenAction mScreenAction;
	private ServiceConnection mScreenServiceConn = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mScreenAction = (ScreenAction) service;
		}
	};

	@Override
	protected void initView() {
		setContentView(R.layout.activity_main);
		mShakeSv = (SettingView) findViewById(R.id.sv_main_shake);
		mOnekeySv = (SettingView) findViewById(R.id.sv_main_onekey);
		mAdSv = (SettingView) findViewById(R.id.sv_main_ad);
		if (SpUtil.getIsShowAd()) {
			showAd();
		}
	}

	private void showAd() {
		// 参数：appId, appSecret, 调试模式
		hideAd();
		AdManager.getInstance(this).init(Constants.youmi.APPID, Constants.youmi.APPSECRET, false);
		LinearLayout adLayout = (LinearLayout) findViewById(R.id.lv_main_ad);
		AdView adView = new AdView(this, AdSize.FIT_SCREEN);
		adLayout.addView(adView);
	}

	private void hideAd() {
		LinearLayout adLayout = (LinearLayout) findViewById(R.id.lv_main_ad);
		adLayout.removeAllViewsInLayout();
	}

	@Override
	protected void setListener() {
		mShakeSv.setOnClickListener(this);
		mOnekeySv.setOnClickListener(this);
		mAdSv.setOnClickListener(this);
		findViewById(R.id.btn_main_feedback).setOnClickListener(this);
	}

	@Override
	protected void afterViews(Bundle savedInstanceState) {
		startService(new Intent(this, ScreenService.class));
		bindService(new Intent(this, ScreenService.class), mScreenServiceConn, BIND_AUTO_CREATE);
		mDevicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
		mComponentName = new ComponentName(this, DeviceKeeperReceiver.class);

	}

	@Override
	protected void onStart() {
		super.onStart();
		mShakeSv.setChecked(SpUtil.getShakeUnlockScreen());
		mOnekeySv.setChecked(SpUtil.getOnekeyLockScreen());
		mAdSv.setChecked(SpUtil.getIsShowAd());
	}

	@Override
	protected void onStop() {
		// 如果不调用此方法，则按home键的时候会出现图标无法显示的情况。
		SpotManager.getInstance(this).disMiss(false);
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		SpotManager.getInstance(this).unregisterSceenReceiver();
		unbindService(mScreenServiceConn);
		super.onDestroy();
	}

	private void activeDeviceAdmin() {
		Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
		intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mComponentName);
		intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "必须激活屏幕专家为设备管理器之后才能实现一键锁屏功能");
		startActivity(intent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@SuppressWarnings("unused")
	private void removeDeviceAdmin() {
		boolean active = mDevicePolicyManager.isAdminActive(mComponentName);
		if (active) {
			mDevicePolicyManager.removeActiveAdmin(mComponentName);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sv_main_shake:
			if (mScreenAction != null) {
				if (mShakeSv.isChecked()) {
					mShakeSv.setChecked(false);
					mScreenAction.closeShakeUnlockScreen();
				} else {
					mShakeSv.setChecked(true);
					mScreenAction.openShakeUnlockScreen();
				}
			}
			break;
		case R.id.sv_main_onekey:
			if (mScreenAction != null) {
				if (mDevicePolicyManager.isAdminActive(mComponentName)) {
					if (mOnekeySv.isChecked()) {
						mOnekeySv.setChecked(false);
						mScreenAction.closeOnekeyLockScreen();
					} else {
						Logger.i(TAG, "开启一键锁屏");
						mOnekeySv.setChecked(true);
						mScreenAction.openOnekeyLockScreen();
					}
				} else {
					activeDeviceAdmin();
				}
			}
			break;
		case R.id.sv_main_ad:
			if (mAdSv.isChecked()) {
				hideAd();
				mAdSv.setChecked(false);
				SpUtil.putIsShowAd(false);
			} else {
				showAd();
				mAdSv.setChecked(true);
				SpUtil.putIsShowAd(true);
			}
			break;
		case R.id.btn_main_feedback:
			FeedbackManager.getInstance(this).startFeedbackActivity();
			break;
		default:
			break;
		}
	}
}