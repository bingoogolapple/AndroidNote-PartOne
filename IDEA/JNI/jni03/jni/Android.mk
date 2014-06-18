LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := jni03
LOCAL_SRC_FILES := jni03.c

#liblog.so  -l表示引入一个库
LOCAL_LDLIBS += -llog

include $(BUILD_SHARED_LIBRARY)