package com.bingoogol.jni02.engine;

/**
 * @author bingoogol@sina.com 14-2-23.
 */
public class JNIProvider {
    //在最外层包目录下通过javah命名生成方法名   javah com.bingoogol.jni02.engine.JNIProvider
    public native String helloFromC();

    public native String hello_from_c();
}
