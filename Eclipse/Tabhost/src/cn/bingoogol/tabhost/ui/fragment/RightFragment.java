package cn.bingoogol.tabhost.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import cn.bingoogol.tabhost.R;
import cn.bingoogol.tabhost.util.Logger;

public class RightFragment extends BaseFragment {
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
	protected void initView(Bundle savedInstanceState) {
		Logger.i(TAG, "onCreateView Right");
		setRootView(R.layout.fragment_right);
		mTestTv = (TextView) mRootView.findViewById(R.id.tv_right_test);
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void afterViews(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void handleMsg(Message msg) {
		// TODO Auto-generated method stub
		
	}
}