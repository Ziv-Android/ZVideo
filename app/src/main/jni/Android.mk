LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := AVIPlayer
LOCAL_SRC_FILES := Common.cpp com_ziv_zvideo_AbstractPlayerActivity.cpp \
                              com_ziv_zvideo_BitmapPlayerActivity.cpp \
                              com_ziv_zvideo_OpenGLPlayerActivity.cpp \
                              com_ziv_zvideo_NativeWindowPlayerActivity.cpp

LOCAL_C_INCLUDES += $(LOCAL_PATH)/include

# Use AVILib static library
LOCAL_STATIC_LIBRARIES += avilib_static

# 启动 GL ext 原型
LOCAL_CFLAGS += -DGL_GLEXT_PROTOTYPES

# Link with JNI graphics  and OpenGL ES
LOCAL_LDLIBS += -ljnigraphics -lGLESv1_CM -lEGL -landroid

include $(BUILD_SHARED_LIBRARY)

# Import AVILib library module
$(call import-module, transcode-1.1.7/avilib)