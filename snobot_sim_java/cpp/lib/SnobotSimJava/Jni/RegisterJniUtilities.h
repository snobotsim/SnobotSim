
#pragma once

#include <jni.h>

#include <string>

#include "MockData/HAL_Value.h"

namespace SnobotSimJava
{
    extern JavaVM* gJvm;

    struct CallbackHelperContainer
    {
        jmethodID mMethodId;
        jclass mClazz;

        CallbackHelperContainer() :
            mMethodId(NULL),
            mClazz(NULL)
        {

        }
    };

    void SetGlobalEnvironment(JNIEnv * env);

    void SetCallbackContainerInfo(JNIEnv * env, jclass clz, const std::string& functionName, SnobotSimJava::CallbackHelperContainer& outContainer);

    jobject ConvertHalValue(JNIEnv* env, const struct HAL_Value* value);

    void CallJavaCallback(const CallbackHelperContainer& callbackHelper, const char* name, void* param, const struct HAL_Value* value);
}  // namespace SnobotSimJava
