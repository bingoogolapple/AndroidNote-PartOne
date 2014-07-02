package cn.bingoogol.simples.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import cn.bingoogol.simples.R;

public class MulTouchActivity extends BaseActivity {
	private static final String TAG = MulTouchActivity.class.getSimpleName();
	private ImageView iv_icon;
	private double currentDistance;
	private double lastDistance = -1;

	@Override
	protected void initView() {
		setContentView(R.layout.activity_mul_touch);
		iv_icon = (ImageView) findViewById(R.id.iv_icon);
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
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			Log.i(TAG, "ACTION_DOWN");
			break;
		case MotionEvent.ACTION_MOVE:
			// LayoutParams lp = (LayoutParams) iv_icon.getLayoutParams();
			// lp.leftMargin = (int) event.getX();
			// lp.topMargin = (int) event.getY();
			// iv_icon.setLayoutParams(lp);
			// Log.i(TAG, "ACTION_MOVE  " + String.format("x:%f,y:%f", event.getX(), event.getY()));
			if (event.getPointerCount() == 2) {

				float offsetX = event.getX(0) - event.getX(1);
				float offsetY = event.getY(0) - event.getY(1);
				currentDistance = Math.sqrt(Math.pow(offsetX, 2) + Math.pow(offsetY, 2));
				if (lastDistance < 0) {
					lastDistance = currentDistance;
				} else {
					if (currentDistance - lastDistance > 5) {
						lastDistance = currentDistance;
						Log.i(TAG, "ACTION_MOVIE  放大");

						LayoutParams lp = (LayoutParams) iv_icon.getLayoutParams();
						lp.width = (int) (1.1 * iv_icon.getWidth());
						lp.height = (int) (1.1 * iv_icon.getHeight());
						iv_icon.setLayoutParams(lp);
					} else if (lastDistance - currentDistance > 5) {
						lastDistance = currentDistance;
						Log.i(TAG, "ACTION_MOVIE  缩小");
						LayoutParams lp = (LayoutParams) iv_icon.getLayoutParams();
						lp.width = (int) (0.9 * iv_icon.getWidth());
						lp.height = (int) (0.9 * iv_icon.getHeight());
						iv_icon.setLayoutParams(lp);
					}
				}
			}

			break;
		case MotionEvent.ACTION_UP:
			Log.i(TAG, "ACTION_UP");
			break;
		}
		// 三个阶段有依赖关系，如果没有指定第一个阶段触发成功，则后续事件不会触发
		return true;
	}
}
