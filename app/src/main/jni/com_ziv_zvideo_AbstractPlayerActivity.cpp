extern "C" {
#include "avilib.h"
}

#include "Common.h"
#include "com_ziv_zvideo_AbstractPlayerActivity.h"

/*
 * Class:     com_ziv_zvideo_AbstractPlayerActivity
 * Method:    open
 * Signature: (Ljava/lang/String;)J
 */
JNIEXPORT jlong JNICALL Java_com_ziv_zvideo_AbstractPlayerActivity_open
        (JNIEnv *env, jclass clazz, jstring fileName) {
    avi_t *avi = 0;
    const char *cFileName = env->GetStringUTFChars(fileName, NULL);
    if (0 == cFileName) {
        goto exit;
    }
    // 打开AVI文件
    avi = AVI_open_input_file(cFileName, 1);
    env->ReleaseStringUTFChars(fileName, cFileName);

    if (0 == avi) {
        // 打开失败抛出异常
        ThrowException(env, "java/io/IOException", AVI_strerror());
    }

    exit:
    return (jlong) avi;
}

/*
 * Class:     com_ziv_zvideo_AbstractPlayerActivity
 * Method:    getWidth
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_ziv_zvideo_AbstractPlayerActivity_getWidth
        (JNIEnv *env, jclass clazz, jlong avi) {
    return AVI_video_width((avi_t *)avi);
}

/*
 * Class:     com_ziv_zvideo_AbstractPlayerActivity
 * Method:    getHeight
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_ziv_zvideo_AbstractPlayerActivity_getHeight
        (JNIEnv *env, jclass clazz, jlong avi) {
    return AVI_video_height((avi_t *)avi);
}

/*
 * Class:     com_ziv_zvideo_AbstractPlayerActivity
 * Method:    getFrameRate
 * Signature: (J)D
 */
JNIEXPORT jdouble JNICALL Java_com_ziv_zvideo_AbstractPlayerActivity_getFrameRate
        (JNIEnv *env, jclass clazz, jlong avi) {
    return AVI_video_frames((avi_t *)avi);
}

/*
 * Class:     com_ziv_zvideo_AbstractPlayerActivity
 * Method:    close
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_ziv_zvideo_AbstractPlayerActivity_close
        (JNIEnv *env, jclass clazz, jlong avi) {
    AVI_close((avi_t *) avi);
}