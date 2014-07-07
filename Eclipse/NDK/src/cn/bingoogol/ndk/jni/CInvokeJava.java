package cn.bingoogol.ndk.jni;

import cn.bingoogol.ndk.util.Logger;

public class CInvokeJava {
	public String name = "哈哈";

	private static final String TAG = CInvokeJava.class.getSimpleName();

	public native void callHelloFromJava();

	public native void callAdd();

	public native void callPrintStaticString();

	public void helloFromJava() {
		Logger.i(TAG, "c代码调用java代码打印Hello");
	}

	public int add(int x, int y) {
		return x + y;
	}

	public void printString(String s) {
		Logger.i(TAG, name + "--在java代码中打印c代码传递过来的字符串--" + s);
	}

	public static void printStaticString(String s) {
		Logger.i(TAG, "在java的静态方法中打印c代码传递过来的字符串--" + s);
	}
}
