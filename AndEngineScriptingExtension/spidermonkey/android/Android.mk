LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := spidermonkey_static
LOCAL_MODULE_FILENAME := libspidermonkey
LOCAL_SRC_FILES := ../dist/lib/libspidermonkey_static.a

LOCAL_EXPORT_C_INCLUDES := $(LOCAL_PATH)/../dist/include

include $(PREBUILT_STATIC_LIBRARY)
