package cn.bingoogol.tabhost.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import cn.bingoogol.tabhost.R;
import cn.bingoogol.tabhost.util.Logger;
import cn.bingoogol.tabhost.util.ToastUtil;

public class ABOneActivity extends BaseActivity {
	private static final String TAG = ABOneActivity.class.getSimpleName();

	@Override
	protected void initView(Bundle savedInstanceState) {
		setTitle("这是标题");
		// getActionBar().hide();
		setContentView(R.layout.activity_abone);
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
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_abone, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.ab_actionbar_one:
			ToastUtil.makeText("ONE");
			return true;
		case R.id.ab_actionbar_two:
			ToastUtil.makeText("TWO");
			startActivity(new Intent(this, ABTwoActivity.class));
			return true;
		case R.id.ab_actionbar_three:
			ToastUtil.makeText("THREE");
			startActivity(new Intent(this, ABThreeActivity.class));
			return true;
		case R.id.ab_actionbar_right:
			ToastUtil.makeText("RIGHT");
			startActivity(new Intent(this, ABFourActivity.class));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Logger.i(TAG, "onDestroy");
	}
}
