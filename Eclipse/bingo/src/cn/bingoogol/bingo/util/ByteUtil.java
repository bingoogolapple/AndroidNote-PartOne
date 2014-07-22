package cn.bingoogol.bingo.util;

/**
 * 字节数据操作工具类
 * 
 * @author bingoogol@sina.com 2014-4-23
 */
public class ByteUtil {
	private ByteUtil() {
	}

	/**
	 * int数据转字节数组
	 * 
	 * @param number
	 * @return
	 */
	public static byte[] intToBytes(int number) {
		return new byte[] { (byte) ((number >> 24) & 0xFF), (byte) ((number >> 16) & 0xFF), (byte) ((number >> 8) & 0xFF), (byte) (number & 0xFF) };
	}

	/**
	 * 字节数组转int数据
	 * 
	 * @param bytes
	 * @return
	 */
	public static int bytesToInt(byte[] bytes) {
		return bytes[3] & 0xFF | (bytes[2] & 0xFF) << 8 | (bytes[1] & 0xFF) << 16 | (bytes[0] & 0xFF) << 24;
	}

	/**
	 * short数据转字节数组
	 * 
	 * @param number
	 * @return
	 */
	public static byte[] shortToBytes(short number) {
		int temp = number;
		byte[] bytes = new byte[2];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = new Integer(temp & 0xff).byteValue();
			temp = temp >> 8;
		}
		return bytes;
	}

	/**
	 * 字节数组转short数据
	 * 
	 * @param b
	 * @return
	 */
	public static short bytesToShort(byte[] b) {
		short s0 = (short) (b[0] & 0xff);
		short s1 = (short) (b[1] & 0xff);
		s1 <<= 8;
		return (short) (s0 | s1);
	}
}