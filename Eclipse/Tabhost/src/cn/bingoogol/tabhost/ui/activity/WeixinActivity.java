package cn.bingoogol.tabhost.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import cn.bingoogol.tabhost.R;
import cn.bingoogol.tabhost.ui.fragment.OneFragment;
import cn.bingoogol.tabhost.ui.fragment.RightFragment;
import cn.bingoogol.tabhost.ui.fragment.ThreeFragment;
import cn.bingoogol.tabhost.ui.fragment.TwoFragment;
import cn.bingoogol.tabhost.util.Logger;

public class WeixinActivity extends BaseActivity {
	private static final String TAG = WeixinActivity.class.getSimpleName();
	@SuppressWarnings("rawtypes")
	private static final Class[] fragments = { OneFragment.class, TwoFragment.class, ThreeFragment.class, RightFragment.class };
	private FragmentTabHost mTabHost = null;
	private RadioGroup mTabRg = null;

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.activity_weixin);
		Logger.i(TAG, "onCreate WeixinActivity");
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
		for (int i = 0; i < fragments.length; i++) {
			mTabHost.addTab(mTabHost.newTabSpec(i + "").setIndicator(i + ""), fragments[i], null);
		}
		mTabRg = (RadioGroup) findViewById(R.id.rg_weixin_tab);
	}

	@Override
	protected void setListener() {
		mTabRg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				Logger.i(TAG, "onCheckedChanged");
				switch (checkedId) {
				case R.id.rb_weixin_one:
					mTabHost.setCurrentTab(0);
					break;
				case R.id.rb_weixin_two:
					// 不能用mTabRg.check(R.id.rb_weixin_one)，会调用出发两次onCheckedChanged
					// ((RadioButton) findViewById(R.id.rb_weixin_two)).setChecked(false);
					mTabHost.setCurrentTab(1);
					break;
				case R.id.rb_weixin_three:
					mTabHost.setCurrentTab(2);
					break;
				case R.id.rb_weixin_right:
					mTabHost.setCurrentTab(3);
					break;
				default:
					break;
				}
			}
		});
	}

	@Override
	protected void afterViews(Bundle savedInstanceState) {
		mTabHost.setCurrentTab(0);
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
