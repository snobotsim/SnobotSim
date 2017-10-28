
#include <assert.h>
#include <jni.h>
#include "support/jni_util.h"

#include "com_snobot_simulator_jni_RegisterCallbacksJni.h"
#include "SnobotSimJava/JavaHalCallbacks/JavaHalCallbacks.h"
#include "SnobotSimJava/Logging/SnobotLogger.h"
#include "SnobotSimJava/Logging/SnobotCoutLogger.h"
#include "HAL/handles/HandlesInternal.h"

//http://adamish.com/blog/archives/327

static SnobotLogging::ISnobotLogger* sSnobotLogger = NULL;

using namespace wpi::java;


void SetCallbackContainerInfo(JNIEnv * env, jclass clz, const std::string& functionName, SnobotSimJava::CallbackHelperContainer& outContainer)
{
    outContainer.mClazz = clz;
    outContainer.mMethodId = env->GetStaticMethodID(clz, functionName.c_str(), "(Ljava/lang/String;ILcom/snobot/simulator/jni/HalCallbackValue;)V");
    if (outContainer.mMethodId == NULL)
    {
        SNOBOT_LOG(SnobotLogging::CRITICAL, "Failed to find method reference for function " << functionName);
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

    if(sSnobotLogger)
    {
        delete sSnobotLogger;
    }

    sSnobotLogger = new SnobotLogging::SnobotCoutLogger();
    sSnobotLogger->SetLogLevel(SnobotLogging::DEBUG);
    SnobotLogging::SetLogger(sSnobotLogger);
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
 * Method:    registerCanCallback
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_RegisterCallbacksJni_registerCanCallback
  (JNIEnv * env, jclass clz, jstring aFunctionName)
{
    SnobotSimJava::SetGlobalEnvironment(env);
    std::string functionName = env->GetStringUTFChars(aFunctionName, NULL);
    SetCallbackContainerInfo(env, clz, functionName, SnobotSimJava::GetCanCallback());
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
 * Method:    registerI2CCallback
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_RegisterCallbacksJni_registerI2CCallback
  (JNIEnv * env, jclass clz, jstring aFunctionName)
{
    SnobotSimJava::SetGlobalEnvironment(env);
    std::string functionName = env->GetStringUTFChars(aFunctionName, NULL);
    SnobotSimJava::BufferCallbackHelperContainer& callbackContainer = SnobotSimJava::GetI2CCallback();
    SetCallbackContainerInfo(env, clz, functionName, callbackContainer);

    callbackContainer.mReadBufferMethodId = env->GetStaticMethodID(callbackContainer.mClazz, functionName.c_str(), "(Ljava/lang/String;ILjava/nio/ByteBuffer;)V");
    if (callbackContainer.mReadBufferMethodId == NULL)
    {
        SNOBOT_LOG(SnobotLogging::CRITICAL, "Failed to find method reference for function " << functionName);
    }

    callbackContainer.mWriteBufferMethodId = env->GetStaticMethodID(callbackContainer.mClazz, functionName.c_str(), "(Ljava/lang/String;ILjava/nio/ByteBuffer;)V");
    if (callbackContainer.mWriteBufferMethodId == NULL)
    {
        SNOBOT_LOG(SnobotLogging::CRITICAL, "Failed to find method reference for function " << functionName);
    }
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

/*
 * Class:     com_snobot_simulator_jni_RegisterCallbacksJni
 * Method:    registerSpiCallback
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_RegisterCallbacksJni_registerSpiCallback
  (JNIEnv * env, jclass clz, jstring aFunctionName)
{
    SnobotSimJava::SetGlobalEnvironment(env);
    std::string functionName = env->GetStringUTFChars(aFunctionName, NULL);

    SnobotSimJava::BufferCallbackHelperContainer& callbackContainer = SnobotSimJava::GetSpiCallback();

    SetCallbackContainerInfo(env, clz, functionName, callbackContainer);

    callbackContainer.mReadBufferMethodId = env->GetStaticMethodID(callbackContainer.mClazz, functionName.c_str(), "(Ljava/lang/String;ILjava/nio/ByteBuffer;)V");
    if (callbackContainer.mReadBufferMethodId == NULL)
    {
        SNOBOT_LOG(SnobotLogging::CRITICAL, "Failed to find method reference for function " << functionName);
    }

    callbackContainer.mWriteBufferMethodId = env->GetStaticMethodID(callbackContainer.mClazz, functionName.c_str(), "(Ljava/lang/String;ILjava/nio/ByteBuffer;)V");
    if (callbackContainer.mWriteBufferMethodId == NULL)
    {
        SNOBOT_LOG(SnobotLogging::CRITICAL, "Failed to find method reference for function " << functionName);
    }
}


}
