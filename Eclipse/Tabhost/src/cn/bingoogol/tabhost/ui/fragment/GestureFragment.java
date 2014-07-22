package cn.bingoogol.tabhost.ui.fragment;

import android.app.Activity;
import android.gesture.GestureOverlayView;
import android.os.Bundle;
import android.os.Message;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import cn.bingoogol.tabhost.R;
import cn.bingoogol.tabhost.util.DensityUtil;
import cn.bingoogol.tabhost.util.Logger;

public class GestureFragment extends BaseFragment {

	private static final String TAG = GestureFragment.class.getSimpleName();
	private GestureOverlayView mGestureGov;
	private GestureDetector mGestureDetector;
	private SimpleOnGestureListener mGestureListener = new SimpleOnGestureListener() {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			int len = DensityUtil.dip2px(getActivity(), 50);
			if (Math.abs(e1.getRawX() - e2.getRawX()) > Math.abs(e1.getRawY() - e2.getRawY())) {
				// 左右滑动
				if (Math.abs(velocityX) > 50) {
					if (e2.getRawX() - e1.getRawX() > len) {
						// 向右滑动
						Logger.i(TAG, "向右滑动");
					}
					if (e1.getRawX() - e2.getRawX() > len) {
						// 向左滑动
						Logger.i(TAG, "向左滑动");
					}
				}
			} else {
				// 上下滑动
				if (Math.abs(velocityY) > 50) {
					if (e2.getRawY() - e1.getRawY() > len) {
						// 向下滑动
						Logger.i(TAG, "向下滑动");
					}
					if (e1.getRawY() - e2.getRawY() > len) {
						// 向上滑动
						Logger.i(TAG, "向上滑动");
					}
				}
			}
			return super.onFling(e1, e2, velocityX, velocityY);
		}
	};

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Logger.i(TAG, "onAttach Gesture");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Logger.i(TAG, "onCreate Gesture");
	}

	@Override
	protected void initView(Bundle savedInstanceState) {
		Logger.i(TAG, "onCreateView Gesture");
		setRootView(R.layout.fragment_gesture);
		mGestureGov = (GestureOverlayView) mRootView.findViewById(R.id.gov_gesture);
	}

	@Override
	protected void setListener() {
		mGestureGov.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return mGestureDetector.onTouchEvent(event);
			}
		});
	}

	@Override
	protected void afterViews(Bundle savedInstanceState) {
		mGestureDetector = new GestureDetector(getActivity(), mGestureListener);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Logger.i(TAG, "onActivityCreated Gesture");
	}

	@Override
	public void onStart() {
		super.onStart();
		Logger.i(TAG, "onStart Gesture");
	}

	@Override
	public void onResume() {
		super.onResume();
		Logger.i(TAG, "onResume Gesture");
	}

	@Override
	public void onPause() {
		super.onPause();
		Logger.i(TAG, "onPause Gesture");
	}

	@Override
	public void onStop() {
		super.onStop();
		Logger.i(TAG, "onStop Gesture");
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Logger.i(TAG, "onDestroyView Gesture");
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Logger.i(TAG, "onSaveInstanceState Gesture");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Logger.i(TAG, "onDestroy Gesture");
	}

	@Override
	public void onDetach() {
		super.onDetach();
		Logger.i(TAG, "onDetach Gesture");
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handleMsg(Message msg) {
		// TODO Auto-generated method stub

	}
}