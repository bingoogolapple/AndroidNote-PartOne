package cn.bingoogol.screenexpert.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;
import cn.bingoogol.screenexpert.R;
import cn.bingoogol.screenexpert.service.OnekeyLockService;
import cn.bingoogol.screenexpert.service.ShakeUnlockService;
import cn.bingoogol.screenexpert.util.Logger;
import cn.bingoogol.screenexpert.util.ServiceStatusUtils;
import cn.bingoogol.screenexpert.util.SpUtil;

public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";
	private ToggleButton tb_shake;
	private ToggleButton tb_onkey;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SpUtil.init(this);
		setContentView(R.layout.activity_main);
		tb_shake = (ToggleButton) findViewById(R.id.tb_shake);
		tb_shake.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					MainActivity.this.startService(new Intent(MainActivity.this, ShakeUnlockService.class));
				} else {
					MainActivity.this.stopService(new Intent(MainActivity.this, ShakeUnlockService.class));
				}
			}
		});
		tb_onkey = (ToggleButton) findViewById(R.id.tb_onkey);
		tb_onkey.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				if (isChecked) {
					Logger.i(TAG, "开启一键锁屏服务");
					MainActivity.this.startService(new Intent(MainActivity.this, OnekeyLockService.class));
				} else {
					MainActivity.this.stopService(new Intent(MainActivity.this, OnekeyLockService.class));
				}
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		tb_shake.setChecked(ServiceStatusUtils.isServiceRunning(this, ShakeUnlockService.class.getName()));
		tb_onkey.setChecked(ServiceStatusUtils.isServiceRunning(this, OnekeyLockService.class.getName()));
	}

}