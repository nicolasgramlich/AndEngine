LOCAL_PATH	:= $(call my-dir)
#LOCAL_ARM_MODE	:= arm

include $(CLEAR_VARS)

include $(LOCAL_PATH)/misc/Makefile
include $(LOCAL_PATH)/loaders/Makefile
include $(LOCAL_PATH)/loaders/prowizard/Makefile
include $(LOCAL_PATH)/player/Makefile

MISC_SOURCES	:= $(addprefix misc/,$(MISC_OBJS))
LOADERS_SOURCES := $(addprefix loaders/,$(LOADERS_OBJS))
PROWIZ_SOURCES	:= $(addprefix loaders/prowizard/,$(PROWIZ_OBJS))
PLAYER_SOURCES	:= $(addprefix player/,$(PLAYER_OBJS))

XMP_VERSION	:= `grep ^VERSION $(LOCAL_PATH)/Makefile|sed 's/.* //'`
LOCAL_MODULE    := xmp
LOCAL_CFLAGS	:= -DVERSION=\"$(XMP_VERSION)\" -O3 -DHAVE_CONFIG_H -I$(LOCAL_PATH) -I$(LOCAL_PATH)/include
LOCAL_LDLIBS	:= -Lbuild/platforms/android-3/arch-arm/usr/lib -llog
LOCAL_SRC_FILES := xmp-jni.c drivers/smix.c \
	$(MISC_SOURCES:.o=.c) \
	$(LOADERS_SOURCES:.o=.c) \
	$(PROWIZ_SOURCES:.o=.c) \
	$(PLAYER_SOURCES:.o=.c.arm)

include $(BUILD_SHARED_LIBRARY)
