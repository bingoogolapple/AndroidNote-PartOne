package com.bingoogol.socketclient.engine;

import android.os.Build;
import android.os.Handler;
import android.os.Message;
import com.bingoogol.socketclient.util.Constants;
import com.bingoogol.socketclient.util.Logger;
import com.bingoogol.socketclient.util.StreamUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TcpClient {
	private static final String TAG = "TcpClient";
	private static TcpClient mInstance = new TcpClient();

	private static Socket mSocket;
	private static boolean mIsLink;

	private static InputStream mInputStream;
	private static OutputStream mOutputStream;

	private TcpClient() {
	}

	public static void connect(final String serverIP, final Handler handler) {
		disconnect();
		new Thread(new Runnable() {
			@Override
			public void run() {
				Message msg = handler.obtainMessage();
				try {
					mSocket = new Socket();
					mSocket.connect(new InetSocketAddress(serverIP, Constants.net.SERVER_TCP_PORT), Constants.net.TCP_TIME_OUT);
					mIsLink = true;

                    mInputStream = mSocket.getInputStream();
                    mOutputStream = mSocket.getOutputStream();
                    // 发送用户名
                    StreamUtil.sendString(mOutputStream,"bingoogol");
                    // 发送品牌
                    StreamUtil.sendString(mOutputStream, Build.BRAND);
                    // 接收用户id
                    String id = StreamUtil.receiveString(mInputStream);
                    Logger.i(TAG,"id:" + id);
                    msg.what = Constants.what.SUCCESS;
                    new Thread(new ReceiveTask()).start();
				} catch (Exception e) {
					msg.what = Constants.what.FAILURE;
					Logger.e(TAG, "TCP连接失败" + e.getLocalizedMessage());
				} finally {
					handler.sendMessage(msg);
				}
			}
		}).start();
	}

	public static void disconnect() {
		mIsLink = false;
		try {
			if (mInputStream != null) {
				mInputStream.close();
				mOutputStream = null;
			}
		} catch (Exception e) {
			Logger.e(TAG, "关闭输入流出错");
		} finally {
			try {
				if (mOutputStream != null) {
					mOutputStream.close();
					mOutputStream = null;
				}
			} catch (Exception e) {
				Logger.e(TAG, "关闭输入流出错");
			} finally {
				try {
					if (mSocket != null) {
						mSocket.close();
						mSocket = null;
					}
				} catch (IOException e) {
					Logger.e(TAG, "关闭TCP连接失败");
				}
			}
		}
	}

	private static class ReceiveTask implements Runnable {
        int commond = 0;
		@Override
		public void run() {
			try {
				while (mIsLink && mSocket.isConnected()) {
                    commond = StreamUtil.receiveInt(mInputStream);
                    switch (commond) {
                        case 1:
                            // 接收到数字
                            int intMsg = StreamUtil.receiveInt(mInputStream);
                            Logger.i(TAG,"客户端收到数字:" + intMsg);
                            break;
                        case 2:
                            // 接收到字符串
                            String strMsg = StreamUtil.receiveString(mInputStream);
                            Logger.i(TAG,"客户端收到字符串:" + strMsg);
                            break;
                        case 3:
                            // 询问是否接收文件
                            Logger.i(TAG,"收到服务端推送的下载文件消息，是否下载？ 10086.下载  1000.不下载");
                            int isDownload = 10086;
                            StreamUtil.sendInt(mOutputStream,isDownload);
                            break;
                    }
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}