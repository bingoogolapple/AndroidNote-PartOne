package cn.bingoogol.tabhost.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import cn.bingoogol.tabhost.App;

public abstract class BaseActivity extends FragmentActivity implements OnClickListener {
	protected App mApp;
	protected LocalBroadcastManager mLocalBroadcastManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mApp = App.getInstance();
		mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
		App.addActivity(this);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		initView(savedInstanceState);
		setListener();
		afterViews(savedInstanceState);
	}

	/**
	 * 初始化布局以及View控件
	 */
	protected abstract void initView(Bundle savedInstanceState);

	/**
	 * 给View控件添加事件监听器
	 */
	protected abstract void setListener();

	/**
	 * 处理业务逻辑，状态恢复等操作
	 * 
	 * @param savedInstanceState
	 */
	protected abstract void afterViews(Bundle savedInstanceState);

	@Override
	protected void onDestroy() {
		super.onDestroy();
		App.removeActivity(this);
	}
}