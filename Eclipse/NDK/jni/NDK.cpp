#include <jni.h>
#include <android/log.h>
#include <string.h>
#include "cn_bingoogol_ndk_jni_HelloInt.h"
#include "cn_bingoogol_ndk_jni_HelloString.h"
#include "Hello.h"

#define TAG "NDK.cpp"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, TAG, __VA_ARGS__)

JNIEXPORT jint JNICALL Java_cn_bingoogol_ndk_jni_HelloInt_add(JNIEnv *, jclass,
		jint x, jint y) {
	LOGE("错误");
	LOGI("x = %d", x);
	LOGI("y = %d", y);
	return x + y;
}

JNIEXPORT jstring JNICALL Java_cn_bingoogol_ndk_jni_HelloString_getFullName(
		JNIEnv *env, jclass, jstring first, jstring second) {
	// convert Java string to UTF-8
	const char *_first = env->GetStringUTFChars(first, 0);
	const char *_second = env->GetStringUTFChars(second, 0);
	LOGI("first = %s", _first);
	LOGI("second = %s", _second);
	Hello hello;
	return env->NewStringUTF(hello.getWords());
}

JNIEXPORT jstring JNICALL Java_cn_bingoogol_ndk_jni_HelloString_getCString(
		JNIEnv *env, jobject) {
	return env->NewStringUTF("getCString");
}
