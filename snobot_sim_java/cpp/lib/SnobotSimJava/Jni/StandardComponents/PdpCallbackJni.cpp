
#include "com_snobot_simulator_jni_standard_components_PdpCallbackJni.h"

#include "MockData/PDPData.h"

#include "SnobotSimJava/Jni/RegisterJniUtilities.h"

int gPdpArrayIndices[64];
SnobotSimJava::CallbackHelperContainer gPdpCallbackContainer;

void PdpCallback(const char* name, void* param, const struct HAL_Value* value)
{
    SnobotSimJava::CallJavaCallback(gPdpCallbackContainer, name, param, value);
}

/*
 * Class:     com_snobot_simulator_jni_standard_components_PdpCallbackJni
 * Method:    registerPdpCallback
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_standard_1components_PdpCallbackJni_registerPdpCallback
  (JNIEnv * env, jclass clz, jstring aFunctionName)
{
    SnobotSimJava::SetGlobalEnvironment(env);
    std::string functionName = env->GetStringUTFChars(aFunctionName, NULL);
    SetCallbackContainerInfo(env, clz, functionName, gPdpCallbackContainer);
}

/*
 * Class:     com_snobot_simulator_jni_standard_components_PdpCallbackJni
 * Method:    reset
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_standard_1components_PdpCallbackJni_reset
  (JNIEnv *, jclass)
{
    for(int i = 0; i < HAL_GetNumPDPModules(); ++i)
    {
        HALSIM_ResetPDPData(i);
    }

    for(int i = 0; i < HAL_GetNumPDPModules(); ++i)
    {
        gPdpArrayIndices[i] = i;
        HALSIM_RegisterPDPAllNonCurrentCallbacks(i, 0, &PdpCallback, &gPdpArrayIndices[i], false);
    }
}
