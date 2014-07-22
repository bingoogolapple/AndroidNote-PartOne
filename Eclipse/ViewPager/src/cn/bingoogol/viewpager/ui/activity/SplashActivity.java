package cn.bingoogol.viewpager.ui.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import cn.bingoogol.viewpager.R;
import cn.bingoogol.viewpager.util.DensityUtil;
import cn.bingoogol.viewpager.util.Logger;

public class SplashActivity extends BaseActivity {
	private static final String TAG = SplashActivity.class.getSimpleName();
	private ViewPager mContentVp;
	private ArrayList<View> mViews;
	private ImageView[] mPoints;

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.activity_splash);
		initContentVp();
		initPoints();
	}

	private void initContentVp() {
		LayoutInflater inflater = getLayoutInflater();
		mViews = new ArrayList<View>();
		mViews.add(inflater.inflate(R.layout.view_splash_one, null));
		mViews.add(inflater.inflate(R.layout.view_splash_two, null));
		mViews.add(inflater.inflate(R.layout.view_splash_three, null));

		mContentVp = (ViewPager) findViewById(R.id.vp_splash_content);
		mContentVp.setAdapter(new MyAdapter());
		mContentVp.setOnPageChangeListener(new MyListener());
	}

	private void initPoints() {
		LinearLayout pointLl = (LinearLayout) findViewById(R.id.ll_splash_point);
		mPoints = new ImageView[mViews.size()];
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		int margin = DensityUtil.dip2px(getResources().getDimension(R.dimen.size_divider));
		Logger.i(TAG, "margin:" + margin);
		lp.setMargins(margin, 0, margin, 0);
		ImageView imageView;
		for (int i = 0; i < mViews.size(); i++) {
			imageView = new ImageView(SplashActivity.this);
			imageView.setLayoutParams(lp);
			mPoints[i] = imageView;
			pointLl.addView(imageView);
		}
		setCurrentPoint(0);
	}

	@Override
	protected void setListener() {
		mViews.get(2).findViewById(R.id.btn_splash_three_main).setOnClickListener(this);
	}

	@Override
	protected void afterViews(Bundle savedInstanceState) {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_splash_three_main:
			startActivity(new Intent(mApp, MainActivity.class));
			finish();
			break;
		}
	}

	private void setCurrentPoint(int position) {
		mPoints[position].setBackgroundResource(R.drawable.point_focused);
		for (int i = 0; i < mPoints.length; i++) {
			if (position != i) {
				mPoints[i].setBackgroundResource(R.drawable.point_unfocused);
			}
		}
	}

	private final class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			// 获取ViewPager的个数，这个方法是必须实现的
			Logger.i(TAG, "getCount");
			return mViews.size();
		}

		@Override
		public Object instantiateItem(View container, int position) {
			// container容器就是ViewPager, position指的是ViewPager的索引
			// 从View集合中获取对应索引的元素, 并添加到ViewPager中
			((ViewPager) container).addView(mViews.get(position));
			Logger.i(TAG, "instantiateItem");
			return mViews.get(position);
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			// 从ViewPager中删除集合中对应索引的View对象
			((ViewPager) container).removeView(mViews.get(position));
			Logger.i(TAG, "destroyItem");
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			// view 要关联的页面, object instantiateItem()方法返回的对象
			// 是否要关联显示页面与 instantiateItem()返回值，这个方法是必须实现的
			Logger.i(TAG, "isViewFromObject");
			return view == object;
		}
	}

	private final class MyListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int state) {
			switch (state) {
			case ViewPager.SCROLL_STATE_DRAGGING:
				// 开始滑动
				Logger.i(TAG, "onPageScrollStateChanged:  state=" + state + "    SCROLL_STATE_DRAGGING");
				break;
			case ViewPager.SCROLL_STATE_SETTLING:
				// 当松开手时
				// 如果没有其他页显示出来：SCROLL_STATE_DRAGGING --> SCROLL_STATE_IDLE
				// 如果有其他页有显示出来（不管显示了多少），就会触发正在设置页码
				// 页码没有改变时：SCROLL_STATE_DRAGGING --> SCROLL_STATE_SETTLING --> SCROLL_STATE_IDLE
				// 页码有改变时：SCROLL_STATE_DRAGGING --> SCROLL_STATE_SETTLING --> onPageSelected --> SCROLL_STATE_IDLE
				Logger.i(TAG, "onPageScrollStateChanged:  state=" + state + "    SCROLL_STATE_SETTLING");
				break;
			case ViewPager.SCROLL_STATE_IDLE:
				// 停止滑动
				Logger.i(TAG, "onPageScrollStateChanged:  state=" + state + "    SCROLL_STATE_IDLE");
				break;
			}
		}

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			// Logger.i(TAG, "onPageScrolled:  position=" + position + "  positionOffset=" + positionOffset + "  positionOffsetPixels=" + positionOffsetPixels);
		}

		@Override
		public void onPageSelected(int position) {
			Logger.i(TAG, "onPageSelected:  position=" + position);
			setCurrentPoint(position);
		}
	}
}