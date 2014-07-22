package cn.bingoogol.tabhost.ui.activity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import cn.bingoogol.tabhost.R;
import cn.bingoogol.tabhost.ui.fragment.GestureFragment;
import cn.bingoogol.tabhost.ui.fragment.OneFragment;
import cn.bingoogol.tabhost.ui.fragment.ThreeFragment;
import cn.bingoogol.tabhost.ui.view.PagerSlidingTabStrip;
import cn.bingoogol.tabhost.util.Logger;

public class WechatActivity extends BaseActivity {
	private static final String TAG = WechatActivity.class.getSimpleName();
	private OneFragment mOneFragment;
	private GestureFragment mGestureFragment;
	private ThreeFragment mThreeFragment;
	private PagerSlidingTabStrip mTabPats;

	private DisplayMetrics mDisplayMetrics;

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.activity_wechat);
		Logger.i(TAG, "onCreate WechatActivity");
		setOverflowShowingAlways();
		mDisplayMetrics = getResources().getDisplayMetrics();
		ViewPager pagerVp = (ViewPager) findViewById(R.id.vp_wechat_pager);

		pagerVp.setOffscreenPageLimit(2);

		mTabPats = (PagerSlidingTabStrip) findViewById(R.id.psts_wechat_tab);
		pagerVp.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
		mTabPats.setViewPager(pagerVp);
		setTabsValue();
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
		Logger.i(TAG, "onStart WechatActivity");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Logger.i(TAG, "onResume WechatActivity");
	}

	@Override
	protected void onResumeFragments() {
		super.onResumeFragments();
		Logger.i(TAG, "onResumeFragments WechatActivity");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Logger.i(TAG, "onPause WechatActivity");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Logger.i(TAG, "onStop WechatActivity");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Logger.i(TAG, "onDestroy WechatActivity");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_wechat, menu);
		return true;
	}

	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
			if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
				try {
					Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
					m.setAccessible(true);
					m.invoke(menu, true);
				} catch (Exception e) {
				}
			}
		}
		return super.onMenuOpened(featureId, menu);
	}

	private void setOverflowShowingAlways() {
		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
			menuKeyField.setAccessible(true);
			menuKeyField.setBoolean(config, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 对PagerSlidingTabStrip的各项属性进行赋值。
	 */
	private void setTabsValue() {
		// 设置Tab是自动填充满屏幕的
		mTabPats.setShouldExpand(true);
		// 设置Tab的分割线是透明的
		mTabPats.setDividerColor(Color.TRANSPARENT);
		// 设置Tab底部线的高度
		mTabPats.setUnderlineHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, mDisplayMetrics));
		// 设置Tab Indicator的高度
		mTabPats.setIndicatorHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, mDisplayMetrics));
		// 设置Tab标题文字的大小
		mTabPats.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, mDisplayMetrics));
		// 设置Tab Indicator的颜色
		mTabPats.setIndicatorColor(Color.parseColor("#45c01a"));
		// 设置选中Tab文字的颜色 (这是我自定义的一个方法)
		mTabPats.setSelectedTextColor(Color.parseColor("#45c01a"));
		// 取消点击Tab时的背景色
		mTabPats.setTabBackground(0);
	}

	public class MyPagerAdapter extends FragmentPagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		private final String[] titles = { "聊天", "发现", "通讯录" };

		@Override
		public CharSequence getPageTitle(int position) {
			return titles[position];
		}

		@Override
		public int getCount() {
			return titles.length;
		}
		
		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				if (mOneFragment == null) {
					mOneFragment = new OneFragment();
				}
				return mOneFragment;
			case 1:
				if (mGestureFragment == null) {
					mGestureFragment = new GestureFragment();
				}
				return mGestureFragment;
			case 2:
				if (mThreeFragment == null) {
					mThreeFragment = new ThreeFragment();
				}
				return mThreeFragment;
			default:
				return null;
			}
		}

	}
}
