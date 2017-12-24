
#include "com_snobot_simulator_jni_standard_components_SpiCallbackJni.h"

#include "MockData/SPIData.h"

#include "SnobotSimJava/Jni/RegisterJniUtilities.h"
#include "SnobotSimJava/Jni/BufferCallbackUtilities.h"
#include "SnobotSimJava/Logging/SnobotCoutLogger.h"

int gSpiInArrayIndices[5];
SnobotSimJava::BufferCallbackHelperContainer gSpiCallbackContainer;

void SpiCallback(const char* name, void* param, const struct HAL_Value* value)
{
    SnobotSimJava::CallJavaCallback(gSpiCallbackContainer, name, param, value);
}
void SpiReadCallback(const char* name, void* param, uint8_t* buffer, uint32_t count)
{
    SnobotSimJava::CallJavaReadBufferCallback(gSpiCallbackContainer, name, param, buffer, count);
}
void SpiWriteCallback(const char* name, void* param, const uint8_t* buffer, uint32_t count)
{
    SnobotSimJava::CallJavaWriteBufferCallback(gSpiCallbackContainer, name, param, buffer, count);
}

/*
 * Class:     com_snobot_simulator_jni_standard_components_SpiCallbackJni
 * Method:    registerSpiCallback
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_standard_1components_SpiCallbackJni_registerSpiCallback
  (JNIEnv * env, jclass clz, jstring aFunctionName)
{
    SnobotSimJava::SetGlobalEnvironment(env);
    std::string functionName = env->GetStringUTFChars(aFunctionName, NULL);

    SetCallbackContainerInfo(env, clz, functionName, gSpiCallbackContainer);

    gSpiCallbackContainer.mReadBufferMethodId = env->GetStaticMethodID(gSpiCallbackContainer.mClazz, functionName.c_str(), "(Ljava/lang/String;ILjava/nio/ByteBuffer;)V");
    if (gSpiCallbackContainer.mReadBufferMethodId == NULL)
    {
        SNOBOT_LOG(SnobotLogging::CRITICAL, "Failed to find method reference for function " << functionName);
    }

    gSpiCallbackContainer.mWriteBufferMethodId = env->GetStaticMethodID(gSpiCallbackContainer.mClazz, functionName.c_str(), "(Ljava/lang/String;ILjava/nio/ByteBuffer;)V");
    if (gSpiCallbackContainer.mWriteBufferMethodId == NULL)
    {
        SNOBOT_LOG(SnobotLogging::CRITICAL, "Failed to find method reference for function " << functionName);
    }
}

/*
 * Class:     com_snobot_simulator_jni_standard_components_SpiCallbackJni
 * Method:    registerSpiReadWriteCallback
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_standard_1components_SpiCallbackJni_registerSpiReadWriteCallback
  (JNIEnv *, jclass, jint aPort)
{
    HALSIM_RegisterSPIReadCallback(aPort, &SpiReadCallback, &gSpiInArrayIndices[aPort]);
    HALSIM_RegisterSPIWriteCallback(aPort, &SpiWriteCallback, &gSpiInArrayIndices[aPort]);
}

/*
 * Class:     com_snobot_simulator_jni_standard_components_SpiCallbackJni
 * Method:    reset
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_standard_1components_SpiCallbackJni_resetNative
  (JNIEnv *, jclass)
{
    for(int i = 0; i < 5; ++i)
    {
        HALSIM_ResetSPIData(i);
    }

    for(int i = 0; i < 5; ++i)
    {
        gSpiInArrayIndices[i] = i;
        HALSIM_RegisterSPIInitializedCallback(i, &SpiCallback, &gSpiInArrayIndices[i], false);
    }
}

