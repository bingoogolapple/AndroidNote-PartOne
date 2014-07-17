package cn.bingoogol.bingo.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import cn.bingoogol.bingo.App;

public abstract class BaseFragment extends Fragment implements OnClickListener {
	protected App mApp;
	protected LocalBroadcastManager mLocalBroadcastManager;
	protected View mRootView;

	protected Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			handleMsg(msg);
		};
	};

	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mApp = App.getInstance();
		mLocalBroadcastManager = LocalBroadcastManager.getInstance(activity);
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (mRootView == null) {
			initView(savedInstanceState);
			setListener();
			afterViews(savedInstanceState);
		} else {
			ViewGroup parent = (ViewGroup) mRootView.getParent();
			if (parent != null) {
				parent.removeView(mRootView);
			}
		}
		return mRootView;
	}

	protected void setRootView(int layoutResID) {
		mRootView = LayoutInflater.from(mApp).inflate(layoutResID, null);
	}

	/**
	 * 处理handler发送的消息
	 * 
	 * @param msg
	 */
	protected abstract void handleMsg(Message msg);

	/**
	 * 初始化View控件
	 */
	protected abstract void initView(Bundle savedInstanceState);

	/**
	 * 给View控件添加事件监听器
	 */
	protected abstract void setListener();

	/**
	 * 处理业务逻辑，状态恢复等操作
	 * 
	 * @param savedInstanceState
	 */
	protected abstract void afterViews(Bundle savedInstanceState);
}
