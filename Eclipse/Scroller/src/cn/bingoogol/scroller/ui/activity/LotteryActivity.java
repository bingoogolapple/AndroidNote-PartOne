package cn.bingoogol.scroller.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import cn.bingoogol.scroller.R;

public class LotteryActivity extends Activity {
	private ListView mListLv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lottery);
		mListLv = (ListView) findViewById(R.id.lv_lottery_list);
		mListLv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new String[] { "14082241期", "14082242期", "14082243期" }));
		findViewById(R.id.btn_lottery_test).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "内部按钮能获取到焦点", Toast.LENGTH_SHORT).show();
			}
		});
	}
}
