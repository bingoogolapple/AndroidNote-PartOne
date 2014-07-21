package cn.bingoogol.anim.ui.fragment;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.animation.AnimationUtils;
import cn.bingoogol.anim.R;

public class ShakeFragment extends BaseFragment {

	@Override
	protected void initView(Bundle savedInstanceState) {
		setRootView(R.layout.fragment_shake);
	}

	@Override
	protected void setListener() {
		mRootView.findViewById(R.id.btn_shake).setOnClickListener(this);
	}

	@Override
	protected void afterViews(Bundle savedInstanceState) {

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_shake) {
			mRootView.findViewById(R.id.et_shake).startAnimation(AnimationUtils.loadAnimation(mApp, R.anim.shake));
		}
	}

	@Override
	protected void handleMsg(Message msg) {

	}

}
