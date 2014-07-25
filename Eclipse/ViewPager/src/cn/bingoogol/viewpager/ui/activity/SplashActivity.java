package cn.bingoogol.viewpager.ui.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import cn.bingoogol.viewpager.R;
import cn.bingoogol.viewpager.ui.view.BingoViewPagerExecutor;

public class SplashActivity extends BaseActivity {
	private View mLastView;

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.activity_splash);
		LayoutInflater inflater = getLayoutInflater();
		ArrayList<View> views = new ArrayList<View>();
		views.add(inflater.inflate(R.layout.view_one, null));
		views.add(inflater.inflate(R.layout.view_two, null));
		views.add(inflater.inflate(R.layout.view_three, null));
		mLastView = inflater.inflate(R.layout.view_last, null);
		views.add(mLastView);
		
		BingoViewPagerExecutor pager = (BingoViewPagerExecutor) findViewById(R.id.bvp_splash_pager);
		pager.setViewPagerViews(views);
	}


	@Override
	protected void setListener() {
		mLastView.findViewById(R.id.btn_last_main).setOnClickListener(this);
	}

	@Override
	protected void afterViews(Bundle savedInstanceState) {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_last_main:
			startActivity(new Intent(mApp, MainActivity.class));
			finish();
			break;
		}
	}
}