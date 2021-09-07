//
// Created on 2021/6/25.
//

#ifndef RTSACONTROL_JVMMANAGER_H
#define RTSACONTROL_JVMMANAGER_H

#include <jni.h>
#include <mutex>

using JniMethodInfo = struct JniMethodInfo_ {
    JNIEnv *env;
    jclass classID;
    jmethodID methodID;
};

class JvmManager {
public:
    /* *
     * 负责Rtsa中与Jvm相关的数据初始化
     * 在So被onLoad的时候调用
     *
     * @param vm vm
     * @return 0(success),other(failed)
     */
    static int Init(JavaVM *vm);

    /* *
     * 负责Rtsa中与Jvm相关的数据析构
     * 在So被unLoad的时候调用
     *
     * @return 0(success),other(failed)
     */
    static int Release();

    /* *
     * 获取Android的app context
     * @return app context
     */
    static jobject GetGloableAppContext();

    static JavaVM *GetJavaVM();

    static JNIEnv *GetEnv();

    static bool GetMethodInfo(JniMethodInfo &methodinfo, const char *className, const char *methodName,
        const char *paramCode);

    static bool GetStaticMethodInfo(JniMethodInfo &methodinfo, const char *className, const char *methodName,
        const char *paramCode);

    static std::string jstring2string(jstring str);


private:
    static JNIEnv *CacheEnv(JavaVM *jvm);

    /* *
     * 获取App context 实例
     * @return 0(success),other(failed)
     */
    static int NewGlobalContext();

    static JavaVM *global_jvm;

    static jobject g_context;

    static std::recursive_mutex g_ctxMutex;
};


#endif // RTSACONTROL_JVMMANAGER_H
