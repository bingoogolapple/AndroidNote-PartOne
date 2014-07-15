package cn.bingoogol.tabhost.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.bingoogol.tabhost.R;
import cn.bingoogol.tabhost.util.Logger;

public class AlbumFragment extends Fragment {

	private static final String TAG = AlbumFragment.class.getSimpleName();
	private View mRootView;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Logger.i(TAG, "onAttach Album");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Logger.i(TAG, "onCreate Album");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Logger.i(TAG, "onCreateView Album");
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
		Logger.i(TAG, "onActivityCreated Album");
	}

	@Override
	public void onStart() {
		super.onStart();
		Logger.i(TAG, "onStart Album");
	}

	@Override
	public void onResume() {
		super.onResume();
		Logger.i(TAG, "onResume Album");
	}

	@Override
	public void onPause() {
		super.onPause();
		Logger.i(TAG, "onPause Album");
	}

	@Override
	public void onStop() {
		super.onStop();
		Logger.i(TAG, "onStop Album");
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Logger.i(TAG, "onDestroyView Album");
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Logger.i(TAG, "onSaveInstanceState Album");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Logger.i(TAG, "onDestroy Album");
	}

	@Override
	public void onDetach() {
		super.onDetach();
		Logger.i(TAG, "onDetach Album");
	}

}