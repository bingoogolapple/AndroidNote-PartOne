package com.bingoogol.socketserver.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by bingoogol@sina.com on 14-3-7.
 */
public class StreamUtil {
    private StreamUtil() {
    }

    public static void sendInt(OutputStream os, int intValue) throws IOException {
        os.write(ByteUtil.intToBytes(intValue));
        os.flush();
    }

    public static void sendString(OutputStream os, String stringValue) throws IOException {
        byte[] bytes = stringValue.getBytes("UTF-8");
        // 发送字符串字节数组长度
        sendInt(os, bytes.length);
        // 发送字符串内容
        os.write(bytes);
        os.flush();
    }

    public static int receiveInt(InputStream is) throws IOException {
        byte[] bytes = new byte[4];
        is.read(bytes);
        return ByteUtil.bytesToInt(bytes);
    }

    public static String receiveString(InputStream is) throws IOException {
        // 接收字符串长度
        int length = receiveInt(is);
        // 接收字符串内容
        byte[] bytes = new byte[length];
        is.read(bytes);
        return new String(bytes, "UTF-8");
    }
}