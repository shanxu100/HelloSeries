//
// Created on 2021/9/6.
//

#ifndef HELLOSERIES_TESTJAVAMANAGER_H
#define HELLOSERIES_TESTJAVAMANAGER_H

#include <jni.h>
#include <string>
#include <mutex>
#include "JvmManager.h"

class TestJavaManager {
public:
    static int InitClazzAndFunction();

    TestJavaManager();

    ~TestJavaManager();

    int TestFun1();

    static bool TestStaticFun2();

private:
    static std::string g_testJavaManagerClassPath;

    static JniMethodInfo g_constructionInfo;

    static JniMethodInfo g_testFun1;

    static JniMethodInfo g_testStaticFun2;

    jobject g_testJavaManager;

    static std::recursive_mutex g_testMutex;
};


#endif //HELLOSERIES_TESTJAVAMANAGER_H
