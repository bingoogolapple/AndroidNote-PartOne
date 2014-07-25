package cn.bingoogol.viewpager.ui.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import cn.bingoogol.viewpager.R;
import cn.bingoogol.viewpager.ui.view.BingoViewPagerExecutor;
import cn.bingoogol.viewpager.ui.view.BingoViewPagerTimer;

public class MainActivity extends BaseActivity {
	private BingoViewPagerTimer mPagerTimerBvp;
	private ArrayList<View> mPagerTimerViews;
	private BingoViewPagerExecutor mPagerExecutor1Bvp;
	private ArrayList<View> mPagerExecutor1Views;
	private BingoViewPagerExecutor mPagerExecutor2Bvp;
	private ArrayList<View> mPagerExecutor2Views;

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