#include<stdio.h>
#include<jni.h>
#include"com_bingoogol_jni02_engine_JNIProvider.h"

//引入编译器里的.h文件用<>
//引入当前工作目录下的.h文件用""

JNIEXPORT jstring JNICALL Java_com_bingoogol_jni02_engine_JNIProvider_helloFromC(JNIEnv* env, jobject obj){
    return (*env)->NewStringUTF(env,"helloFromC");
}

JNIEXPORT jstring JNICALL Java_com_bingoogol_jni02_engine_JNIProvider_hello_1from_1c(JNIEnv* env, jobject obj) {
    return (*env)->NewStringUTF(env,"hello_1from_1c");
}