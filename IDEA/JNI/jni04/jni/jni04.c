#include<stdio.h>
#include <jni.h>
#include <android/log.h>
#include<malloc.h>
#include"com_bingoogol_jni04_engine_DataProvider.h"
#define LOG_TAG "jni"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

/**
 * 返回值 char* 这个代表char数组的首地址
 *  Jstring2CStr 把java中的jstring的类型转化成一个c语言中的char 字符串
 */
char*   Jstring2CStr(JNIEnv*   env,   jstring   jstr)
{
	 char*   rtn   =   NULL;
	 jclass   clsstring   =   (*env)->FindClass(env,"java/lang/String"); //String
	 jstring   strencode   =   (*env)->NewStringUTF(env,"GB2312");  // 得到一个java字符串 "GB2312"
	 jmethodID   mid   =   (*env)->GetMethodID(env,clsstring,   "getBytes",   "(Ljava/lang/String;)[B"); //[ String.getBytes("gb2312");
	 jbyteArray   barr=   (jbyteArray)(*env)->CallObjectMethod(env,jstr,mid,strencode); // String .getByte("GB2312");
	 jsize   alen   =   (*env)->GetArrayLength(env,barr); // byte数组的长度
	 jbyte*   ba   =   (*env)->GetByteArrayElements(env,barr,JNI_FALSE);
	 if(alen   >   0)
	 {
	  rtn   =   (char*)malloc(alen+1);         //"\0"
	  memcpy(rtn,ba,alen);
	  rtn[alen]=0;
	 }
	 (*env)->ReleaseByteArrayElements(env,barr,ba,0);  //
	 return rtn;
}


/**env java虚拟机 结构体c实现的指针 包含的有很多jni方法
 *jobject obj 代表的是调用这个c代码的java对象 代表的是DataProider的对象
 */
JNIEXPORT jint JNICALL Java_com_bingoogol_jni04_engine_DataProvider_add
  (JNIEnv* env, jobject obj, jint x, jint y) {
    //java中的int占4个字节，c语言中的int也是占4个字节，等价的
    LOGI("x=%d\n",x);
    LOGI("y=%d\n",y);
    return x + y;
}


JNIEXPORT jstring JNICALL Java_com_bingoogol_jni04_engine_DataProvider_sayHelloInc
  (JNIEnv* env, jobject obj, jstring jstr) {
    //在c语言中 是没有java的String
    char* cstr = Jstring2CStr(env, jstr);
    LOGI("cstr=%s",cstr);
    // c语言中的字符串 都是以'/0' 作为结尾
    char arr[7]= {' ','h','e','l','l','o','\0'};
    strcat(cstr,arr);
    LOGI("new cstr=%s",cstr);
    return (*env)->NewStringUTF(env,cstr);
}

JNIEXPORT jintArray JNICALL Java_com_bingoogol_jni04_engine_DataProvider_intMethod
  (JNIEnv* env, jobject obj, jintArray arr) {
    //1.知道数组的长度
    //2.操作这个数组里面的每一个元素
    int len = (*env)->GetArrayLength(env,arr);
    LOGI("shuzu len =%d",len);
    //    jint*       (*GetIntArrayElements)(JNIEnv*, jintArray, jboolean*); 是否允许拷贝?
    jint* intarr = (*env)->GetIntArrayElements(env,arr,0);
    int i =0; //c99 循环的变量的初始化条件要在for循环的外面
    for(;i<len;i++){
    	//*(intarr+i) += 10;

    	LOGI("intarr[%d]=%d", i, intarr[i]);

    	intarr[i]+= 10;
    }
    //    void        (*ReleaseIntArrayElements)(JNIEnv*, jintArray,
    //                        jint*, jint);
    //

    //(*env)->ReleaseIntArrayElements(env,arr,intarr,0); // c语言释放掉 刚才申请的内存空间
    return arr;
}


/**
 * 代表的是调用c代码 的class类
 * jclass DataProvider  类
 */
JNIEXPORT jint JNICALL Java_com_bingoogol_jni04_engine_DataProvider_sub
  (JNIEnv* env, jclass clazz, jint x, jint y) {
        LOGI("x=%d\n",x);
        LOGI("y=%d\n",y);
        return x - y;
}