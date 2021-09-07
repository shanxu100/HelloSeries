//
// Created on 2021/9/3.
//

#ifndef HELLOSERIES_HELLOJVMVERSION_H
#define HELLOSERIES_HELLOJVMVERSION_H

#ifdef HELLOJVM_VERSION_NAME

// 使用指定的版本号
#define HELLOJVM_VERSION_STR "Hello-Jvm " HELLOJVM_VERSION_NAME

#else

// 不指定版本号时，使用默认值
#define HELLOJVM_VERSION_STR "Hello-Jvm 1.0.0.100"

#endif


#endif //HELLOSERIES_HELLOJVMVERSION_H
