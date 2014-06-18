package com.bingoogol.wifi.engine;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.bingoogol.wifi.model.Message;
import com.bingoogol.wifi.util.StreamUtil;

public class Server {
	private ServerSocket mServerSocket;
	private Socket mSocket;
	private InputStream mInputStream;
	private OutputStream mOutputStream;
	private boolean mIsStrat = false;
	private Message mMessage;

	private MessageListener mMessageListener;

	public void start() {
		new Thread(new Runnable() {

			public void run() {
				try {
					mServerSocket = new ServerSocket(9993);
					mSocket = mServerSocket.accept();
					System.out.println("有客到");
					mOutputStream = mSocket.getOutputStream();
					mInputStream = mSocket.getInputStream();
					mIsStrat = true;
					startReceive();
					startSend();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public void startReceive() {
		new Thread(new Runnable() {
			public void run() {
				try {
					while (mIsStrat) {
						int command = StreamUtil.receiveInt(mInputStream);
						System.out.println("服务端收到command:" + command);
						switch (command) {
						case 0:
							// 断开连接
							System.out.println("服务端：收到客户端断开连接的消息");
							mIsStrat = false;
							break;
						case Message.INTVALUE:
							mMessageListener.onMessage(StreamUtil.receiveInt(mInputStream));
							break;
						case Message.STRINGVALUE:
							mMessageListener.onMessage(StreamUtil.receiveString(mInputStream));
							break;
						default:
							break;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public void startSend() {
		new Thread(new Runnable() {
			public void run() {
				try {
					while (mIsStrat) {
						if (mMessage != null) {
							StreamUtil.sendInt(mOutputStream, mMessage.getMessageType());
							switch (mMessage.getMessageType()) {
							case Message.INTVALUE:
								StreamUtil.sendInt(mOutputStream, mMessage.getIntValue());
								break;
							case Message.STRINGVALUE:
								StreamUtil.sendString(mOutputStream,mMessage.getStringValue());
								break;
							default:
								break;
							}
							mMessage = null;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public void setMessage(Message message) {
		mMessage = message;
	}

	public void setMessageListener(MessageListener messageListener) {
		mMessageListener = messageListener;
	}
	
	public void stop() {
		StreamUtil.close(mInputStream);
		StreamUtil.close(mOutputStream);
		StreamUtil.close(mSocket);
		StreamUtil.close(mServerSocket);
	}

	public interface MessageListener {
		public void onMessage(String msg);

		public void onMessage(int msg);
	}

}
