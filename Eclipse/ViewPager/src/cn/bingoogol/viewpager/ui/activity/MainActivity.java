package cn.bingoogol.viewpager.ui.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import cn.bingoogol.viewpager.R;
import cn.bingoogol.viewpager.ui.view.BingoViewPagerTimer;

public class MainActivity extends BaseActivity {
	private BingoViewPagerTimer mPagerBvp;
	private ArrayList<View> mViews;

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.activity_main);
		mPagerBvp = (BingoViewPagerTimer) findViewById(R.id.bvp_main_pager);

		LayoutInflater inflater = getLayoutInflater();
		mViews = new ArrayList<View>();
		mViews.add(inflater.inflate(R.layout.view_splash_one, null));
		mViews.add(inflater.inflate(R.layout.view_splash_two, null));
		mViews.add(inflater.inflate(R.layout.view_splash_three, null));
		mViews.add(inflater.inflate(R.layout.view_splash_four, null));
		mPagerBvp.setViewPagerViews(mViews);
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