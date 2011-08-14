LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := andengine
LOCAL_CFLAGS    := -Werror
LOCAL_SRC_FILES := src/GLES20Fix.c \
					src/BufferUtils.cpp
LOCAL_LDLIBS    := -lGLESv2

include $(BUILD_SHARED_LIBRARY)
