LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := NDK
LOCAL_SRC_FILES := CInvokeJava.cpp \
					JavaInvokeC.cpp \
					Hello.cpp

# for logging
LOCAL_LDLIBS    += -llog

include $(BUILD_SHARED_LIBRARY)
