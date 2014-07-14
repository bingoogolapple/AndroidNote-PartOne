package cn.bingoogol.tabhost.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.bingoogol.tabhost.R;
import cn.bingoogol.tabhost.util.Logger;

public class RightFragment extends Fragment {
	private static final String TAG = RightFragment.class.getSimpleName();
	private TextView mTestTv;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Logger.i(TAG, "onAttach Right");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Logger.i(TAG, "onCreate Right");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Logger.i(TAG, "onCreateView Right");
		View rootView = inflater.inflate(R.layout.fragment_right, container, false);
		mTestTv = (TextView) rootView.findViewById(R.id.tv_right_test);
		return rootView;
	}

	public void setTestText(String text) {
		mTestTv.setText(text);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Logger.i(TAG, "onActivityCreated Right");
	}

	@Override
	public void onStart() {
		super.onStart();
		Logger.i(TAG, "onStart Right");
	}

	@Override
	public void onResume() {
		super.onResume();
		Logger.i(TAG, "onResume Right");
	}

	@Override
	public void onPause() {
		super.onPause();
		Logger.i(TAG, "onPause Right");
	}

	@Override
	public void onStop() {
		super.onStop();
		Logger.i(TAG, "onStop Right");
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Logger.i(TAG, "onDestroyView Right");
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Logger.i(TAG, "onSaveInstanceState Right");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Logger.i(TAG, "onDestroy Right");
	}

	@Override
	public void onDetach() {
		super.onDetach();
		Logger.i(TAG, "onDetach Right");
	}
}