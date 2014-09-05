package cn.bingoogol.dmc.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import cn.bingoogol.dmc.App;

public abstract class BaseActivity extends FragmentActivity implements OnClickListener {
	protected App mApp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mApp = App.getInstance();
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