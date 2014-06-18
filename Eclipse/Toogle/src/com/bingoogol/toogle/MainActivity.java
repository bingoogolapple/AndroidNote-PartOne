package com.bingoogol.toogle;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.bingoogol.ui.Toggle;
import com.bingoogol.ui.Toggle.OnToggleStateChangeListener;

public class MainActivity extends Activity {
	private Toggle toggle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		toggle = (Toggle) findViewById(R.id.toggle);
		toggle.setImages(R.drawable.toggle_on_bg, R.drawable.toggle_off_bg, R.drawable.toggle_slip);
		toggle.setCurrentToggleState(false);
		toggle.setOnToggleStateChangeListener(new OnToggleStateChangeListener() {

			@Override
			public void onToggleStateChange(boolean state) {
				if (state) {
					Toast.makeText(MainActivity.this, "开启状态", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(MainActivity.this, "关闭状态", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

}
