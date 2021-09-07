//
// Created on 2021/9/3.
//

#include "LogPrint.h"

void LogPrint::PrintLog(int priority, const char *tag, const char *fmt, ...)
{
    va_list ap;
    char buf[LOG_BUF_SIZE];
    va_start(ap, fmt);
    int ret = vsnprintf(buf, LOG_BUF_SIZE, fmt, ap);
    if (ret == -1) {
        return;
    }
    va_end(ap);
    __android_log_write(priority, tag, buf);

}
