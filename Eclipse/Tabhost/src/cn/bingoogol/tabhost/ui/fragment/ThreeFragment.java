package cn.bingoogol.tabhost.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.bingoogol.tabhost.R;
import cn.bingoogol.tabhost.util.Logger;

public class ThreeFragment extends Fragment {
	private static final String TAG = ThreeFragment.class.getSimpleName();
	private View mRootView;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Logger.i(TAG, "onAttach Three");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Logger.i(TAG, "onCreate Three");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Logger.i(TAG, "onCreateView Three");
		if (mRootView == null) {
			Logger.i(TAG, "mRootView为空");
			mRootView = inflater.inflate(R.layout.fragment_three, container, false);
		} else {
			Logger.i(TAG, "mRootView不为空");
		}
		ViewGroup parent = (ViewGroup) mRootView.getParent();
		if (parent != null) {
			parent.removeView(mRootView);
		}
		return mRootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Logger.i(TAG, "onActivityCreated Three");

	}

	@Override
	public void onStart() {
		super.onStart();
		Logger.i(TAG, "onStart Three");
	}
	

	@Override
	public void onResume() {
		super.onResume();
		Logger.i(TAG, "onResume Three");
	}
	
	@Override
	public void onPause() {
		super.onPause();
		Logger.i(TAG, "onPause Three");
	}

	@Override
	public void onStop() {
		super.onStop();
		Logger.i(TAG, "onStop Three");
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Logger.i(TAG, "onDestroyView Three");
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Logger.i(TAG, "onSaveInstanceState Three");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Logger.i(TAG, "onDestroy Three");
	}

	@Override
	public void onDetach() {
		super.onDetach();
		Logger.i(TAG, "onDetach Three");
	}
}