package cn.bingoogol.painter.engine;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import android.content.Context;
import android.util.Log;
import cn.bingoogol.painter.model.Message;
import cn.bingoogol.painter.util.Constants;
import cn.bingoogol.painter.util.NetUtil;

public class SendTask implements Runnable {
	private static final String TAG = SendTask.class.getSimpleName();
	private BlockingQueue<Message> mMessageQueue = new ArrayBlockingQueue<Message>(10);
	private DatagramSocket mDs;
	private Context mContext;

	public SendTask(Context context) {
		mContext = context;
	}

	public void setMsg(Message msg) {
		try {
			mMessageQueue.put(msg);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			mDs = new DatagramSocket();
			String ip = NetUtil.getLocalIpAddress(mContext);
			Log.i(TAG, "ip:" + ip);
			if (ip.equals("192.168.0.64")) {
				ip = "192.168.0.166";
			} else if (ip.equals("192.168.0.166")) {
				ip = "192.168.0.64";
			}
			ByteArrayOutputStream baos;
			ObjectOutputStream oos;
			byte[] bytes;
			while (true) {
				Message message = mMessageQueue.take();
				baos = new ByteArrayOutputStream();
				oos = new ObjectOutputStream(baos);
				oos.writeObject(message);
				bytes = baos.toByteArray();
				Log.i(TAG, "sendmsg:" + message.toString());
				mDs.send(new DatagramPacket(bytes, bytes.length, InetAddress.getByName(ip), Constants.net.RECEIVE_PORT));
				baos.close();
				oos.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}