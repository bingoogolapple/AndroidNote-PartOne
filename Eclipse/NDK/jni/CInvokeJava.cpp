#include <jni.h>
#include <android/log.h>
#include "cn_bingoogol_ndk_jni_CInvokeJava.h"
#include "cn_bingoogol_ndk_ui_activity_MainActivity.h"

#define TAG "CInvokeJava.cpp"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, TAG, __VA_ARGS__)

JNIEXPORT void JNICALL Java_cn_bingoogol_ndk_jni_CInvokeJava_callHelloFromJava(
		JNIEnv *env, jobject obj) {
	jclass clazz = env->FindClass("cn/bingoogol/ndk/jni/CInvokeJava");
	if (clazz == 0) {
		LOGE("find class CInvokeJava error");
		return;
	}
	// javap -s cn.bingoogol.ndk.jni.CInvokeJava   获取函数的签名
	jmethodID methodId = env->GetMethodID(clazz, "helloFromJava", "()V");
	if (methodId == 0) {
		LOGE("find method helloFromJava error");
		return;
	}
	env->CallVoidMethod(obj, methodId);
}

JNIEXPORT void JNICALL Java_cn_bingoogol_ndk_jni_CInvokeJava_callAdd(
		JNIEnv *env, jobject obj) {
	jclass clazz = env->FindClass("cn/bingoogol/ndk/jni/CInvokeJava");
	if (clazz == 0) {
		LOGE("find class CInvokeJava error");
		return;
	}
	jmethodID methodId = env->GetMethodID(clazz, "add", "(II)I");
	if (methodId == 0) {
		LOGE("find method add error");
		return;
	}
	jint result = env->CallIntMethod(obj, methodId, 2, 3);
	LOGI("2+3=%d", result);
}

JNIEXPORT void JNICALL Java_cn_bingoogol_ndk_jni_CInvokeJava_callPrintStaticString(
		JNIEnv *env, jobject obj) {
	jclass clazz = env->FindClass("cn/bingoogol/ndk/jni/CInvokeJava");
	if (clazz == 0) {
		LOGE("find class CInvokeJava error");
		return;
	}
	jmethodID methodId = env->GetStaticMethodID(clazz, "printStaticString",
			"(Ljava/lang/String;)V");
	if (methodId == 0) {
		LOGE("find method printStaticString error");
		return;
	}
	env->CallStaticVoidMethod(clazz, methodId, env->NewStringUTF("静态 小苹果"));
}

JNIEXPORT void JNICALL Java_cn_bingoogol_ndk_ui_activity_MainActivity_callCInvokeJavaPrintString(
		JNIEnv *env, jobject obj) {
	jclass cInvokeJavaClazz = env->FindClass("cn/bingoogol/ndk/jni/CInvokeJava");
	if (cInvokeJavaClazz == 0) {
		LOGE("find class CInvokeJava error");
		return;
	}
	jmethodID methodId = env->GetMethodID(cInvokeJavaClazz, "printString",
			"(Ljava/lang/String;)V");
	if (methodId == 0) {
		LOGE("find method printString error");
		return;
	}

	jclass mainActivityClazz = env->FindClass("cn/bingoogol/ndk/ui/activity/MainActivity");
	if (cInvokeJavaClazz == 0) {
		LOGE("find class mainActivityClazz error");
		return;
	}

	jfieldID fieldId = env->GetFieldID(mainActivityClazz, "mCInvokeJava", "Lcn/bingoogol/ndk/jni/CInvokeJava;");
	jobject mCInvokeJavaObj = env->GetObjectField(obj, fieldId);

	env->CallVoidMethod(mCInvokeJavaObj, methodId, env->NewStringUTF("小苹果"));
}
