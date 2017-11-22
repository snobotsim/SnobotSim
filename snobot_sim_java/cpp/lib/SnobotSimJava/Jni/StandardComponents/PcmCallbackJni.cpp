
#include "com_snobot_simulator_jni_standard_components_PcmCallbackJni.h"

#include "MockData/PCMData.h"
#include "HAL/handles/HandlesInternal.h"

#include "SnobotSimJava/Jni/RegisterJniUtilities.h"

int gSolenoidArrayIndices[20];
SnobotSimJava::CallbackHelperContainer gPcmCallbackContainer;

void SolenoidCallback(const char* name, void* param, const struct HAL_Value* value)
{
    SnobotSimJava::CallJavaCallback(gPcmCallbackContainer, name, param, value);
}

/*
 * Class:     com_snobot_simulator_jni_standard_components_PcmCallbackJni
 * Method:    registerPcmCallback
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_standard_1components_PcmCallbackJni_registerPcmCallback
  (JNIEnv * env, jclass clz, jstring aFunctionName)
{
    SnobotSimJava::SetGlobalEnvironment(env);
    std::string functionName = env->GetStringUTFChars(aFunctionName, NULL);
    SetCallbackContainerInfo(env, clz, functionName, gPcmCallbackContainer);
}

/*
 * Class:     com_snobot_simulator_jni_standard_components_PcmCallbackJni
 * Method:    reset
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_standard_1components_PcmCallbackJni_reset
  (JNIEnv *, jclass)
{
    for(int i = 0; i < HAL_GetNumSolenoidChannels(); ++i)
    {
        HALSIM_ResetPCMData(i);
    }

    for(int i = 0; i < HAL_GetNumSolenoidChannels(); ++i)
    {
        gSolenoidArrayIndices[i] = i;
        HALSIM_RegisterPCMSolenoidInitializedCallback(0, i, &SolenoidCallback, &gSolenoidArrayIndices[i], false);
        HALSIM_RegisterPCMSolenoidOutputCallback(0, i, &SolenoidCallback, &gSolenoidArrayIndices[i], false);
    }
}
