#include<stdio.h>
#include<jni.h>
#include"com_bingoogol_jni03_engine_JNIProvider.h"
#include <android/log.h>

#define LOG_TAG "jni"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

JNIEXPORT jstring JNICALL Java_com_bingoogol_jni03_engine_JNIProvider_helloFromC(JNIEnv* env, jobject obj){
    LOGI("info helloFromC\n");
    return (*env)->NewStringUTF(env,"helloFromC");
}

JNIEXPORT jstring JNICALL Java_com_bingoogol_jni03_engine_JNIProvider_hello_1from_1c(JNIEnv* env, jobject obj) {
    LOGI("info hello_1from_1c\n");
    return (*env)->NewStringUTF(env,"hello_1from_1c");
}