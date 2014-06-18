package com.bingoogol.googlerefresh;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener {

	private SwipeRefreshLayout swipeLayout;
	private ListView listView;
	private ArrayAdapter<String> mAdapter;
	private ArrayList<String> mDatas = new ArrayList<String>();
	private int flag = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mDatas.add("item1");
		mDatas.add("item2");
		mDatas.add("item3");
		mDatas.add("item4");
		swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
		swipeLayout.setOnRefreshListener(this);
		swipeLayout.setColorScheme(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
		listView = (ListView) findViewById(R.id.list);
		mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mDatas);
		listView.setAdapter(mAdapter);

	}

	@Override
	public void onRefresh() {
		new AsyncTask<Void, Void, Void>() {
			private ProgressDialog pd = new ProgressDialog(MainActivity.this);

			protected void onPreExecute() {
				pd.setMessage("正在加载数据");
				pd.show();
			};

			@Override
			protected Void doInBackground(Void... params) {
				mDatas.add("new Item" + flag++);
				mDatas.add("new Item" + flag++);
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return null;
			}

			protected void onPostExecute(Void result) {
				pd.dismiss();
				swipeLayout.setRefreshing(false);
				mAdapter.notifyDataSetChanged();
			};

		}.execute();
	}
}