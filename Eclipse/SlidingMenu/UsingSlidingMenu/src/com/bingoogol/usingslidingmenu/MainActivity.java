package com.bingoogol.usingslidingmenu;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class MainActivity extends Activity {

	private SlidingMenu mSlidingMenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mSlidingMenu = new SlidingMenu(this);
		mSlidingMenu.setMode(SlidingMenu.LEFT);
		mSlidingMenu.setBehindOffsetRes(R.dimen.sm_menu_offset);
		mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		mSlidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		mSlidingMenu.setMenu(R.layout.slidingmenu);
		mSlidingMenu.findViewById(R.id.btn_sm1).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "点击了SlidingMenu按钮", Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_MENU:
			mSlidingMenu.toggle();
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
}
