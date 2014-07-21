package cn.bingoogol.anim.ui.fragment;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import cn.bingoogol.anim.R;

public class FrameFragment extends BaseFragment {

	private ImageView mFrameIv;
	private AnimationDrawable mAd;

	@Override
	protected void initView(Bundle savedInstanceState) {
		setRootView(R.layout.fragment_frame);
		mFrameIv = (ImageView) mRootView.findViewById(R.id.iv_frame);
		mAd = (AnimationDrawable) mFrameIv.getDrawable();
	}

	@Override
	protected void setListener() {
		mRootView.findViewById(R.id.btn_frame_start).setOnClickListener(this);
		mRootView.findViewById(R.id.btn_frame_stop).setOnClickListener(this);
	}

	@Override
	protected void afterViews(Bundle savedInstanceState) {

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_frame_start) {
			mAd.start();
		}
		if (v.getId() == R.id.btn_frame_stop) {
			mAd.stop();
		}
	}

	@Override
	protected void handleMsg(Message msg) {

	}

}
