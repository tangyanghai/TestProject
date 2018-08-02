//
// Created by Administrator on 2018/7/17.
//

#include <string.h>
#include <jni.h>
//#include "yue_excample_hello_JniLoader.h"
//按照C语言规则编译。jni依照C的规则查找函数，而不是C++，没有这一句运行时会崩溃报错：
// java.lang.UnsatisfiedLinkError: Native method not found:
extern "C"{
JNIEXPORT jstring JNICALL Java_com_example_myapplication2_JniLoader_getHelloString
(JNIEnv *env, jobject _this)
{
//return (*env)->NewStringUTF(env, "Hello world from jni)");//C语言格式，文件名应为xxx.c
return env->NewStringUTF((char *)"Hello world from jni");//C++格式，文件名应为xxx.cpp
}
#ifdef __cplusplus
}

#endif

