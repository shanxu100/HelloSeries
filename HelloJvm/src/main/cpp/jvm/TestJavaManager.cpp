//
// Created on 2021/9/6.
//

#include "TestJavaManager.h"
#include "LogPrint.h"


std::string TestJavaManager::g_testJavaManagerClassPath = "org/helloseries/hellojvm/TestJavaManager";

JniMethodInfo TestJavaManager::g_constructionInfo = {nullptr, nullptr, nullptr};

JniMethodInfo TestJavaManager::g_testFun1 = {nullptr, nullptr, nullptr};

JniMethodInfo TestJavaManager::g_testStaticFun2 = {nullptr, nullptr, nullptr};

std::recursive_mutex TestJavaManager::g_testMutex;


int TestJavaManager::InitClazzAndFunction() {
    TRACE_FUNC();
    if (JvmManager::GetGloableAppContext() == nullptr) {
        LOGE("jvmManage->g_context is nullptr");
        return -1;
    }

    // 1. 构造函数 public testJavaManager(){...}
    std::string constructionMethodName = "<init>";
    std::string constructionMethodSig = "()V";
    if (!JvmManager::GetMethodInfo(g_constructionInfo, g_testJavaManagerClassPath.c_str(),
                                   constructionMethodName.c_str(), constructionMethodSig.c_str())) {
        LOGE("can not find construction '<init>' method.");
    } else {
        LOGI("find construction '<init>' method SUCCESS");
    }

    // 2. 成员方法:public void testFun1(Context mContext){...}
    std::string initMethodName = "testFun1";
    std::string initMethodSig = "(Landroid/content/Context;)V";
    if (!JvmManager::GetMethodInfo(g_testFun1, g_testJavaManagerClassPath.c_str(),
                                   initMethodName.c_str(), initMethodSig.c_str())) {
        LOGE("can not find 'init()' method.");
    }

    // 3. 静态方法:public static bool testStaticFun2(int i){...}
    std::string checkMobileNetworkMethodName = "testStaticFun2";
    std::string checkMobileNetworkMethodSig = "(I)Z";
    if (!JvmManager::GetStaticMethodInfo(g_testStaticFun2, g_testJavaManagerClassPath.c_str(),
                                         checkMobileNetworkMethodName.c_str(),
                                         checkMobileNetworkMethodSig.c_str())) {
        LOGE("can not find 'startAcceleration()' method.");
    }

    return 0;
}

TestJavaManager::TestJavaManager() {
    g_testJavaManager = nullptr;
}

TestJavaManager::~TestJavaManager() {
    JNIEnv *env = JvmManager::GetEnv();
    if (!env) {
        LOGE("Failed to get JNIEnv");
    }
    if (g_testJavaManager != nullptr && env != nullptr) {
        env->DeleteGlobalRef(g_testJavaManager);
        g_testJavaManager = nullptr;
    }
}

int TestJavaManager::TestFun1() {

    TRACE_FUNC();
    std::lock_guard<std::recursive_mutex> lock(g_testMutex);

    jobject context = JvmManager::GetGloableAppContext();
    if (context == nullptr) {
        LOGE("jvmManage::GetGloableAppContext is nullptr");
        return -1;
    }

    if (g_constructionInfo.classID == nullptr || g_constructionInfo.methodID == nullptr) {
        LOGE("Failed to get construction '<init>': classID or methodID");
        return -1;
    }
    if (g_testFun1.classID == nullptr || g_testFun1.methodID == nullptr) {
        LOGE("Failed to get g_initInfo: classID or methodID");
        return false;
    }
    JNIEnv *env = JvmManager::GetEnv();
    if (!env) {
        LOGE("Failed to get JNIEnv");
        return false;
    }

    jobject testJavaManager = env->NewObject(g_constructionInfo.classID,
                                             g_constructionInfo.methodID);
    if (testJavaManager != nullptr) {
        g_testJavaManager = env->NewGlobalRef(testJavaManager);
    }
    if (g_testJavaManager == nullptr) {
        LOGE("Failed to get g_grsManager");
        return false;
    }
    env->CallVoidMethod(g_testJavaManager, g_testFun1.methodID, JvmManager::GetGloableAppContext());
    LOGI("TestFun1 CPP: FINISH");
    return 0;
}

bool TestJavaManager::TestStaticFun2() {
    TRACE_FUNC();
    std::lock_guard<std::recursive_mutex> lock(g_testMutex);

    jobject context = JvmManager::GetGloableAppContext();
    if (context == nullptr) {
        LOGE("jvmManage::GetGloableAppContext is nullptr");
        return -1;
    }

    if (g_testStaticFun2.classID == nullptr || g_testStaticFun2.methodID == nullptr) {
        LOGE("Failed to get initInfo: classID or methodID");
        return false;
    }
    JNIEnv *env = JvmManager::GetEnv();
    if (!env) {
        LOGE("Failed to get JNIEnv");
        return false;
    }

    env->CallStaticVoidMethod(g_testStaticFun2.classID, g_testStaticFun2.methodID,
                              JvmManager::GetGloableAppContext());

    LOGI("TestStaticFun2 CPP: FINISH");
    return 0;
}


