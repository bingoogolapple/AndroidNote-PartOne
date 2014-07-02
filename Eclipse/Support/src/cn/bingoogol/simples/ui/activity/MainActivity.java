package cn.bingoogol.simples.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import cn.bingoogol.simples.R;

public class MainActivity extends BaseActivity {

	@Override
	protected void initView() {
		setContentView(R.layout.activity_main);
	}

	@Override
	protected void setListener() {
	}

	@Override
	protected void afterViews(Bundle savedInstanceState) {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_main_SwipeRefreshLayout:
			startActivity(new Intent(this, SwipeRefreshLayoutActivity.class));
			break;
		case R.id.btn_main_RecyclerView:
			startActivity(new Intent(this, RecyclerViewActivity.class));
			break;
		case R.id.btn_main_CardView:
			startActivity(new Intent(this, CardViewActivity.class));
			break;
		case R.id.btn_main_MulTouch:
			startActivity(new Intent(this, MulTouchActivity.class));
			break;

		default:
			break;
		}
	}

}
