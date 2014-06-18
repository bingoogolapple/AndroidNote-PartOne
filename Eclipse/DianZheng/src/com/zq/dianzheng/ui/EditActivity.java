package com.zq.dianzheng.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zq.dianzheng.R;
import com.zq.dianzheng.db.dao.MemoDao;
import com.zq.dianzheng.model.Memo;
import com.zq.dianzheng.util.Logger;
import com.zq.dianzheng.util.ToastUtil;

/**
 * 编辑备忘录界面
 * 
 * @author 郑强
 */
public class EditActivity extends GenericActivity {
	private Button saveBtn;
	private Button backBtn;
	private EditText contentEt;
	private MemoDao memoDao;
	private Memo memo;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back_edit:
			finish();
			overridePendingTransition(R.anim.translate_in_reverse, R.anim.translate_out_reverse);
			break;
		case R.id.btn_save_edit:
			String content = contentEt.getText().toString().trim();
			if (TextUtils.isEmpty(content)) {
				ToastUtil.makeText(app, "请输入备忘内容");
			} else {
				if (memo == null) {
					memo = new Memo(content);
					if (memoDao.addMemo(memo)) {
						// mResultCode默认值为RESULT_CANCELED;
						setResult(RESULT_OK);
						Logger.i(tag, "add success");
						finish();
						overridePendingTransition(R.anim.translate_in_reverse, R.anim.translate_out_reverse);
						ToastUtil.makeText(app, "添加成功");
					} else {
						ToastUtil.makeText(app, "添加失败");
					}
				} else {
					memo.setContent(content);
					if (memoDao.updateMemo(memo)) {
						setResult(RESULT_OK);
						Logger.i(tag, "update success");
						finish();
						overridePendingTransition(R.anim.translate_in_reverse, R.anim.translate_out_reverse);
						ToastUtil.makeText(app, "修改成功");
					} else {
						ToastUtil.makeText(app, "修改失败");
					}
				}
			}
			break;
		}
	}

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.activity_edit);
	}

	@Override
	protected void findViewById() {
		saveBtn = (Button) this.findViewById(R.id.btn_save_edit);
		backBtn = (Button) this.findViewById(R.id.btn_back_edit);
		contentEt = (EditText) this.findViewById(R.id.et_content_edit);
	}

	@Override
	protected void setListener() {
		saveBtn.setOnClickListener(this);
		backBtn.setOnClickListener(this);
	}

	@Override
	protected void processLogic() {
		memoDao = new MemoDao(app);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			int id = bundle.getInt("id");
			memo = memoDao.getMemo(id);
			contentEt.setText(memo.getContent());
		}
	}

	@Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.translate_in_reverse, R.anim.translate_out_reverse);
	}

}
