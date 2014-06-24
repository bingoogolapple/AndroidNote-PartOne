package cn.bingoogol.screenexpert.ui;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;
import cn.bingoogol.screenexpert.R;
import cn.bingoogol.screenexpert.receiver.DeviceKeeperReceiver;
import cn.bingoogol.screenexpert.service.ScreenService;
import cn.bingoogol.screenexpert.service.ScreenService.ScreenAction;
import cn.bingoogol.screenexpert.util.Logger;
import cn.bingoogol.screenexpert.util.SpUtil;

public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";
	private DevicePolicyManager mDevicePolicyManager;
	private ComponentName mComponentName;
	private ToggleButton tb_shake;
	private ToggleButton tb_onkey;
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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initListener();
		afterViews();
	}

	protected void initView() {
		setContentView(R.layout.activity_main);
		tb_shake = (ToggleButton) findViewById(R.id.tb_shake);
		tb_onkey = (ToggleButton) findViewById(R.id.tb_onkey);
	}

	protected void initListener() {
		tb_shake.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (mScreenAction != null) {
					if (isChecked) {
						mScreenAction.openShakeUnlockScreen();
					} else {
						mScreenAction.closeShakeUnlockScreen();
					}
				}
			}
		});
		tb_onkey.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (mScreenAction != null) {
					if (mDevicePolicyManager.isAdminActive(mComponentName)) {
						if (isChecked) {
							Logger.i(TAG, "开启一键锁屏服务");
							mScreenAction.openOnekeyLockScreen();
						} else {
							mScreenAction.closeOnekeyLockScreen();
						}
					} else {
						activeDeviceAdmin();
					}
				}
			}
		});
	}

	protected void afterViews() {
		bindService(new Intent(this, ScreenService.class), mScreenServiceConn, BIND_AUTO_CREATE);
		mDevicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
		mComponentName = new ComponentName(this, DeviceKeeperReceiver.class);
	}

	@Override
	protected void onStart() {
		super.onStart();
		tb_shake.setChecked(SpUtil.getShakeUnlockScreen());
		tb_onkey.setChecked(SpUtil.getOnekeyLockScreen());
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindService(mScreenServiceConn);
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

}