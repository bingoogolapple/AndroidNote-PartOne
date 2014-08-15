package cn.bingoogol.painter.engine;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import android.os.Handler;
import cn.bingoogol.painter.util.Constants;

public class ReceiveTask implements Runnable {
	private DatagramSocket mDs;
	private Handler mHandler;

	public ReceiveTask(Handler handler) {
		mHandler = handler;
	}

	@Override
	public void run() {
		try {
			mDs = new DatagramSocket(Constants.net.RECEIVE_PORT);
			ByteArrayInputStream bais;
			ObjectInputStream ois;
			while (true) {
				byte[] receiveBuf = new byte[128];
				DatagramPacket dp = new DatagramPacket(receiveBuf, receiveBuf.length);
				mDs.receive(dp);

				bais = new ByteArrayInputStream(receiveBuf);
				ois = new ObjectInputStream(bais);
				mHandler.sendMessage(mHandler.obtainMessage(0, ois.readObject()));
				bais.close();
				ois.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}