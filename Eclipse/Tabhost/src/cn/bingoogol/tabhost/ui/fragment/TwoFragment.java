package cn.bingoogol.tabhost.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import cn.bingoogol.tabhost.R;
import cn.bingoogol.tabhost.util.Logger;

public class TwoFragment extends BaseFragment {
	private static final String TAG = TwoFragment.class.getSimpleName();

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
	protected void initView(Bundle savedInstanceState) {
		Logger.i(TAG, "onCreateView Two");
		setRootView(R.layout.fragment_two);
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void afterViews(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void handleMsg(Message msg) {
		// TODO Auto-generated method stub
		
	}
}