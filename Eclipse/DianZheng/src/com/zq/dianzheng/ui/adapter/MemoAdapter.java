package com.zq.dianzheng.ui.adapter;

import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zq.dianzheng.R;
import com.zq.dianzheng.db.dao.MemoDao;
import com.zq.dianzheng.model.Memo;
import com.zq.dianzheng.ui.EditActivity;
import com.zq.dianzheng.ui.SetAlermActivity;
import com.zq.dianzheng.util.Constants;
import com.zq.dianzheng.util.Logger;
import com.zq.dianzheng.util.TimeUtil;
import com.zq.dianzheng.util.ToastUtil;

/**
 * 备忘录适配器
 * 
 * @author 郑强
 */
public class MemoAdapter extends BaseAdapter implements OnItemLongClickListener {
	private static final String tag = "MemoAdapter";
	private List<Memo> datas;
	private LayoutInflater layoutInflater;
	private ListView listView;
	private Activity activity;
	private MemoDao memoDao;
	
	public MemoAdapter(Activity activity, ListView listView, List<Memo> datas) {
		this.layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.datas = datas;
		this.listView = listView;
		this.activity = activity;
		this.listView.setOnItemLongClickListener(this);
		memoDao = new MemoDao(activity);
	}
	
	public void addMoreMoment(List<Memo> memos) {
		datas.addAll(memos);
	}
	
	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return datas.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.memo_item, null);
			viewHolder = new ViewHolder();
			viewHolder.idTv = (TextView) convertView.findViewById(R.id.tv_id_memo_item);
			viewHolder.contentTv = (TextView) convertView.findViewById(R.id.tv_content_memo_item);
			viewHolder.timeTv = (TextView) convertView.findViewById(R.id.tv_time_memo_item);
			viewHolder.clockIv = (ImageView) convertView.findViewById(R.id.iv_clock_memo_item);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Memo memo = datas.get(position);
		viewHolder.idTv.setText(memo.getId() + "");
		viewHolder.contentTv.setText(memo.getContent());
		long time = memo.getTime();
		if(time != 0) {
			viewHolder.timeTv.setText(TimeUtil.millis2String(time));
			Logger.i(tag, "time:" + memo.getTime());
			Logger.i(tag, "currentTimeMillis:" + System.currentTimeMillis());
			if(time > System.currentTimeMillis()) {
				viewHolder.clockIv.setImageResource(R.drawable.set);
			} else {
				viewHolder.clockIv.setImageResource(R.drawable.unset);
			}
		} else {
			viewHolder.clockIv.setImageResource(R.drawable.unset);
			viewHolder.timeTv.setText("");
		}
		return convertView;
	}

	private class ViewHolder {
		private TextView idTv;
		private TextView contentTv;
		private TextView timeTv;
		private ImageView clockIv;
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		showDialog((int)id);
		return false;
	}
	
	private void showDialog(final int id) {
		final Dialog dialog = new Dialog(activity, R.style.DialogTheme);
		View view = View.inflate(activity, R.layout.item_long_click_dialog, null);
		Button setClockBtn = (Button) view.findViewById(R.id.btn_set_clock_item_dialog);
		setClockBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Logger.i(tag, "设置闹钟");
				dialog.dismiss();
				Intent intent = new Intent(activity,SetAlermActivity.class);
				intent.putExtra("id", id);
				activity.startActivityForResult(intent, Constants.activity.SET_ALERM_REQUEST);
				activity.overridePendingTransition(R.anim.translate_in, R.anim.translate_out);
			}
		});
		Button updateBtn = (Button) view.findViewById(R.id.btn_update_item_dialog);
		updateBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Logger.i(tag, "修改" + id);
				dialog.dismiss();
				Intent intent = new Intent(activity,EditActivity.class);
				intent.putExtra("id", id);
				activity.startActivityForResult(intent, Constants.activity.EDIT_MEMO_REQUEST);
				activity.overridePendingTransition(R.anim.translate_in, R.anim.translate_out);
			}
		});
		Button deleteBtn = (Button) view.findViewById(R.id.btn_delete_item_dialog);
		deleteBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Logger.i(tag, "删除" + id);
				dialog.dismiss();
				if(memoDao.deleteMemo(id)) {
					datas.remove(getDeleteMemo(id));
					MemoAdapter.this.notifyDataSetChanged();
					Logger.i("HomeMomentAdapter", "size=" + datas.size());
					ToastUtil.makeText(activity, "删除成功");
				} else {
					ToastUtil.makeText(activity, "删除失败");
				}
			}
		});
		dialog.setContentView(view);
		dialog.setCancelable(true);
		dialog.show();
	}
	
	/**
	 * 从数据源中获取要删除的Memo
	 * @param id
	 * @return
	 */
	public Memo getDeleteMemo(int id) {
		for(Memo memo : datas) {
			if(memo.getId() == id) {
				return memo;
			}
		}
		return null;
	}

}
