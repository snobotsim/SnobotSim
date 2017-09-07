
#include <assert.h>
#include <jni.h>
#include "support/jni_util.h"

#include "com_snobot_simulator_jni_RegisterCallbacksJni.h"
#include "SnobotSimJava/JavaHalCallbacks/JavaHalCallbacks.h"
#include "HAL/handles/HandlesInternal.h"
#include <iostream>

//http://adamish.com/blog/archives/327

using namespace wpi::java;


void SetCallbackContainerInfo(JNIEnv * env, jclass clz, const std::string& functionName, SnobotSimJava::CallbackHelperContainer& outContainer)
{
    outContainer.mClazz = clz;
    outContainer.mMethodId = env->GetStaticMethodID(clz, functionName.c_str(), "(Ljava/lang/String;ILcom/snobot/simulator/jni/HalCallbackValue;)V");
    if (outContainer.mMethodId == NULL)
    {
        std::cerr << "Failed to find method reference for function " << functionName << std::endl;
    }
}

extern "C"
{
/*
 * Class:     com_snobot_simulator_jni_RegisterCallbacksJni
 * Method:    reset
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_RegisterCallbacksJni_reset
  (JNIEnv *, jclass)
{
    hal::HandleBase::ResetGlobalHandles();
    SnobotSimJava::ResetMockData();
}

/*
 * Class:     com_snobot_simulator_jni_RegisterCallbacksJni
 * Method:    registerAnalogCallback
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_RegisterCallbacksJni_registerAnalogCallback
  (JNIEnv * env, jclass clz, jstring aFunctionName)
{
    SnobotSimJava::SetGlobalEnvironment(env);
    std::string functionName = env->GetStringUTFChars(aFunctionName, NULL);
    SetCallbackContainerInfo(env, clz, functionName, SnobotSimJava::GetAnalogCallback());
}

/*
 * Class:     com_snobot_simulator_jni_RegisterCallbacksJni
 * Method:    registerAnalogGyroCallback
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_RegisterCallbacksJni_registerAnalogGyroCallback
  (JNIEnv * env, jclass clz, jstring aFunctionName)
{
    SnobotSimJava::SetGlobalEnvironment(env);
    std::string functionName = env->GetStringUTFChars(aFunctionName, NULL);
    SetCallbackContainerInfo(env, clz, functionName, SnobotSimJava::GetAnalogGyroCallback());
}

/*
 * Class:     com_snobot_simulator_jni_RegisterCallbacksJni
 * Method:    registerDigitalCallback
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_RegisterCallbacksJni_registerDigitalCallback
  (JNIEnv * env, jclass clz, jstring aFunctionName)
{
    SnobotSimJava::SetGlobalEnvironment(env);
    std::string functionName = env->GetStringUTFChars(aFunctionName, NULL);
    SetCallbackContainerInfo(env, clz, functionName, SnobotSimJava::GetDigitalCallback());
}

/*
 * Class:     com_snobot_simulator_jni_RegisterCallbacksJni
 * Method:    registerEncoderCallback
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_RegisterCallbacksJni_registerEncoderCallback
  (JNIEnv * env, jclass clz, jstring aFunctionName)
{
    SnobotSimJava::SetGlobalEnvironment(env);
    std::string functionName = env->GetStringUTFChars(aFunctionName, NULL);
    SetCallbackContainerInfo(env, clz, functionName, SnobotSimJava::GetEncoderCallback());
}

/*
 * Class:     com_snobot_simulator_jni_RegisterCallbacksJni
 * Method:    registerPcmCallback
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_RegisterCallbacksJni_registerPcmCallback
  (JNIEnv * env, jclass clz, jstring aFunctionName)
{
    SnobotSimJava::SetGlobalEnvironment(env);
    std::string functionName = env->GetStringUTFChars(aFunctionName, NULL);
    SetCallbackContainerInfo(env, clz, functionName, SnobotSimJava::GetPCMCallback());
}

/*
 * Class:     com_snobot_simulator_jni_RegisterCallbacksJni
 * Method:    registerPdpCallback
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_RegisterCallbacksJni_registerPdpCallback
  (JNIEnv * env, jclass clz, jstring aFunctionName)
{
    SnobotSimJava::SetGlobalEnvironment(env);
    std::string functionName = env->GetStringUTFChars(aFunctionName, NULL);
    SetCallbackContainerInfo(env, clz, functionName, SnobotSimJava::GetPDPCallback());
}

/*
 * Class:     com_snobot_simulator_jni_RegisterCallbacksJni
 * Method:    registerPwmCallback
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_RegisterCallbacksJni_registerPwmCallback
  (JNIEnv * env, jclass clz, jstring aFunctionName)
{
    SnobotSimJava::SetGlobalEnvironment(env);
    std::string functionName = env->GetStringUTFChars(aFunctionName, NULL);
    SetCallbackContainerInfo(env, clz, functionName, SnobotSimJava::GetPWMCallback());
}

/*
 * Class:     com_snobot_simulator_jni_RegisterCallbacksJni
 * Method:    registerPwmCallback
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_RegisterCallbacksJni_registerRelayCallback
  (JNIEnv * env, jclass clz, jstring aFunctionName)
{
    SnobotSimJava::SetGlobalEnvironment(env);
    std::string functionName = env->GetStringUTFChars(aFunctionName, NULL);
    SetCallbackContainerInfo(env, clz, functionName, SnobotSimJava::GetRelayCallback());
}


}
