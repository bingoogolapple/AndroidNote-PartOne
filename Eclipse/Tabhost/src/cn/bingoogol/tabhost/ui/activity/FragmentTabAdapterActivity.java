package cn.bingoogol.tabhost.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.RadioGroup;
import cn.bingoogol.tabhost.R;
import cn.bingoogol.tabhost.ui.activity.TabFragmentAdapter.OnTabChangerListener;
import cn.bingoogol.tabhost.ui.fragment.OneFragment;
import cn.bingoogol.tabhost.ui.fragment.RightFragment;
import cn.bingoogol.tabhost.ui.fragment.ThreeFragment;
import cn.bingoogol.tabhost.ui.fragment.TwoFragment;
import cn.bingoogol.tabhost.util.Logger;

public class FragmentTabAdapterActivity extends BaseActivity {
	private static final String TAG = FragmentTabAdapterActivity.class.getSimpleName();
	private List<Fragment> mFragments = new ArrayList<Fragment>();
	private RadioGroup mTabRg = null;
	private TabFragmentAdapter mTabFragmentAdapter;

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.activity_fragment_tab_adapter);
		Logger.i(TAG, "onCreate FragmentAdapterActivity");

		mFragments.add(new OneFragment());
		mFragments.add(new TwoFragment());
		mFragments.add(new ThreeFragment());
		mFragments.add(new RightFragment());
		mTabRg = (RadioGroup) findViewById(R.id.rg_weixin_tab);

		mTabFragmentAdapter = new TabFragmentAdapter(this, R.id.realtabcontent, mFragments, mTabRg);
		mTabFragmentAdapter.setOnTabChangeListener(new OnTabChangerListener() {

			@Override
			public boolean onBeforeChange(int oldCheckedId, int newCheckedId) {
				if (newCheckedId == R.id.rb_weixin_three) {
					return true;
				}
				return false;
			}
		});
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
		Logger.i(TAG, "onStart FragmentAdapterActivity");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Logger.i(TAG, "onResume FragmentAdapterActivity");
	}

	@Override
	protected void onResumeFragments() {
		super.onResumeFragments();
		Logger.i(TAG, "onResumeFragments FragmentAdapterActivity");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Logger.i(TAG, "onPause FragmentAdapterActivity");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Logger.i(TAG, "onStop FragmentAdapterActivity");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Logger.i(TAG, "onDestroy FragmentAdapterActivity");
	}
}
