LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := jni04
LOCAL_SRC_FILES := jni04.c

#liblog.so  -l表示引入一个库
LOCAL_LDLIBS += -llog

include $(BUILD_SHARED_LIBRARY)