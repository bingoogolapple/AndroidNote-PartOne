package cn.bingoogol.tabhost.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.bingoogol.tabhost.R;
import cn.bingoogol.tabhost.util.Logger;

public class TwoFragment extends Fragment {
	private static final String TAG = TwoFragment.class.getSimpleName();
	private View mRootView;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Logger.i(TAG, "onAttach Two");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Logger.i(TAG, "onCreate Two");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Logger.i(TAG, "onCreateView Two");
		if (mRootView == null) {
			Logger.i(TAG, "mRootView为空");
			mRootView = inflater.inflate(R.layout.fragment_two, container, false);
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
		Logger.i(TAG, "onActivityCreated Two");

	}

	@Override
	public void onStart() {
		super.onStart();
		Logger.i(TAG, "onStart Two");
	}

	@Override
	public void onResume() {
		super.onResume();
		Logger.i(TAG, "onResume Two");
	}

	@Override
	public void onPause() {
		super.onPause();
		Logger.i(TAG, "onPause Two");
	}

	@Override
	public void onStop() {
		super.onStop();
		Logger.i(TAG, "onStop Two");
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Logger.i(TAG, "onDestroyView Two");
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Logger.i(TAG, "onSaveInstanceState Two");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Logger.i(TAG, "onDestroy Two");
	}

	@Override
	public void onDetach() {
		super.onDetach();
		Logger.i(TAG, "onDetach Two");
	}
}