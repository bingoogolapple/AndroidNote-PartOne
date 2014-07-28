package cn.bingoogol.viewpager.ui.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import cn.bingoogol.viewpager.R;
import cn.bingoogol.viewpager.ui.view.BingoViewPagerExecutor;
import cn.bingoogol.viewpager.ui.view.BingoViewPagerTimer;
import cn.bingoogol.viewpager.ui.view.WrapContentHeightViewPager;

public class MainActivity extends BaseActivity {
	private BingoViewPagerTimer mPagerTimerBvp;
	private ArrayList<View> mPagerTimerViews;
	private BingoViewPagerExecutor mPagerExecutor1Bvp;
	private ArrayList<View> mPagerExecutor1Views;
	private BingoViewPagerExecutor mPagerExecutor2Bvp;
	private ArrayList<View> mPagerExecutor2Views;
	private WrapContentHeightViewPager mPagerWchwp;
	private ArrayList<ImageView> mImageViews;

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.activity_main);
		LayoutInflater inflater = getLayoutInflater();
		mPagerTimerViews = new ArrayList<View>();
		mPagerTimerViews.add(inflater.inflate(R.layout.view_one, null));
		mPagerTimerViews.add(inflater.inflate(R.layout.view_two, null));
		mPagerTimerViews.add(inflater.inflate(R.layout.view_three, null));
		mPagerTimerViews.add(inflater.inflate(R.layout.view_four, null));
		mPagerTimerBvp = (BingoViewPagerTimer) findViewById(R.id.bvp_main_pagertimer);
		mPagerTimerBvp.setViewPagerViews(mPagerTimerViews);

		mPagerExecutor1Views = new ArrayList<View>();
		mPagerExecutor1Views.add(inflater.inflate(R.layout.view_one, null));
		mPagerExecutor1Views.add(inflater.inflate(R.layout.view_two, null));
		mPagerExecutor1Views.add(inflater.inflate(R.layout.view_three, null));
		mPagerExecutor1Views.add(inflater.inflate(R.layout.view_four, null));
		mPagerExecutor1Bvp = (BingoViewPagerExecutor) findViewById(R.id.bvp_main_pagerexecutor1);
		mPagerExecutor1Bvp.setViewPagerViews(mPagerExecutor1Views);

		mPagerExecutor2Views = new ArrayList<View>();
		mPagerExecutor2Views.add(inflater.inflate(R.layout.view_one, null));
		mPagerExecutor2Views.add(inflater.inflate(R.layout.view_two, null));
		mPagerExecutor2Views.add(inflater.inflate(R.layout.view_three, null));
		mPagerExecutor2Views.add(inflater.inflate(R.layout.view_four, null));
		mPagerExecutor2Bvp = (BingoViewPagerExecutor) findViewById(R.id.bvp_main_pagerexecutor2);
		mPagerExecutor2Bvp.setViewPagerViews(mPagerExecutor2Views);
		
		mImageViews = new ArrayList<ImageView>();
		mImageViews.add(getImageView(R.drawable.a));
		mImageViews.add(getImageView(R.drawable.b));
		mImageViews.add(getImageView(R.drawable.c));

		mPagerWchwp = (WrapContentHeightViewPager) findViewById(R.id.wchwp_main_pager);
		mPagerWchwp.setAdapter(new PagerAdapter() {

			@Override
			public int getCount() {
				// 获取ViewPager的个数，这个方法是必须实现的
				return mImageViews.size();
			}

			@Override
			public Object instantiateItem(View container, int position) {
				// container容器就是ViewPager, position指的是ViewPager的索引
				// 从View集合中获取对应索引的元素, 并添加到ViewPager中
				((ViewPager) container).addView(mImageViews.get(position));
				return mImageViews.get(position);
			}

			@Override
			public void destroyItem(View container, int position, Object object) {
				// 从ViewPager中删除集合中对应索引的View对象
				((ViewPager) container).removeView(mImageViews.get(position));
			}

			@Override
			public boolean isViewFromObject(View view, Object object) {
				// view 要关联的页面, object instantiateItem()方法返回的对象
				// 是否要关联显示页面与 instantiateItem()返回值，这个方法是必须实现的
				return view == object;
			}
		});
	}

	private ImageView getImageView(int resId) {
		ImageView imageView = new ImageView(mApp);
		imageView.setImageResource(resId);
		imageView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		return imageView;
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
}