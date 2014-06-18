#交叉编译器在编译c代码/c++代码依赖的配置文件   linux下makefile的语法的子集
#makefile中的函数 $()
#获取当前Android.mk的路径
LOCAL_PATH := $(call my-dir)

#变量的初始化操作，清空变量的值，特点：不会重新初始化LOCAL_PATH的变量
include $(CLEAR_VARS)


#libjni01.so  加lib前缀  .so后缀

#ndk-build
#ndk-build clean

#libjni01.so  加lib前缀  .so后缀  makefile的语法约定的

#即使这里写的是LOCAL_MODULE    := libjni01  java中也不能加lib前缀，System.loadLibrary("jni01");
LOCAL_MODULE    := jni01
LOCAL_SRC_FILES := jni01.c




#别的参数

#LOCAL_CPP_EXTENSION := cc //指定c++文件的扩展名
#LOCAL_MODULE    := ndkfoo
#LOCAL_SRC_FILES := ndkfoo.cc

#指定需要加载一些别的什么库.
#LOCAL_LDLIBS += -llog -lvmsagent -lmpnet -lmpxml -lH264Android

include $(BUILD_SHARED_LIBRARY)