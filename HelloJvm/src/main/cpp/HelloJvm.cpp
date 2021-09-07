//
// Created on 2021/9/2.
//

#include "HelloJvm.h"
#include "JvmManager.h"
#include "HelloJvmVersion.h"
#include <jni.h>
#include "LogPrint.h"

extern "C" JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved)
{
    if (NULL == vm) {
        return -1;
        LOGI("Jvm Manager....JNI_OnLoad Failed: vm is NULL. Version is %s", HELLOJVM_VERSION_STR);
    }
    int ret = JvmManager::Init(vm);
    LOGI("Jvm Manager....JNI_OnLoad FINISH...ret=%d.  Version is %s", ret, HELLOJVM_VERSION_STR);
    return JNI_VERSION_1_6;
}
