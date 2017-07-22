extern "C" {
#include <avilib.h>
}

#include "Common.h"
#include "com_ziv_zvideo_AbstractPlayerActivity.h"

/*
 * Class:     com_ziv_zvideo_AbstractPlayerActivity
 * Method:    open
 * Signature: (Ljava/lang/String;)J
 */
JNIEXPORT jlong JNICALL Java_com_ziv_zvideo_AbstractPlayerActivity_open
  (JNIEnv *env, jclass clazz, jstring fileName){
    avi_t* avi = 0;
}

/*
 * Class:     com_ziv_zvideo_AbstractPlayerActivity
 * Method:    getWidth
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_ziv_zvideo_AbstractPlayerActivity_getWidth
  (JNIEnv *env, jclass clazz, jlong avi){

}

/*
 * Class:     com_ziv_zvideo_AbstractPlayerActivity
 * Method:    getHeight
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_ziv_zvideo_AbstractPlayerActivity_getHeight
  (JNIEnv *env, jclass clazz, jlong avi){

}

/*
 * Class:     com_ziv_zvideo_AbstractPlayerActivity
 * Method:    getFrameRate
 * Signature: (J)D
 */
JNIEXPORT jdouble JNICALL Java_com_ziv_zvideo_AbstractPlayerActivity_getFrameRate
  (JNIEnv *env, jclass clazz, jlong avi){

}

/*
 * Class:     com_ziv_zvideo_AbstractPlayerActivity
 * Method:    close
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_ziv_zvideo_AbstractPlayerActivity_close
  (JNIEnv *env, jclass clazz, jlong avi){

}