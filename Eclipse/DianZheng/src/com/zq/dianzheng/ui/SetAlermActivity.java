package com.zq.dianzheng.ui;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import com.zq.dianzheng.R;
import com.zq.dianzheng.db.dao.MemoDao;
import com.zq.dianzheng.model.Memo;
import com.zq.dianzheng.util.Logger;

/**
 * 设置提醒界面
 * 
 * @author 郑强
 */
public class SetAlermActivity extends GenericActivity {
	private Button saveBtn;
	private Button backBtn;
	private DatePicker datePicker;
	private TimePicker timePicker;
	private AlarmManager alarmManager;
	private Calendar calendar = Calendar.getInstance();
	private MemoDao memoDao;
	private Memo memo;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_save_set_alerm:
			setAlerm();
			break;
		case R.id.btn_back_set_alerm:
			finish();
			overridePendingTransition(R.anim.translate_in_reverse, R.anim.translate_out_reverse);
			break;
		}
	}

	private void setAlerm() {
		Logger.i(tag, "datePicker:" + datePicker.getMonth());
		calendar.set(Calendar.YEAR, datePicker.getYear());
		calendar.set(Calendar.MONTH, datePicker.getMonth());
		calendar.set(Calendar.DATE, datePicker.getDayOfMonth());
		memo.setTime(calendar.getTimeInMillis());
		
		if(memo.getTime() > System.currentTimeMillis()) {
			Intent intent = new Intent();
			intent.setAction("com.zq.dianzheng");
			intent.putExtra("id", memo.getId());
			intent.putExtra("time", memo.getTime());
			PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			alarmManager.set(AlarmManager.RTC_WAKEUP, memo.getTime(), pendingIntent);
		}
		memoDao.updateMemo(memo);
		setResult(RESULT_OK);
		finish();
		overridePendingTransition(R.anim.translate_in_reverse, R.anim.translate_out_reverse);
	}

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.activity_set_alerm);
	}

	@Override
	protected void findViewById() {
		saveBtn = (Button) this.findViewById(R.id.btn_save_set_alerm);
		backBtn = (Button) this.findViewById(R.id.btn_back_set_alerm);
		datePicker = (DatePicker) this.findViewById(R.id.datepicker);
		timePicker = (TimePicker) this.findViewById(R.id.timepicker);
		timePicker.setOnTimeChangedListener(new OnTimeChangedListenerImpl());
	}

	@Override
	protected void setListener() {
		saveBtn.setOnClickListener(this);
		backBtn.setOnClickListener(this);
	}

	@Override
	protected void processLogic() {
		alarmManager = (AlarmManager) super.getSystemService(Context.ALARM_SERVICE);
		memoDao = new MemoDao(app);
		Bundle bundle = getIntent().getExtras();
		int id = bundle.getInt("id");
		memo = memoDao.getMemo(id);
	}
	
	@Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.translate_in_reverse, R.anim.translate_out_reverse);
	}

	private class OnTimeChangedListenerImpl implements OnTimeChangedListener {
		public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
			calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
			calendar.set(Calendar.MINUTE, minute);
		}
	}
}
