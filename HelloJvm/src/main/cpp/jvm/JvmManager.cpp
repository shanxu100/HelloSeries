//
// Created on 2021/6/25.
//

#include "JvmManager.h"
#include "TestJavaManager.h"
#include "LogPrint.h"
#include <string>

static pthread_key_t g_key;

void DetachCurrentThread(void *a)
{
    JvmManager::GetJavaVM()->DetachCurrentThread();
    pthread_t thisthread = pthread_self();
    LOGD("JvmManager::DetachCurrentThread(), pthread_self() = %ld", thisthread);
}


// =======================================================
//
// =======================================================

jobject JvmManager::g_context = nullptr;
JavaVM *JvmManager::global_jvm = nullptr;
std::recursive_mutex JvmManager::g_ctxMutex;

// https://cloud.tencent.com/developer/article/1650769
// https://www.coder.work/article/1495537
// http://blog.guorongfei.com/2017/01/24/android-jni-tips-md/


int JvmManager::Init(JavaVM *vm)
{
    if (vm == nullptr) {
        LOGE("vm is nullptr");
        return -1;
    }
    global_jvm = vm;

    pthread_key_create(&g_key, DetachCurrentThread);

    NewGlobalContext();

    // TODO 初始化不同的业务Manager
    TestJavaManager::InitClazzAndFunction();


    return 0;
}

int JvmManager::Release()
{
    TRACE_FUNC();

    return 0;
}


jobject JvmManager::GetGloableAppContext()
{
    TRACE_FUNC();
    std::lock_guard<std::recursive_mutex> lock(g_ctxMutex);
    return JvmManager::g_context;
}


JavaVM *JvmManager::GetJavaVM()
{
    pthread_t thisthread = pthread_self();
    LOGD("JvmManager::GetJavaVM(), pthread_self() = %ld", thisthread);
    return global_jvm;
}

JNIEnv *JvmManager::GetEnv()
{
    if (global_jvm == nullptr) {
        return nullptr;
    }
    JNIEnv *_env = (JNIEnv *)pthread_getspecific(g_key);
    if (_env == nullptr) {
        LOGD("JvmManager::GetEnv() env == null ptr");
        _env = JvmManager::CacheEnv(global_jvm);
    }
    return _env;
}

bool JvmManager::GetMethodInfo(JniMethodInfo &methodinfo, const char *className, const char *methodName,
    const char *paramCode)
{
    if ((nullptr == className) || (nullptr == methodName) || (nullptr == paramCode)) {
        return false;
    }

    JNIEnv *env = JvmManager::GetEnv();
    if (!env) {
        LOGE("Failed to get JNIEnv");
        return false;
    }

    jclass classID = env->FindClass(className);
    if (!classID) {
        LOGE("Failed to find class %s", className);
        env->ExceptionClear();
        return false;
    }

    jmethodID methodID = env->GetMethodID(classID, methodName, paramCode);
    if (!methodID) {
        LOGE("Failed to find method id of %s", methodName);
        env->ExceptionClear();
        return false;
    }

    // TODO 一个全局引用，用完记得删除
    methodinfo.classID = (jclass)env->NewGlobalRef(classID);
    methodinfo.env = env;
    methodinfo.methodID = methodID;

    return true;
}

bool JvmManager::GetStaticMethodInfo(JniMethodInfo &methodinfo, const char *className, const char *methodName,
    const char *paramCode)
{
    if ((nullptr == className) || (nullptr == methodName) || (nullptr == paramCode)) {
        return false;
    }

    JNIEnv *env = JvmManager::GetEnv();
    if (!env) {
        LOGE("Failed to get JNIEnv");
        return false;
    }

    jclass classID = env->FindClass(className);
    if (!classID) {
        LOGE("Failed to find class %s", className);
        env->ExceptionClear();
        return false;
    }

    jmethodID methodID = env->GetStaticMethodID(classID, methodName, paramCode);
    if (!methodID) {
        LOGE("Failed to find static method id of %s", methodName);
        env->ExceptionClear();
        return false;
    }

    // TODO 一个全局引用，用完记得删除
    methodinfo.classID = (jclass)env->NewGlobalRef(classID);
    methodinfo.env = env;
    methodinfo.methodID = methodID;
    return true;
}

std::string JvmManager::jstring2string(jstring jstr)
{
    if (jstr == nullptr) {
        LOGE("jstring2string Failed: jstr is null");
        return "";
    }

    JNIEnv *env = JvmManager::GetEnv();
    if (!env) {
        LOGE("jstring2string Failed: env is null");
        return "";
    }

    const char *chars = env->GetStringUTFChars(jstr, nullptr);
    std::string ret(chars);
    env->ReleaseStringUTFChars(jstr, chars);

    return ret;
}


// =========================================================================
// Private Function
// =========================================================================

JNIEnv *JvmManager::CacheEnv(JavaVM *jvm)
{
    JNIEnv *_env = nullptr;
    // get jni environment
    jint ret = jvm->GetEnv((void **)&_env, JNI_VERSION_1_6);

    switch (ret) {
        case JNI_OK:
            // Success!
            LOGD("Success CacheEnv JNI_OK");
            pthread_setspecific(g_key, _env);
            return _env;

        case JNI_EDETACHED:
            // Thread not attached
            if (jvm->AttachCurrentThread(&_env, nullptr) < 0) {
                LOGE("Failed to get the environment using AttachCurrentThread()");

                return nullptr;
            } else {
                // Success : Attached and obtained JNIEnv!
                LOGD("Success CacheEnv JNI_EDETACHED");
                pthread_setspecific(g_key, _env);
                return _env;
            }

        case JNI_EVERSION:
            // Cannot recover from this error
            LOGE("JNI interface version 1.4 not supported");
        default:
            LOGE("Failed to get the environment using GetEnv(),ret = %d", ret);
            return nullptr;
    }
}


int JvmManager::NewGlobalContext()
{
    TRACE_FUNC();
    std::lock_guard<std::recursive_mutex> lock(g_ctxMutex);

    JniMethodInfo atinfo;
    std::string activityThreadClassPath = "android/app/ActivityThread";
    std::string atMethodName = "currentActivityThread";
    std::string atSig = "()Landroid/app/ActivityThread;";
    if (!JvmManager::GetStaticMethodInfo(atinfo, activityThreadClassPath.c_str(), atMethodName.c_str(),
        atSig.c_str())) {
        LOGE("can not find 'currentActivityThread()' method.");
        return -1;
    }
    jobject atObject = atinfo.env->CallStaticObjectMethod(atinfo.classID, atinfo.methodID);
    if (atObject == nullptr) {
        LOGE("call currentActivityThread method failed.");
        return -1;
    }

    JniMethodInfo getApplicationInfo;
    std::string getApplicationMethodName = "getApplication";
    std::string getApplicationSig = "()Landroid/app/Application;";
    if (!JvmManager::GetMethodInfo(getApplicationInfo, activityThreadClassPath.c_str(),
        getApplicationMethodName.c_str(), getApplicationSig.c_str())) {
        LOGE("can not find 'getApplication()' method.");
        return -1;
    }

    jobject context = getApplicationInfo.env->CallObjectMethod(atObject, getApplicationInfo.methodID);

    if (context == nullptr) {
        LOGE("call getApplication() method failed. context is null");
        return -1;
    }
    g_context = getApplicationInfo.env->NewGlobalRef(context);
    if (g_context != nullptr) {
        LOGI("NewGlobalContext success");
    }
    LOGI("finish Creating Global Context Object");
    return 0;
}
