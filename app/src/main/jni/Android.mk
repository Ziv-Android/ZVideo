LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := AVIPlayer
LOCAL_SRC_FILES := Common.cpp com_ziv_zvideo_AbstractPlayerActivity.cpp \
                              com_ziv_zvideo_BitmapPlayerActivity.cpp
LOCAL_C_INCLUDES += $(LOCAL_PATH)/include

# Use AVILib static library
LOCAL_STATIC_LIBRARIES += avilib_static

# Link with JNI graphics
LOCAL_LDLIBS += -ljnigraphics

include $(BUILD_SHARED_LIBRARY)

# Import AVILib library module
$(call import-module, transcode-1.1.7/avilib)