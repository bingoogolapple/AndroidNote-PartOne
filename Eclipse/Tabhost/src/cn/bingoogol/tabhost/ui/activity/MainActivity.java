package cn.bingoogol.tabhost.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import cn.bingoogol.tabhost.R;

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
		case R.id.btn_main_horizontal:
			startActivity(new Intent(this, HorizontalActivity.class));
			break;
		case R.id.btn_main_vertical:
			startActivity(new Intent(this, VerticalActivity.class));
			break;
		case R.id.btn_main_weixin:
			startActivity(new Intent(this, WeixinActivity.class));
			break;
		case R.id.btn_main_communication:
			startActivity(new Intent(this, CommunicationActivity.class));
			break;
		default:
			break;
		}
	}
}
