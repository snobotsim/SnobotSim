
#include "com_snobot_simulator_jni_standard_components_DigitalCallbackJni.h"

#include "MockData/DIOData.h"

#include "SnobotSimJava/Jni/RegisterJniUtilities.h"

int gDigitalInArrayIndices[26];
int gDigitalOutArrayIndices[26];
SnobotSimJava::CallbackHelperContainer gDigitalCallbackContainer;

void DigitalIOCallback(const char* name, void* param, const struct HAL_Value* value)
{
    SnobotSimJava::CallJavaCallback(gDigitalCallbackContainer, name, param, value);
}

/*
 * Class:     com_snobot_simulator_jni_standard_components_DigitalCallbackJni
 * Method:    setDigitalInput
 * Signature: (IZ)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_standard_1components_DigitalCallbackJni_setDigitalInput
  (JNIEnv *, jclass, jint aHandle, jboolean aState)
{
    HALSIM_SetDIOValue(aHandle, aState);
}

/*
 * Class:     com_snobot_simulator_jni_standard_components_DigitalCallbackJni
 * Method:    registerDigitalCallback
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_standard_1components_DigitalCallbackJni_registerDigitalCallback
  (JNIEnv * env, jclass clz, jstring aFunctionName)
{
    SnobotSimJava::SetGlobalEnvironment(env);
    std::string functionName = env->GetStringUTFChars(aFunctionName, NULL);
    SetCallbackContainerInfo(env, clz, functionName, gDigitalCallbackContainer);
}

/*
 * Class:     com_snobot_simulator_jni_standard_components_DigitalCallbackJni
 * Method:    reset
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_standard_1components_DigitalCallbackJni_reset
  (JNIEnv *, jclass)
{
    for(int i = 0; i < HAL_GetNumDigitalHeaders(); ++i)
    {
        HALSIM_ResetDIOData(i);
    }

    for(int i = 0; i < HAL_GetNumDigitalHeaders(); ++i)
    {
        gDigitalInArrayIndices[i] = i;
        HALSIM_RegisterDIOAllCallbacks(i, &DigitalIOCallback, &gDigitalInArrayIndices[i], false);
    }
}


