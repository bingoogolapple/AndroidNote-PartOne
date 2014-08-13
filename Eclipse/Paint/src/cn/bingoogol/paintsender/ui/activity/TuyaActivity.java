package cn.bingoogol.paintsender.ui.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import cn.bingoogol.paint.R;
import cn.bingoogol.paintsender.ui.activity.ui.view.TuyaView;
import cn.bingoogol.paintsender.ui.activity.ui.view.TuyaView.MaskFilterType;
import cn.bingoogol.paintsender.ui.activity.ui.view.TuyaView.XfermodeType;

public class TuyaActivity extends Activity {
	private TuyaView mTuyaView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tuya);
		mTuyaView = (TuyaView) findViewById(R.id.tyv_tuya);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_red:
			mTuyaView.setColor(Color.RED);
			break;
		case R.id.btn_green:
			mTuyaView.setColor(Color.GREEN);
			break;
		case R.id.btn_blue:
			mTuyaView.setColor(Color.BLUE);
			break;
		case R.id.btn_mask_emboss:
			mTuyaView.setMaskFilter(MaskFilterType.EMBOSSMASK);
			break;
		case R.id.btn_mask_blur:
			mTuyaView.setMaskFilter(MaskFilterType.BLURMASK);
			break;
		case R.id.btn_mask_none:
			mTuyaView.setMaskFilter(MaskFilterType.NONEMASK);
			break;
		case R.id.btn_xfermode_clear:
			mTuyaView.setXfermodeType(XfermodeType.CLEARXFERMODE);
			break;
		case R.id.btn_xfermode_srcatop:
			mTuyaView.setXfermodeType(XfermodeType.SRCATOPXFERMODE);
			break;
		case R.id.btn_xfermode_none:
			mTuyaView.setXfermodeType(XfermodeType.NONEXFERMODE);
			break;
		case R.id.btn_redo:
			mTuyaView.redo();
			break;
		case R.id.btn_undo:
			mTuyaView.undo();
			break;

		default:
			break;
		}
	}

}
