package cn.bingoogol.simples.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.bingoogol.simples.R;

public class DrawerLayoutActivity extends FragmentActivity {
	private DrawerLayout mDrawerLayout;
	private TextView mContent;
	private LinearLayout mStartDrawer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drawer_layout);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mStartDrawer = (LinearLayout) findViewById(R.id.ll_drawer_start);
		mContent = (TextView) findViewById(R.id.content_text);

		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_menu1:
			mContent.setText("呵呵");
			mDrawerLayout.closeDrawer(mStartDrawer);
			break;
		case R.id.btn_menu2:
			mContent.setText("哈哈");
			mDrawerLayout.closeDrawer(mStartDrawer);
			break;
		case R.id.btn_menu3:
			mContent.setText("嘿嘿");
			mDrawerLayout.closeDrawer(mStartDrawer);
			break;
		default:
			break;
		}
	}

}
