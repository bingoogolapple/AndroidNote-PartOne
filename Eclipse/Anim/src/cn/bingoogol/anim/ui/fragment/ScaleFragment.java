package cn.bingoogol.anim.ui.fragment;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.animation.AnimationUtils;
import cn.bingoogol.anim.R;

public class ScaleFragment extends BaseFragment {

	@Override
	protected void initView(Bundle savedInstanceState) {
		setRootView(R.layout.fragment_scale);
	}

	@Override
	protected void setListener() {
		mRootView.findViewById(R.id.btn_scale1).setOnClickListener(this);
	}

	@Override
	protected void afterViews(Bundle savedInstanceState) {

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_scale1) {
			mRootView.findViewById(R.id.v_scale).startAnimation(AnimationUtils.loadAnimation(mApp, R.anim.scale1));
		}
	}

	@Override
	protected void handleMsg(Message msg) {

	}

}
