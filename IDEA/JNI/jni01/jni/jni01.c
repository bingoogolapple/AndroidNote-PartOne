#include<stdio.h>
#include<jni.h>

//Java开头，包名，类名，方法名
jstring Java_com_bingoogol_jni01_engine_JNIProvider_helloFromC(JNIEnv* env, jobject obj) {

    //typedef const struct JNINativeInterface* JNIEnv;
    //JNIEnv                   (*env)
    //JNINativeInterface       (*(*env))

    //jstring (*NewStringUTF)(JNIEnv*, const char*);

//    return (*(*env)).NewStringUTF(env,"helloFromC");

    return (*env)->NewStringUTF(env,"helloFromC");
}

jstring Java_com_bingoogol_jni01_engine_JNIProvider_hello_1from_1c(JNIEnv* env, jobject obj) {

    return (*env)->NewStringUTF(env,"hello_1from_1c");
}