LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE             := andengine_shared
LOCAL_MODULE_FILENAME    := libandengine
LOCAL_CFLAGS             := -Werror
LOCAL_SRC_FILES          := src/GLES20Fix.c \
                             src/BufferUtils.cpp
LOCAL_LDLIBS             := -lGLESv2
LOCAL_EXPORT_C_INCLUDES  := $(LOCAL_PATH)/src

include $(BUILD_SHARED_LIBRARY)
