package cn.bingoogol.ndk.jni;

public class HelloString {

	public static native String getFullName(String first,String second);

	public native String getCString();
}