package com.github.mediaserver.server.jni;

import java.io.UnsupportedEncodingException;

public class DMSJniInterface {

	static {
		// libgit-platinum.so 可执行的二进制文件，通过静态代码块把二进制的库文件加载到java虚拟机里
		System.loadLibrary("git-platinum");
	}

	public static native int startServer(byte[] rootDir, byte[] name, byte[] uid);

	public static native int stopServer();

	public static native boolean enableLogPrint(boolean flag);

	public static boolean startServer(String rootDir, String name, String uid) {
		if (rootDir == null) {
			rootDir = "";
		}
		if (name == null) {
			name = "";
		}
		if (uid == null) {
			uid = "";
		}
		int ret = -1;
		try {
			ret = startServer(rootDir.getBytes("UTF-8"), name.getBytes("UTF-8"), uid.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// not reachable
		}
		return ret == 0 ? true : false;
	}

}
