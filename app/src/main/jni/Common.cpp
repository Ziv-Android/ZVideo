//
// Created by ziv on 2017/7/20.
//

#include "Common.h"

void ThrowException(JNIEnv* env, const char* className, const char* message){
    // 获取异常类
    jclass clazz = env->FindClass(className);

    if (0 != clazz) {
        env->ThrowNew(clazz, message);
        env->DeleteLocalRef(clazz);
    }
}