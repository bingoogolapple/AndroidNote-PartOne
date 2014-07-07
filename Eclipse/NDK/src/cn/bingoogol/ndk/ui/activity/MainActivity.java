package cn.bingoogol.ndk.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import cn.bingoogol.ndk.R;
import cn.bingoogol.ndk.jni.HelloInt;
import cn.bingoogol.ndk.jni.HelloString;

public class MainActivity extends BaseActivity {
	private TextView mResultTv;

	static {
		System.loadLibrary("NDK");
	}

	@Override
	protected void initView() {
		setContentView(R.layout.activity_main);
		mResultTv = (TextView) findViewById(R.id.tv_main_result);
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
		case R.id.btn_main_invoke:
			mResultTv.setText(HelloString.getFullName("王", "浩") + " --- " + HelloInt.add(2, 3));
			break;

		default:
			break;
		}
	}
}
