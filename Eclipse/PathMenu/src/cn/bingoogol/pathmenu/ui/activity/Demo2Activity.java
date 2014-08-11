package cn.bingoogol.pathmenu.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import cn.bingoogol.pathmenu.R;
import cn.bingoogol.pathmenu.util.PathAnimation;

public class Demo2Activity extends Activity implements OnClickListener {
	private RelativeLayout functionRl;
	private ImageButton functionIb;
	private boolean isFunctionsShowing;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_demo2);
		functionRl = (RelativeLayout) this.findViewById(R.id.rl_function_main);
		functionIb = (ImageButton) this.findViewById(R.id.ib_function_main);
		functionIb.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (!isFunctionsShowing) {
			showFunction();
		} else {
			closeFunction();
		}
	}

	private void showFunction() {
		PathAnimation.startAnimationsIn(functionRl, 300);
		functionIb.startAnimation(PathAnimation.getRotateAnimation(0, -270, 300));
		isFunctionsShowing = true;
	}

	private void closeFunction() {
		PathAnimation.startAnimationsOut(functionRl, 300);
		functionIb.startAnimation(PathAnimation.getRotateAnimation(-270, 0, 300));
		isFunctionsShowing = false;
	}
}