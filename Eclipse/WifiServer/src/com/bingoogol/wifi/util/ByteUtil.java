package com.bingoogol.wifi.util;

public class ByteUtil {
	private ByteUtil() {
    }

    public static byte[] intToBytes(int number) {
        return new byte[] { (byte) ((number >> 24) & 0xFF), (byte) ((number >> 16) & 0xFF), (byte) ((number >> 8) & 0xFF), (byte) (number & 0xFF) };
    }

    public static int bytesToInt(byte[] bytes) {
        return bytes[3] & 0xFF | (bytes[2] & 0xFF) << 8 | (bytes[1] & 0xFF) << 16 | (bytes[0] & 0xFF) << 24;
    }
}
