package cn.bingoogol.simples.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.bingoogol.simples.R;

public class RecyclerViewActivity extends BaseActivity {

	@Override
	protected void initView() {
		setContentView(R.layout.activity_recycler_view);
		initHorizaontal();
		initVertical();
	}

	@Override
	protected void setListener() {
	}

	@Override
	protected void afterViews(Bundle savedInstanceState) {
	}

	@Override
	public void onClick(View v) {
	}

	private void initHorizaontal() {
		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview_horizontal);
		// 创建一个线性布局管理器
		LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
		// 设置布局管理器
		recyclerView.setLayoutManager(layoutManager);
		// 创建数据集
		String[] dataset = new String[100];
		for (int i = 0; i < dataset.length; i++) {
			dataset[i] = "item" + i;
		}
		MyAdapter adapter = new MyAdapter(dataset);
		recyclerView.setAdapter(adapter);
	}

	public void initVertical() {
		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview_vertical);
		recyclerView.setHasFixedSize(true);
		// 创建一个线性布局管理器
		LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		// 默认是Vertical，可以不写
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		// 设置布局管理器
		recyclerView.setLayoutManager(layoutManager);

		// 创建数据集
		String[] dataset = new String[100];
		for (int i = 0; i < dataset.length; i++) {
			dataset[i] = "item" + i;
		}
		MyAdapter adapter = new MyAdapter(dataset);
		recyclerView.setAdapter(adapter);
	}

	private class MyAdapter extends RecyclerView.Adapter<ViewHolder> {
		// 数据集
		private String[] mDataset;

		public MyAdapter(String[] dataset) {
			super();
			mDataset = dataset;
		}

		@Override
		public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
			View view = View.inflate(viewGroup.getContext(), android.R.layout.simple_list_item_1, null);
			ViewHolder holder = new ViewHolder(view);
			return holder;
		}

		@Override
		public void onBindViewHolder(ViewHolder viewHolder, int i) {
			viewHolder.mTextView.setText(mDataset[i]);
		}

		@Override
		public int getItemCount() {
			return mDataset.length;
		}

	}

	private static class ViewHolder extends RecyclerView.ViewHolder {
		public TextView mTextView;

		public ViewHolder(View itemView) {
			super(itemView);
			mTextView = (TextView) itemView;
		}
	}
}
