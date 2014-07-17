package cn.bingoogol.tabhost.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import cn.bingoogol.tabhost.R;
import cn.bingoogol.tabhost.util.Logger;

public class LeftFragment extends BaseFragment {
	private static final String TAG = LeftFragment.class.getSimpleName();

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Logger.i(TAG, "onAttach Left");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Logger.i(TAG, "onCreate Left");
	}

	@Override
	protected void initView(Bundle savedInstanceState) {
		Logger.i(TAG, "onCreateView Left");
		setRootView(R.layout.fragment_left);
	}

	@Override
	protected void setListener() {
		mRootView.findViewById(R.id.btn_left_change).setOnClickListener(this);
	}

	@Override
	protected void afterViews(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Logger.i(TAG, "onActivityCreated Left");
	}

	@Override
	public void onStart() {
		super.onStart();
		Logger.i(TAG, "onStart Left");
	}

	@Override
	public void onResume() {
		super.onResume();
		Logger.i(TAG, "onResume Left");
	}

	@Override
	public void onPause() {
		super.onPause();
		Logger.i(TAG, "onPause Left");
	}

	@Override
	public void onStop() {
		super.onStop();
		Logger.i(TAG, "onStop Left");
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Logger.i(TAG, "onDestroyView Left");
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Logger.i(TAG, "onSaveInstanceState Left");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Logger.i(TAG, "onDestroy Left");
	}

	@Override
	public void onDetach() {
		super.onDetach();
		Logger.i(TAG, "onDetach Left");
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_left_change) {
			RightFragment rightFragment = (RightFragment) getFragmentManager().findFragmentById(R.id.fg_communication_right);
			// 在activity中通过tag的方式添加，此处才能通过tag查找
			// getFragmentManager().findFragmentByTag("");
			rightFragment.setTestText("内容变化了");
		}
	}

	@Override
	protected void handleMsg(Message msg) {
		// TODO Auto-generated method stub

	}
}