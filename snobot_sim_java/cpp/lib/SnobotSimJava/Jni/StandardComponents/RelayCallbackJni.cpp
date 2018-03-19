
#include "MockData/RelayData.h"
#include "SnobotSimJava/Jni/RegisterJniUtilities.h"
#include "com_snobot_simulator_jni_standard_components_RelayCallbackJni.h"

int gRelayArrayIndices[20];
SnobotSimJava::CallbackHelperContainer gRelayCallbackContainer;

void RelayCallback(const char* name, void* param, const struct HAL_Value* value)
{
    SnobotSimJava::CallJavaCallback(gRelayCallbackContainer, name, param, value);
}

/*
 * Class:     com_snobot_simulator_jni_standard_components_RelayCallbackJni
 * Method:    registerRelayCallback
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_standard_1components_RelayCallbackJni_registerRelayCallback
  (JNIEnv * env, jclass clz, jstring aFunctionName)
{
    SnobotSimJava::SetGlobalEnvironment(env);
    std::string functionName = env->GetStringUTFChars(aFunctionName, NULL);
    SetCallbackContainerInfo(env, clz, functionName, gRelayCallbackContainer);
}

/*
 * Class:     com_snobot_simulator_jni_standard_components_RelayCallbackJni
 * Method:    reset
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_standard_1components_RelayCallbackJni_reset
  (JNIEnv *, jclass)
{
    for(int i = 0; i < HAL_GetNumRelayHeaders(); ++i)
    {
        HALSIM_ResetRelayData(i);
    }

    for(int i = 0; i < HAL_GetNumRelayHeaders(); ++i)
    {
        gRelayArrayIndices[i] = i;
        HALSIM_RegisterRelayAllCallbacks(i, &RelayCallback, &gRelayArrayIndices[i], false);
    }
}
