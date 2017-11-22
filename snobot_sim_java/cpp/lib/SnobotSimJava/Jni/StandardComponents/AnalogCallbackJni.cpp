
#include "com_snobot_simulator_jni_standard_components_AnalogCallbackJni.h"

#include "MockData/AnalogInData.h"
#include "MockData/AnalogOutData.h"

#include "SnobotSimJava/Jni/RegisterJniUtilities.h"

int gAnalogInArrayIndices[26];
int gAnalogOutArrayIndices[26];

SnobotSimJava::CallbackHelperContainer gAnalogCallbackContainer;

void AnalogIOCallback(const char* name, void* param, const struct HAL_Value* value)
{
    SnobotSimJava::CallJavaCallback(gAnalogCallbackContainer, name, param, value);
}

/*
 * Class:     com_snobot_simulator_jni_standard_components_AnalogCallbackJni
 * Method:    setAnalogVoltage
 * Signature: (ID)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_standard_1components_AnalogCallbackJni_setAnalogVoltage
  (JNIEnv *, jclass, jint aHandle, jdouble aVoltage)
{
    HALSIM_SetAnalogInVoltage(aHandle, aVoltage);
}

/*
 * Class:     com_snobot_simulator_jni_standard_components_AnalogCallbackJni
 * Method:    registerAnalogCallback
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_standard_1components_AnalogCallbackJni_registerAnalogCallback
  (JNIEnv * env, jclass clz, jstring aFunctionName)
{
    SnobotSimJava::SetGlobalEnvironment(env);
    std::string functionName = env->GetStringUTFChars(aFunctionName, NULL);
    SetCallbackContainerInfo(env, clz, functionName, gAnalogCallbackContainer);
}

/*
 * Class:     com_snobot_simulator_jni_standard_components_AnalogCallbackJni
 * Method:    reset
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_standard_1components_AnalogCallbackJni_reset
  (JNIEnv *, jclass)
{
    for(int i = 0; i < HAL_GetNumAnalogInputs(); ++i)
    {
        HALSIM_ResetAnalogInData(i);
    }

    for(int i = 0; i < HAL_GetNumAnalogOutputs(); ++i)
    {
        HALSIM_ResetAnalogOutData(i);
    }

    // Initialize
    for(int i = 0; i < HAL_GetNumAnalogInputs(); ++i)
    {
        gAnalogInArrayIndices[i] = i;
        HALSIM_RegisterAnalogInInitializedCallback(i, &AnalogIOCallback, &gAnalogInArrayIndices[i], false);
    }

    for(int i = 0; i < HAL_GetNumAnalogOutputs(); ++i)
    {
        gAnalogOutArrayIndices[i] = i;
        HALSIM_RegisterAnalogOutInitializedCallback(i, &AnalogIOCallback, &gAnalogOutArrayIndices[i], false);
    }
}


