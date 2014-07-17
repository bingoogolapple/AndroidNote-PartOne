package cn.bingoogol.tabhost.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;
import cn.bingoogol.tabhost.R;
import cn.bingoogol.tabhost.ui.fragment.OneFragment;
import cn.bingoogol.tabhost.ui.fragment.ThreeFragment;
import cn.bingoogol.tabhost.ui.fragment.TwoFragment;
import cn.bingoogol.tabhost.util.Logger;

public class VerticalActivity extends BaseActivity {
	private static final String TAG = VerticalActivity.class.getSimpleName();
	private FragmentTabHost mTabHost = null;

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.activity_vertical);
		Logger.i(TAG, "onCreate VerticalActivity");
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
		// 添加tab名称和图标
		TabHost.TabSpec tab1 = mTabHost.newTabSpec("tab1");
		TabHost.TabSpec tab2 = mTabHost.newTabSpec("tab2");
		TabHost.TabSpec tab3 = mTabHost.newTabSpec("tab3");
		tab1.setIndicator(getIndicatorView(R.drawable.ic_launcher, "标签一"));
		tab2.setIndicator(getIndicatorView(R.drawable.ic_launcher, "标签二"));
		tab3.setIndicator(getIndicatorView(R.drawable.ic_launcher, "标签三"));
		mTabHost.addTab(tab1, OneFragment.class, null);
		mTabHost.addTab(tab2, TwoFragment.class, null);
		mTabHost.addTab(tab3, ThreeFragment.class, null);
	}

	private View getIndicatorView(int icon, String name) {
		View view = View.inflate(this, R.layout.item_tab_vertical, null);
		view.findViewById(R.id.iv_icon).setBackgroundResource(icon);
		TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
		tv_name.setText(name);
		return view;
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
		Logger.i(TAG, "onStart VerticalActivity");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Logger.i(TAG, "onResume VerticalActivity");
	}

	@Override
	protected void onResumeFragments() {
		super.onResumeFragments();
		Logger.i(TAG, "onResumeFragments VerticalActivity");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Logger.i(TAG, "onPause VerticalActivity");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Logger.i(TAG, "onStop VerticalActivity");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Logger.i(TAG, "onDestroy VerticalActivity");
	}
}
