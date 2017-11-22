
#include "com_snobot_simulator_jni_standard_components_PwmCallbackJni.h"

#include "MockData/PWMData.h"
#include "HAL/handles/HandlesInternal.h"

#include "SnobotSimJava/Jni/RegisterJniUtilities.h"

int gPwmArrayIndices[26];
SnobotSimJava::CallbackHelperContainer gPwmCallbackContainer;

void PwmCallback(const char* name, void* param, const struct HAL_Value* value)
{
    SnobotSimJava::CallJavaCallback(gPwmCallbackContainer, name, param, value);
}

/*
 * Class:     com_snobot_simulator_jni_standard_components_PwmCallbackJni
 * Method:    registerPwmCallback
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_standard_1components_PwmCallbackJni_registerPwmCallback
  (JNIEnv * env, jclass clz, jstring aFunctionName)
{
    SnobotSimJava::SetGlobalEnvironment(env);
    std::string functionName = env->GetStringUTFChars(aFunctionName, NULL);
    SetCallbackContainerInfo(env, clz, functionName, gPwmCallbackContainer);
}

/*
 * Class:     com_snobot_simulator_jni_standard_components_PwmCallbackJni
 * Method:    reset
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_standard_1components_PwmCallbackJni_reset
  (JNIEnv *, jclass)
{
    for(int i = 0; i < HAL_GetNumPWMChannels(); ++i)
    {
        HALSIM_ResetPWMData(i);
    }

    for(int i = 0; i < HAL_GetNumPWMChannels(); ++i)
    {
        gPwmArrayIndices[i] = i;
        HALSIM_RegisterPWMInitializedCallback(i, &PwmCallback, &gPwmArrayIndices[i], false);
        HALSIM_RegisterPWMSpeedCallback(i, &PwmCallback, &gPwmArrayIndices[i], false);
        HALSIM_RegisterPWMRawValueCallback(i, &PwmCallback, &gPwmArrayIndices[i], false);
        HALSIM_RegisterPWMPositionCallback(i, &PwmCallback, &gPwmArrayIndices[i], false);
    }
}
