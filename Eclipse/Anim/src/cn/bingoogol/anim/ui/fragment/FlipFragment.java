package cn.bingoogol.anim.ui.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import cn.bingoogol.anim.R;

public class FlipFragment extends BaseFragment {
	private Interpolator accelerator = new AccelerateInterpolator();
	private Interpolator decelerator = new DecelerateInterpolator();
	private View mFlip1;
	private View mFlip2;

	@Override
	protected void initView(Bundle savedInstanceState) {
		setRootView(R.layout.fragment_flip);
		mFlip1 = mRootView.findViewById(R.id.v_flip1);
		mFlip2 = mRootView.findViewById(R.id.v_flip2);
	}

	@Override
	protected void setListener() {
		mRootView.findViewById(R.id.btn_flip).setOnClickListener(this);
	}

	@Override
	protected void afterViews(Bundle savedInstanceState) {

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_flip) {
			flipit();
		}
	}

	private void flipit() {
		final View visibleFlip;
		final View invisibleFlip;
		if (mFlip1.getVisibility() == View.GONE) {
			visibleFlip = mFlip2;
			invisibleFlip = mFlip1;
		} else {
			invisibleFlip = mFlip2;
			visibleFlip = mFlip1;
		}
		
		ObjectAnimator visToInvis = ObjectAnimator.ofFloat(visibleFlip, "rotationY", 0f, 90f);
		visToInvis.setDuration(500);
		visToInvis.setInterpolator(accelerator);
		final ObjectAnimator invisToVis = ObjectAnimator.ofFloat(invisibleFlip, "rotationY", -90f, 0f);
		invisToVis.setDuration(500);
		invisToVis.setInterpolator(decelerator);
		visToInvis.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator anim) {
				visibleFlip.setVisibility(View.GONE);
				invisToVis.start();
				invisibleFlip.setVisibility(View.VISIBLE);
			}
		});
		visToInvis.start();
	}

	@Override
	protected void handleMsg(Message msg) {

	}

}
