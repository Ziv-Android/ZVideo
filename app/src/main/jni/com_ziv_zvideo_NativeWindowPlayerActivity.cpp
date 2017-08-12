extern "C" {
#include <avilib.h>
}

#include <android/native_window_jni.h>

#include "Common.h"
#include "com_ziv_zvideo_NativeWindowPlayerActivity.h"

/*
 * Class:     com_ziv_zvideo_NativeWindowPlayerActivity
 * Method:    init
 * Signature: (JLandroid/view/Surface;)V
 */
JNIEXPORT void JNICALL Java_com_ziv_zvideo_NativeWindowPlayerActivity_init
        (JNIEnv *env, jclass clazz, jlong avi, jobject surface) {
    // 从surface中获取原生window
    ANativeWindow *nativeWindow = ANativeWindow_fromSurface(env, surface);
    if (0 == nativeWindow) {
        ThrowException(env, "java/io/RuntimeException",
                       "Unable to get native window from surface.");
        goto exit;
    }

    // 设置buffer的大小为AVI视频帧的分辨率
    // 如果和window的物理大小不一致，则缩放buffer匹配屏幕尺寸
    if (0 > ANativeWindow_setBuffersGeometry(nativeWindow,
                                             AVI_video_width((avi_t *) avi),
                                             AVI_video_height((avi_t *) avi),
                                             WINDOW_FORMAT_RGB_565)) {
        ThrowException(env, "java/io/RuntimeException", "Unable to set buffers geometry.");
        goto exit;
    }

    // 释放原生window
    ANativeWindow_release(nativeWindow);
    nativeWindow = 0;

    exit:
    return;
}

/*
 * Class:     com_ziv_zvideo_NativeWindowPlayerActivity
 * Method:    render
 * Signature: (JLandroid/view/Surface;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_ziv_zvideo_NativeWindowPlayerActivity_render
        (JNIEnv *env, jclass clazz, jlong avi, jobject surface) {
    jboolean isFrameRead = JNI_FALSE;

    long frameSize = 0;
    int keyFrame = 0;

    // 从surface中获取window
    ANativeWindow *nativeWindow = ANativeWindow_fromSurface(env, surface);
    if (0 == nativeWindow) {
        ThrowException(env, "java/io/RuntimeException",
                       "Unable to get native window from surface.");
        goto exit;
    }

    // 锁定原生window并访问原始buffer
    ANativeWindow_Buffer windowBuffer;
    if (0 > ANativeWindow_lock(nativeWindow, &windowBuffer, 0)) {
        ThrowException(env, "java/io/RuntimeException", "Unable to lock native window.");
        goto release;
    }

    // 将AVI帧的比特流读至原始缓冲区
    frameSize = AVI_read_frame((avi_t *) avi, (char *) windowBuffer.bits, &keyFrame);

    // 检查是否读取成功
    if (0 > frameSize) {
        isFrameRead = JNI_TRUE;
    }

    // 解锁原生window并输出缓冲区显示
    if (0 > ANativeWindow_unlockAndPost(nativeWindow)) {
        ThrowException(env, "java/io/RuntimeException",
                       "Unable to unlock and post to native window");
        goto release;
    }

    release:
    // 释放原生window
    ANativeWindow_release(nativeWindow);
    nativeWindow = 0;

    exit:
    return isFrameRead;
}