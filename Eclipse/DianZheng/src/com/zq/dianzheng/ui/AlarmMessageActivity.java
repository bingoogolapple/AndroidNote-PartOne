package com.zq.dianzheng.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.media.MediaPlayer;
import android.os.Bundle;

import com.zq.dianzheng.R;
import com.zq.dianzheng.db.dao.MemoDao;
import com.zq.dianzheng.model.Memo;
import com.zq.dianzheng.util.TimeUtil;

/**
 * 提醒界面
 * 
 * @author 郑强
 */
@SuppressLint("SimpleDateFormat")
public class AlarmMessageActivity extends Activity{
	private MediaPlayer mediaPlayer;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mediaPlayer = MediaPlayer.create(this, R.raw.yyy);
		mediaPlayer.setLooping(true);
		Bundle bundle = getIntent().getExtras();
		int id = bundle.getInt("id");
		long time = bundle.getLong("time");
		Memo memo = new MemoDao(this).getMemo(id);
		
		if(memo != null && memo.getTime() == time) {
			mediaPlayer.start();
			Dialog dialog =  new AlertDialog.Builder(this)
			.setIcon(R.drawable.ic_launcher)
			.setTitle("备忘录提醒" + TimeUtil.millis2String(memo.getTime()))
			.setMessage(memo.getContent())
			.setPositiveButton("关闭", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mediaPlayer.stop();
					finish();
				}
			}).show();
			
			dialog.setOnDismissListener(new OnDismissListener() {
				
				@Override
				public void onDismiss(DialogInterface dialog) {
					mediaPlayer.stop();
					finish();
				}
			});
		} else {
			finish();
		}
	}
	
}
