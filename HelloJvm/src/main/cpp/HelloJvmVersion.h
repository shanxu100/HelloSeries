//
// Created on 2021/9/3.
//

#ifndef HELLOSERIES_HELLOJVMVERSION_H
#define HELLOSERIES_HELLOJVMVERSION_H

#ifdef HELLOJVM_VERSION_NAME

// 1. 使用指定的版本号
#define HELLOJVM_VERSION_STR "Hello-Jvm " _Expand(HELLOJVM_VERSION_NAME)
// 2. 将宏展开
#define _Expand(str) _ToStr(str)
// 3. 把紧跟着的变量替换为字符串
#define _ToStr(str) #str


#else

// 不指定版本号时，使用默认值
#define HELLOJVM_VERSION_STR "Hello-Jvm 1.0.0.100"

#endif


#endif //HELLOSERIES_HELLOJVMVERSION_H
