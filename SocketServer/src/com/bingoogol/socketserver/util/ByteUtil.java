package com.bingoogol.socketserver.util;

/**
 * Created by bingoogol@sina.com on 14-3-7.
 */
public class ByteUtil {
    private ByteUtil() {
    }

    public static byte[] intToBytes(int number) {
        return new byte[]{(byte) ((number >> 24) & 0xFF), (byte) ((number >> 16) & 0xFF), (byte) ((number >> 8) & 0xFF), (byte) (number & 0xFF)};
    }

    public static int bytesToInt(byte[] bytes) {
        return bytes[3] & 0xFF | (bytes[2] & 0xFF) << 8 | (bytes[1] & 0xFF) << 16 | (bytes[0] & 0xFF) << 24;
    }

    public static byte[] shortToBytes(short number) {
        int temp = number;
        byte[] bytes = new byte[2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = new Integer(temp & 0xff).byteValue();
            temp = temp >> 8;
        }
        return bytes;
    }

    public static short bytesToShort(byte[] b) {
        short s0 = (short) (b[0] & 0xff);
        short s1 = (short) (b[1] & 0xff);
        s1 <<= 8;
        return (short) (s0 | s1);
    }
}