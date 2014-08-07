package cn.bingoogol.actionbar.ui.activity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.Toast;
import cn.bingoogol.actionbar.R;
import cn.bingoogol.actionbar.listener.MyTabListener;
import cn.bingoogol.actionbar.ui.fragment.LockedFragment;
import cn.bingoogol.actionbar.ui.fragment.UnlockFragment;

public class MainActivity extends ActionBarActivity {
	private static final String TAG = MainActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle(R.string.weichat);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		Tab tab = actionBar.newTab().setText("已加锁").setTabListener(new MyTabListener<LockedFragment>(this, "已加锁", LockedFragment.class));
		actionBar.addTab(tab);
		tab = actionBar.newTab().setText("未加锁").setTabListener(new MyTabListener<UnlockFragment>(this, "未加锁", UnlockFragment.class));
		actionBar.addTab(tab);
		setOverflowShowingAlways();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.action_activity_main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.actionbar_video:
			Log.i(TAG, getString(R.string.actionbar_camera));
			Toast.makeText(this, R.string.actionbar_video, Toast.LENGTH_SHORT).show();
			return true;
		case R.id.actionbar_feedback:
			Log.i(TAG, getString(R.string.actionbar_camera));
			Toast.makeText(this, R.string.actionbar_feedback, Toast.LENGTH_SHORT).show();
			startActivity(new Intent(this, FeedbackActivity.class));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * 屏蔽掉物理Menu键，不然在有物理Menu键的手机上，overflow按钮会显示不出来。
	 */
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
	 * 让隐藏在overflow当中的Action按钮的图标显示出来
	 */
	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
			if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
				try {
					Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
					method.setAccessible(true);
					method.invoke(menu, true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return super.onMenuOpened(featureId, menu);
	}
}