package cn.bingoogol.circleprogressbar;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import cn.bingoogol.circleprogressbar.ui.view.CircleProgressBar;

public class MainActivity extends Activity {
	private CircleProgressBar mProgressbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mProgressbar = (CircleProgressBar) findViewById(R.id.cpb_main_progressbar);
	}

	public void onClick(View v) {
		mProgressbar.setProgress(mProgressbar.getProgress() + 5);
	}
}