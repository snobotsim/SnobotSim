
#include "com_snobot_simulator_jni_standard_components_AnalogGyroCallbackJni.h"

#include "MockData/AnalogGyroData.h"
#include "HAL/handles/HandlesInternal.h"

#include "SnobotSimJava/Jni/RegisterJniUtilities.h"

int gAnalogGyroArrayIndices[26];
SnobotSimJava::CallbackHelperContainer gAnalogGyroCallbackContainer;

void AnalogGyroCallback(const char* name, void* param, const struct HAL_Value* value)
{
    SnobotSimJava::CallJavaCallback(gAnalogGyroCallbackContainer, name, param, value);
}

/*
 * Class:     com_snobot_simulator_jni_standard_components_AnalogGyroCallbackJni
 * Method:    setAnalogGyroAngle
 * Signature: (ID)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_standard_1components_AnalogGyroCallbackJni_setAnalogGyroAngle
  (JNIEnv *, jclass, jint aHandle, jdouble aAngle)
{

    HALSIM_SetAnalogGyroAngle(aHandle, aAngle);
}

/*
 * Class:     com_snobot_simulator_jni_standard_components_AnalogGyroCallbackJni
 * Method:    registerAnalogGyroCallback
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_standard_1components_AnalogGyroCallbackJni_registerAnalogGyroCallback
  (JNIEnv * env, jclass clz, jstring aFunctionName)
{
    SnobotSimJava::SetGlobalEnvironment(env);
    std::string functionName = env->GetStringUTFChars(aFunctionName, NULL);
    SetCallbackContainerInfo(env, clz, functionName, gAnalogGyroCallbackContainer);
}

/*
 * Class:     com_snobot_simulator_jni_standard_components_AnalogGyroCallbackJni
 * Method:    reset
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_standard_1components_AnalogGyroCallbackJni_reset
  (JNIEnv *, jclass)
{
    for(int i = 0; i < HAL_GetNumAccumulators(); ++i)
    {
        HALSIM_ResetAnalogGyroData(i);
    }

    // Initialize
    for(int i = 0; i < HAL_GetNumAccumulators(); ++i)
    {
        gAnalogGyroArrayIndices[i] = i;
        HALSIM_RegisterAnalogGyroInitializedCallback(i, &AnalogGyroCallback, &gAnalogGyroArrayIndices[i], false);
        HALSIM_RegisterAnalogGyroAngleCallback(i, &AnalogGyroCallback, &gAnalogGyroArrayIndices[i], false);
        HALSIM_RegisterAnalogGyroRateCallback(i, &AnalogGyroCallback, &gAnalogGyroArrayIndices[i], false);
    }
}

