# 简单AVI播放器实现

本应用使用开源项目Transcode中的AVILib搭建，目前版本v1.1.7
下载地址http://www.linuxfromscratch.org/blfs/view/svn/multimedia/transcode.html
解压文件，Linux使用`tar jxvf <download>/transcode-1.1.7.tar.bz2`，工程中的文件已经完成步骤1和步骤2

1. 修改transcode-1.1.7/avilib/platfrom.h文件中的
    
    ```h
    #ifdef HAVE_CONFIG_H
    #include "config.h"
    #endif
    ```
2. 在avilib目录下创建Android.mk文件
    ```makefile
    LOCAL_PATH := $(call my-dir)
    
    # 转码AVILib
    MY_AVILIB_SRC_FILES := avilib.c platfrom_posix.c
    MY_AVILIB_C_INCLUDES := $(LOCAL_PATH)
    
    include $(CLEAR_VARS)
    
    LOCAL_MODULE := avilib_static
    LOCAL_SRC_FILES := $(MY_AVILIB_SRC_FILES)
    # 包含导出路径
    LOCAL_EXCEPORT_C_INCLUDES := $(MY_AVILIB_C_INCLUDES)
    
    include $(BUILD_STATIC_LIBRARY)
    
    include $(CLEAR_VARS)
    
    LOCAL_MODULE := avilib_shared
    LOCAL_SRC_FILES := $(MY_AVILIB_SRC_FILES)
    # 包含导出路径
    LOCAL_EXCEPORT_C_INCLUDES := $(MY_AVILIB_C_INCLUDES)
    
    include $(BUILD_SHARED_LIBRARY)
    ```
3. 创建Android工程并实现简单GUI界面
4. 复制文件夹transcode-1.1.7到`NDK_HOME/sources`目录下
5. 创建include文件夹，复制avilib.h头文件在其中
6. 以共享库的形式添加工程native依赖，编辑工程中的Android.mk文件
    ```makefile
    LOCAL_PATH := $(call my-dir)
    
    include $(CLEAR_VARS)
    
    LOCAL_MODULE := AVIPlayer
    LOCAL_C_INCLUDES += $(LOCAL_PATH)/include
    LOCAL_STATIC_LIBRARIES += avilib_static
    LOCAL_SRC_FILES := Common.cpp com_ziv_zvideo_AbstractPlayerActivity.cpp
    
    include $(BUILD_SHARED_LIBRARY)
    
    $(call import-module, transcode-1.1.7/avilib)
    ```
7. 实现native调用，渲染视频，展示