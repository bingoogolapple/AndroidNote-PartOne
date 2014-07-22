package cn.bingoogol.viewpager.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

/**
 * 数据流操作工具类
 * 
 * @author bingoogol@sina.com 2014-4-23
 */
public class StreamUtil {
	private static final String TAG = StreamUtil.class.getSimpleName();;

	private StreamUtil() {
	}

	/**
	 * 向输出流中写short数据
	 * 
	 * @param os
	 * @param shortValue
	 * @throws IOException
	 */
	public static void sendShort(OutputStream os, short shortValue) throws IOException {
		byte[] datas = ByteUtil.shortToBytes(shortValue);
		os.write(datas);
		os.flush();
	}

	/**
	 * 向输出流中写int数据
	 * 
	 * @param os
	 * @param intValue
	 * @throws IOException
	 */
	public static void sendInt(OutputStream os, int intValue) throws IOException {
		byte[] datas = ByteUtil.intToBytes(intValue);
		os.write(datas);
		os.flush();
	}

	/**
	 * 向输出流中写String数据。注意：此处字符串长度用short类型表示的
	 * 
	 * @param os
	 * @param stringValue
	 * @throws IOException
	 */
	public static void sendString(OutputStream os, String stringValue) throws IOException {
		byte[] bytes = stringValue.getBytes("UTF-8");
		// 发送字符串字节数组长度
		sendShort(os, (short) bytes.length);
		// 发送字符串内容
		os.write(bytes);
		os.flush();
	}

	/**
	 * 从输入流中读取short数据
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static short receiveShort(InputStream is) throws IOException {
		byte[] bytes = new byte[2];
		is.read(bytes);
		return ByteUtil.bytesToShort(bytes);
	}

	/**
	 * 从输入流中读取int数据
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static int receiveInt(InputStream is) throws IOException {
		byte[] bytes = new byte[4];
		is.read(bytes);
		return ByteUtil.bytesToInt(bytes);
	}

	/**
	 * 从输入流中读取String数据。注意：此处字符串长度用short类型表示的
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static String receiveString(InputStream is) throws IOException {
		// 接收字符串长度
		short length = receiveShort(is);
		// 接收字符串内
		byte[] results = new byte[length];
		is.read(results);
		return new String(results, "UTF-8");
	}

	/**
	 * 从内存中读取short数据
	 * 
	 * @param bais
	 * @return
	 * @throws IOException
	 */
	public static short readShort(ByteArrayInputStream bais) throws IOException {
		byte[] shorts = new byte[2];
		bais.read(shorts);
		return ByteUtil.bytesToShort(shorts);
	}

	/**
	 * 从内存中读取int数据
	 * 
	 * @param bais
	 * @return
	 * @throws IOException
	 */
	public static int readInt(ByteArrayInputStream bais) throws IOException {
		byte[] ints = new byte[4];
		bais.read(ints);
		return ByteUtil.bytesToInt(ints);
	}

	/**
	 * 从内存中读取String数据。注意：此处字符串长度用short类型表示的
	 * 
	 * @param bais
	 * @return
	 * @throws IOException
	 */
	public static String readString(ByteArrayInputStream bais) throws IOException {
		// 接收字符串长度
		short length = readShort(bais);
		byte[] results = new byte[length];
		bais.read(results);
		return new String(results, "UTF-8");
	}

	/**
	 * 向内存中写short数据
	 * 
	 * @param baos
	 * @param shortValue
	 * @throws IOException
	 */
	public static void writeShort(ByteArrayOutputStream baos, short shortValue) throws IOException {
		byte[] datas = ByteUtil.shortToBytes(shortValue);
		baos.write(datas);
	}

	/**
	 * 向内存中写int数据
	 * 
	 * @param baos
	 * @param intValue
	 * @throws IOException
	 */
	public static void writeInt(ByteArrayOutputStream baos, int intValue) throws IOException {
		baos.write(ByteUtil.intToBytes(intValue));
	}

	/**
	 * 往内存中写String数据。注意：此处字符串长度用short类型表示的
	 * 
	 * @param baos
	 * @param stringValue
	 * @throws IOException
	 */
	public static void writeString(ByteArrayOutputStream baos, String stringValue) throws IOException {
		byte[] strs = null;
		try {
			strs = stringValue.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// can not reachable
		}
		writeShort(baos, (short) strs.length);
		baos.write(strs);
	}

	/**
	 * 关闭流
	 * 
	 * @param stream
	 *            要关闭的数据流
	 * @param errMsg
	 *            关闭流出错时对应的错误信息
	 */
	public static void closeStream(Closeable stream, String errMsg) {
		if (stream != null) {
			try {
				stream.close();
			} catch (IOException e) {
				Logger.e(TAG, errMsg);
			}
		}
	}

	public static void closeSocket(Socket socket, String errMsg) {
		try {
			if (socket != null) {
				socket.close();
				socket = null;
			}
		} catch (IOException e) {
			Logger.e(TAG, errMsg);
		}
	}
}