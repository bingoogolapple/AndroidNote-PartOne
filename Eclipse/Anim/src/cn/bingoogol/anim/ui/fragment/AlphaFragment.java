package cn.bingoogol.anim.ui.fragment;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.animation.AnimationUtils;
import cn.bingoogol.anim.R;

public class AlphaFragment extends BaseFragment {

	@Override
	protected void initView(Bundle savedInstanceState) {
		setRootView(R.layout.fragment_alpha);
	}

	@Override
	protected void setListener() {
		mRootView.findViewById(R.id.btn_alpha1).setOnClickListener(this);
	}

	@Override
	protected void afterViews(Bundle savedInstanceState) {

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_alpha1) {
			mRootView.findViewById(R.id.v_alpha).startAnimation(AnimationUtils.loadAnimation(mApp, R.anim.alpha1));
		}
	}

	@Override
	protected void handleMsg(Message msg) {

	}

}
