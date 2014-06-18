package com.zq.dianzheng.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View.OnClickListener;

import com.zq.dianzheng.App;


/**
 * 通用activity，应用程序中所有的activity都继承自改activity，模板方法
 * 
 * @author 郑强
 */
public abstract class GenericActivity extends Activity implements OnClickListener {

	protected App app;
	protected String tag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = App.getInstance();
		app.addActivity(this);
		tag = getClass().getSimpleName();
		initView();
	}

	private void initView() {
		loadViewLayout();
		findViewById();
		setListener();
		processLogic();
	}

	protected abstract void loadViewLayout();

	protected abstract void findViewById();

	protected abstract void setListener();

	protected abstract void processLogic();

	@Override
	protected void onDestroy() {
		app.removeActivity(this);
		super.onDestroy();
	}
}