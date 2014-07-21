package cn.bingoogol.anim.ui.fragment;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.animation.AnimationUtils;
import cn.bingoogol.anim.R;

public class TranslateFragment extends BaseFragment {

	@Override
	protected void initView(Bundle savedInstanceState) {
		setRootView(R.layout.fragment_translate);
	}

	@Override
	protected void setListener() {
		mRootView.findViewById(R.id.btn_translate1).setOnClickListener(this);
	}

	@Override
	protected void afterViews(Bundle savedInstanceState) {

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_translate1) {
			mRootView.findViewById(R.id.v_translate).startAnimation(AnimationUtils.loadAnimation(mApp, R.anim.translate1));
		}
	}

	@Override
	protected void handleMsg(Message msg) {

	}

}
