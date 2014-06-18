LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := jni02
LOCAL_SRC_FILES := jni02.c

include $(BUILD_SHARED_LIBRARY)