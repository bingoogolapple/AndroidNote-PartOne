package com.bingoogol.socketclient.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.bingoogol.socketclient.R;
import com.bingoogol.socketclient.engine.TcpClient;
import com.bingoogol.socketclient.engine.UdpClient;
import com.bingoogol.socketclient.model.Endpoint;
import com.bingoogol.socketclient.ui.view.PTRListView;
import com.bingoogol.socketclient.util.Constants;
import com.bingoogol.socketclient.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends Activity {
	private static final String TAG = "SearchActivity";

	private PTRListView mList;
	private SearchAdapter mSearchAdapter;

	private List<Endpoint> mDatas = new ArrayList<Endpoint>();

	private boolean mIsFirstOpen = true;
	private String mNextServer;
	private String mCurrentServer;

	private ProgressDialog mProgressDialog;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constants.what.SUCCESS:
				mCurrentServer = mNextServer;
				closePD();
				mSearchAdapter.notifyDataSetChanged();
				break;
			case Constants.what.FAILURE:
				ToastUtil.makeText(SearchActivity.this, "连接服务器失败");
				closePD();
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

		UdpClient.getInstance().init(this);
		// 初始化进度对话框
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setCancelable(false);
		initListView();
	};

	@Override
	protected void onStart() {
		super.onStart();
		if (mIsFirstOpen) {
			mIsFirstOpen = false;
			scanBigScreen();
		}
	}

	/**
	 * 显示进度对话框
	 * 
	 * @param title
	 */
	public void showPD(String title) {
		mProgressDialog.setTitle(title);
		mProgressDialog.show();
	}

	/**
	 * 关闭进度对话框
	 */
	public void closePD() {
		mProgressDialog.dismiss();
	}

	private void initListView() {
		mList = (PTRListView) findViewById(android.R.id.list);
		mSearchAdapter = new SearchAdapter();
		mList.setAdapter(mSearchAdapter);

		mList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    Endpoint endpoint = mDatas.get(position - 1);
                    if (endpoint.name.equals(mCurrentServer)) {
                        TcpClient.disconnect();
                        mCurrentServer = null;
                        mSearchAdapter.notifyDataSetChanged();
                        Log.i(TAG, "手动断开连接");
                    } else {
                        showPD("正在连接服务器......");
                        mNextServer = endpoint.name;
                        TcpClient.connect(endpoint.ip, handler);
                    }
                }
            }
        });
        mList.setOnRefreshListener(new PTRListView.OnRefreshListener() {

            @Override
            public void onRefresh() {
                scanBigScreen();
            }
        });
	}

	public void scanBigScreen() {
		UdpClient.getInstance().startScan();
		showPD("正在搜索服务器......");
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				UdpClient.getInstance().stopScan();
				mDatas = UdpClient.getEndpoints();

				closePD();
				mSearchAdapter.notifyDataSetChanged();
				mList.onRefreshComplete();

				if (mDatas.size() == 0) {
					ToastUtil.makeText(SearchActivity.this, "没有搜索到服务器");
				}
			}
		}, Constants.net.SCAN_TOTAL_TIME);
	}

	private class SearchAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mDatas.size();
		}

		@Override
		public Object getItem(int position) {
			return mDatas.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = View.inflate(SearchActivity.this, R.layout.item_search, null);
				viewHolder.tv_item_search_name = (TextView) convertView.findViewById(R.id.tv_item_search_name);
				viewHolder.tv_item_search_status = (TextView) convertView.findViewById(R.id.tv_item_search_status);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			Endpoint endpoint = mDatas.get(position);
			viewHolder.tv_item_search_name.setText(endpoint.name);
			if (endpoint.name.equals(mCurrentServer)) {
				viewHolder.tv_item_search_status.setText("已链接");
			} else {
				viewHolder.tv_item_search_status.setText("未链接");
			}
			return convertView;
		}

	}

	private class ViewHolder {
		public TextView tv_item_search_name;
		public TextView tv_item_search_status;
	}

}