
#include "com_snobot_simulator_jni_standard_components_I2CCallbackJni.h"

#include "MockData/I2CData.h"

#include "SnobotSimJava/Jni/RegisterJniUtilities.h"
#include "SnobotSimJava/Jni/BufferCallbackUtilities.h"
#include "SnobotSimJava/Logging/SnobotCoutLogger.h"

int gI2CInArrayIndices[2];
SnobotSimJava::BufferCallbackHelperContainer gI2CCallbackContainer;

void I2CCallback(const char* name, void* param, const struct HAL_Value* value)
{
    SnobotSimJava::CallJavaCallback(gI2CCallbackContainer, name, param, value);
}
void I2CReadCallback(const char* name, void* param, uint8_t* buffer, uint32_t count)
{
    SnobotSimJava::CallJavaReadBufferCallback(gI2CCallbackContainer, name, param, buffer, count);
}
void I2CWriteCallback(const char* name, void* param, const uint8_t* buffer, uint32_t count)
{
    SnobotSimJava::CallJavaWriteBufferCallback(gI2CCallbackContainer, name, param, buffer, count);
}

/*
 * Class:     com_snobot_simulator_jni_standard_components_I2CCallbackJni
 * Method:    registerI2CCallback
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_standard_1components_I2CCallbackJni_registerI2CCallback
   (JNIEnv * env, jclass clz, jstring aFunctionName)
{
    SnobotSimJava::SetGlobalEnvironment(env);
    std::string functionName = env->GetStringUTFChars(aFunctionName, NULL);
    SetCallbackContainerInfo(env, clz, functionName, gI2CCallbackContainer);

    gI2CCallbackContainer.mReadBufferMethodId = env->GetStaticMethodID(gI2CCallbackContainer.mClazz, functionName.c_str(), "(Ljava/lang/String;ILjava/nio/ByteBuffer;)V");
    if (gI2CCallbackContainer.mReadBufferMethodId == NULL)
    {
        SNOBOT_LOG(SnobotLogging::CRITICAL, "Failed to find method reference for function " << functionName);
    }

    gI2CCallbackContainer.mWriteBufferMethodId = env->GetStaticMethodID(gI2CCallbackContainer.mClazz, functionName.c_str(), "(Ljava/lang/String;ILjava/nio/ByteBuffer;)V");
    if (gI2CCallbackContainer.mWriteBufferMethodId == NULL)
    {
        SNOBOT_LOG(SnobotLogging::CRITICAL, "Failed to find method reference for function " << functionName);
    }
}

/*
 * Class:     com_snobot_simulator_jni_standard_components_I2CCallbackJni
 * Method:    registerReadWriteCallbacks
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_standard_1components_I2CCallbackJni_registerReadWriteCallbacks
  (JNIEnv *, jclass, jint aPort)
{
    HALSIM_RegisterI2CReadCallback(aPort, &I2CReadCallback, &gI2CInArrayIndices[aPort]);
    HALSIM_RegisterI2CWriteCallback(aPort, &I2CWriteCallback, &gI2CInArrayIndices[aPort]);
}
/*
 * Class:     com_snobot_simulator_jni_standard_components_I2CCallbackJni
 * Method:    reset
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_standard_1components_I2CCallbackJni_resetNative
  (JNIEnv *, jclass)
{
    for(int i = 0; i < 2; ++i)
    {
        HALSIM_ResetI2CData(i);
    }

    for(int i = 0; i < 2; ++i)
    {
        gI2CInArrayIndices[i] = i;
        HALSIM_RegisterI2CInitializedCallback(i, &I2CCallback, &gI2CInArrayIndices[i], false);
    }
}
