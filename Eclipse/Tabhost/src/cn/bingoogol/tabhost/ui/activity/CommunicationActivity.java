package cn.bingoogol.tabhost.ui.activity;

import cn.bingoogol.tabhost.R;
import cn.bingoogol.tabhost.util.Logger;
import android.os.Bundle;
import android.view.View;

public class CommunicationActivity extends BaseActivity {
	private static final String TAG = CommunicationActivity.class.getSimpleName();

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.activity_communication);
		Logger.i(TAG, "onCreate CommunicationActivity");
	}

	@Override
	protected void setListener() {

	}

	@Override
	protected void afterViews(Bundle savedInstanceState) {

	}

	@Override
	public void onClick(View v) {

	}

	@Override
	protected void onStart() {
		super.onStart();
		Logger.i(TAG, "onStart CommunicationActivity");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Logger.i(TAG, "onResume CommunicationActivity");
	}

	@Override
	protected void onResumeFragments() {
		super.onResumeFragments();
		Logger.i(TAG, "onResumeFragments CommunicationActivity");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Logger.i(TAG, "onPause CommunicationActivity");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Logger.i(TAG, "onStop CommunicationActivity");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Logger.i(TAG, "onDestroy CommunicationActivity");
	}
}
