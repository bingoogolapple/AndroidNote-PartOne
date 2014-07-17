package cn.bingoogol.tabhost.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import cn.bingoogol.tabhost.R;
import cn.bingoogol.tabhost.util.Logger;

public class ThreeFragment extends BaseFragment {
	private static final String TAG = ThreeFragment.class.getSimpleName();

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
	protected void initView(Bundle savedInstanceState) {
		Logger.i(TAG, "onCreateView Three");
		setRootView(R.layout.fragment_three);
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void handleMsg(Message msg) {
		// TODO Auto-generated method stub
		
	}
}