//
// Created on 2021/9/3.
//

#ifndef HELLOSERIES_LOGPRINT_H
#define HELLOSERIES_LOGPRINT_H
#include <string.h>
#include "stdarg.h"
#include <android/log.h>
#include <cstdio>
#include <linux/time.h>
#include <sys/time.h>


const int LOG_BUF_SIZE = 3072;
const char COMPART = '/';
const char * const g_LogTag = "HelloJvm_";

/**
 * 获取简略的文件名
 */
static char *ConvertToShortFileName(const char *pszFileName)
{
    char *pszPret = const_cast<char *>(pszFileName);
    char *pszNext = nullptr;

    if (pszFileName == nullptr) {
        return nullptr;
    }

    if ((pszNext = strrchr(pszPret, COMPART)) != nullptr) {
        pszPret = pszNext + 1;
    }

    return pszPret;
}


/**
 * 函数入口、出口的类
 */
class FunctionEntryTrace {
public:
    FunctionEntryTrace(const char *funcName, const char *filename) : funcName_(funcName), fileName_(filename)
    {
        dTime = GetCurrentTime();
        __android_log_print(ANDROID_LOG_INFO, g_LogTag, "[%s:%s] Function ENTER: ", ConvertToShortFileName(fileName_),
                            funcName_);
    }

    ~FunctionEntryTrace()
    {
        long long curTime = GetCurrentTime();
        __android_log_print(ANDROID_LOG_INFO, g_LogTag, "[%s:%s] Function LEAVE: Time consumed [%ld]",
                            ConvertToShortFileName(fileName_), funcName_, curTime - dTime);
        funcName_ = nullptr;
        dTime = 0;
    }

    long long GetCurrentTime()
    {
        struct timeval tv;
        gettimeofday(&tv, NULL);
        return ((long long)tv.tv_sec) * 1000 + ((long long)tv.tv_usec) / 1000; // 1000是单位转换
    }

private:
    const char *funcName_;
    const char *fileName_;
    long long dTime;
};

// 记录函数入口和出口
#define TRACE_FUNC() FunctionEntryTrace FUNC_ENTRY_TRACE(__FUNCTION__, __FILE__)

// 打印API调用
#define TRACE_API_FUNC() LogPrint::PrintLog(ANDROID_LOG_INFO, g_LogTag, "[%s] API: %s ", ConvertToShortFileName(__FILE__), __FUNCTION__)

// 按照等级打印日志
#define LOGV(fmt, ...)                                                                                       \
    LogPrint::PrintLog(ANDROID_LOG_VERBOSE, g_LogTag, "[%s:%s](%u): " fmt, ConvertToShortFileName(__FILE__), \
        __FUNCTION__, __LINE__, ##__VA_ARGS__)
#define LOGI(fmt, ...)                                                                                    \
    LogPrint::PrintLog(ANDROID_LOG_INFO, g_LogTag, "[%s:%s](%u): " fmt, ConvertToShortFileName(__FILE__), \
        __FUNCTION__, __LINE__, ##__VA_ARGS__)
#define LOGD(fmt, ...)                                                                                     \
    LogPrint::PrintLog(ANDROID_LOG_DEBUG, g_LogTag, "[%s:%s](%u): " fmt, ConvertToShortFileName(__FILE__), \
        __FUNCTION__, __LINE__, ##__VA_ARGS__)
#define LOGW(fmt, ...)                                                                                    \
    LogPrint::PrintLog(ANDROID_LOG_WARN, g_LogTag, "[%s:%s](%u): " fmt, ConvertToShortFileName(__FILE__), \
        __FUNCTION__, __LINE__, ##__VA_ARGS__)
#define LOGE(fmt, ...)                                                                                     \
    LogPrint::PrintLog(ANDROID_LOG_ERROR, g_LogTag, "[%s:%s](%u): " fmt, ConvertToShortFileName(__FILE__), \
        __FUNCTION__, __LINE__, ##__VA_ARGS__)


class LogPrint {
public:
    /**
     * 日志打印的接口
     */
    static void PrintLog(int priority, const char *tag, const char *fmt, ...);
};


#endif //HELLOSERIES_LOGPRINT_H
