package com.zq.dianzheng.ui;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ListView;

import com.zq.dianzheng.R;
import com.zq.dianzheng.db.dao.MemoDao;
import com.zq.dianzheng.model.Memo;
import com.zq.dianzheng.ui.adapter.MemoAdapter;
import com.zq.dianzheng.util.Constants;
import com.zq.dianzheng.util.Logger;
import com.zq.dianzheng.util.ToastUtil;

/**
 * 主界面
 * 
 * @author 郑强
 */
public class MainActivity extends GenericActivity {
	private Button writeBtn;
	private ListView memoLv;
	private MemoAdapter memoAdapter;
	private int offset = 0;
	private int maxResult = 10;
	private boolean isLoading = false;
	private ProgressDialog pd;
	private MemoDao memoDao;
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			refresh();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_wirite_main:
			Intent writeIntent = new Intent(app, EditActivity.class);
			startActivityForResult(writeIntent, Constants.activity.EDIT_MEMO_REQUEST);
			overridePendingTransition(R.anim.translate_in, R.anim.translate_out);
			break;
		}
	}

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.activity_main);
	}

	@Override
	protected void findViewById() {
		writeBtn = (Button) this.findViewById(R.id.btn_wirite_main);
		memoLv = (ListView) this.findViewById(R.id.lv_memo_main);
	}

	@Override
	protected void setListener() {
		writeBtn.setOnClickListener(this);
		memoLv.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				// 如果当前滚动状态为静止状态，并且listview里面最后一个用户可见的条目等于listview适配器里的最后一个条目
				case OnScrollListener.SCROLL_STATE_IDLE:
					// 如果不是正在加载数据才去加载数据
					if (!isLoading) {
						// 从0开始
						int position = view.getLastVisiblePosition() + 1;
						int count = memoAdapter.getCount();
						
						if (position == count) {
							if (offset + maxResult < memoDao.getCount()) {
								offset += maxResult;
								fillList();
							} else {
								ToastUtil.makeText(app, "没有更多数据了");
							}
						}
					}
					break;
				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			}
		});
	}

	@Override
	protected void processLogic() {
		memoDao = new MemoDao(app);
		fillList();
	}

	@Override
	public void onBackPressed() {
		app.exit();
	}

	private void refresh() {
		memoAdapter = null;
		offset = 0;
		fillList();
	}

	// 加载对话框
	public void showDialog() {
		isLoading = true;
		pd = ProgressDialog.show(MainActivity.this, "提示", "加载数据中...");
		pd.setCancelable(false);
	}

	// 关闭对话框
	public void closeDialog() {
		pd.dismiss();
		isLoading = false;
	}

	private void fillList() {
		new AsyncTask<Void, Void, List<Memo>>() {
			@Override
			protected void onPreExecute() {
				showDialog();
				super.onPreExecute();
			}

			@Override
			protected void onPostExecute(List<Memo> result) {
				if (result != null) {
					if (memoAdapter == null) {
						memoAdapter = new MemoAdapter(MainActivity.this, memoLv, result);
						memoLv.setAdapter(memoAdapter);
					} else {
						// 把获取到的数据添加到数据适配器里
						memoAdapter.addMoreMoment(result);
						memoAdapter.notifyDataSetChanged();
					}
				} else {
					Logger.i(tag, "result is null");
				}
				super.onPostExecute(result);
				closeDialog();
			}

			@Override
			protected List<Memo> doInBackground(Void... params) {
				if (memoDao == null) {
					memoDao = new MemoDao(app);
				}
				return memoDao.getScrollData(offset, maxResult);
			}

		}.execute();
	}

}
