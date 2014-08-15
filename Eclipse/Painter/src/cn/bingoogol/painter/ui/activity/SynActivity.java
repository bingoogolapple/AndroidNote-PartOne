package cn.bingoogol.painter.ui.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import cn.bingoogol.painter.R;
import cn.bingoogol.painter.engine.ReceiveTask;
import cn.bingoogol.painter.engine.SendTask;
import cn.bingoogol.painter.model.Message;
import cn.bingoogol.painter.ui.view.SynView;
import cn.bingoogol.painter.ui.view.SynView.OnRemoteTouchListener;

public class SynActivity extends Activity {
	private static final String TAG = SynActivity.class.getSimpleName();
	private SynView mSynView;
	private SendTask mSendTask;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Message message = (Message) msg.obj;
			Log.i(TAG, "receivemsg:" + msg.toString());
			switch (message.type) {
			case Message.START:
				mSynView.onRemoteTouchStart(message.x, message.y);
				break;
			case Message.MOVE:
				mSynView.onRemoteTouchMove(message.x, message.y);
				break;
			case Message.UP:
				mSynView.onRemoteTouchUp();
				break;
			case Message.COLOR:
				mSynView.setRemoteColor(message.color);
				break;
			case Message.XFERMODE:
				mSynView.setRemoteXfermode(message.xfermode);
				break;
			}
		};
	};

	private OnRemoteTouchListener mRemoteTouchListener = new OnRemoteTouchListener() {
		@Override
		public void onTouchStart(float x, float y) {
			mSendTask.setMsg(Message.getStartMsg(x, y));
		}

		@Override
		public void onTouchMove(float x, float y) {
			mSendTask.setMsg(Message.getMoveMsg(x, y));
		}

		@Override
		public void onTouchUp() {
			mSendTask.setMsg(Message.getUpMsg());
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_syn);
		mSynView = (SynView) findViewById(R.id.sv_syn);
		mSynView.setOnRemoteTouchListener(mRemoteTouchListener);
		mSendTask = new SendTask(this);
		new Thread(mSendTask).start();
		new Thread(new ReceiveTask(mHandler)).start();
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_syn_red:
			mSendTask.setMsg(Message.getColorMsg(Color.RED));
			mSynView.setColor(Color.RED);
			break;
		case R.id.btn_syn_green:
			mSendTask.setMsg(Message.getColorMsg(Color.GREEN));
			mSynView.setColor(Color.GREEN);
			break;
		case R.id.btn_syn_blue:
			mSendTask.setMsg(Message.getColorMsg(Color.BLUE));
			mSynView.setColor(Color.BLUE);
			break;
		case R.id.btn_syn_eraser:
			mSendTask.setMsg(Message.getXfermodeMsg(SynView.XFERMODE_CLEAR));
			mSynView.setXfermode(SynView.XFERMODE_CLEAR);
			break;
		case R.id.btn_syn_srcatop:
			mSendTask.setMsg(Message.getXfermodeMsg(SynView.XFERMODE_SRC_TOP));
			mSynView.setXfermode(SynView.XFERMODE_SRC_TOP);
			break;
		case R.id.btn_syn_normal:
			mSendTask.setMsg(Message.getXfermodeMsg(SynView.XFERMODE_NORMAL));
			mSynView.setXfermode(SynView.XFERMODE_NORMAL);
			break;
		}
	}
}
