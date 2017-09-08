
#include <assert.h>
#include <jni.h>
#include "support/jni_util.h"

#include "com_snobot_simulator_jni_SensorFeedbackJni.h"

#include "MockData/AnalogInData.h"
#include "MockData/AnalogGyroData.h"
#include "MockData/DIOData.h"
#include "MockData/DriverStationData.h"
#include "MockData/EncoderData.h"

#include <iostream>

extern "C"
{


/*
 * Class:     com_snobot_simulator_jni_SensorFeedbackJni
 * Method:    setAnalogGyroAngle
 * Signature: (D)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_SensorFeedbackJni_setAnalogGyroAngle
  (JNIEnv *, jclass, jint aHandle, jdouble aAngle)
{
    HALSIM_SetAnalogGyroAngle(aHandle, aAngle);
}


/*
 * Class:     com_snobot_simulator_jni_SensorFeedbackJni
 * Method:    setEncoderDistance
 * Signature: (ID)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_SensorFeedbackJni_setEncoderDistance
  (JNIEnv *, jclass, jint aHandle, jdouble aDistance)
{
    HALSIM_SetEncoderCount(aHandle, (int) aDistance);
}


/*
 * Class:     com_snobot_simulator_jni_SensorFeedbackJni
 * Method:    setDigitalInput
 * Signature: (IZ)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_SensorFeedbackJni_setDigitalInput
  (JNIEnv *, jclass, jint aHandle, jboolean aState)
{
    HALSIM_SetDIOValue(aHandle, aState);
}


/*
 * Class:     com_snobot_simulator_jni_SensorFeedbackJni
 * Method:    setAnalogVoltage
 * Signature: (ID)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_SensorFeedbackJni_setAnalogVoltage
  (JNIEnv *, jclass, jint aHandle, jdouble aVoltage)
{
    HALSIM_SetAnalogInVoltage(aHandle, aVoltage);
}

/*
 * Class:     com_snobot_simulator_jni_SensorFeedbackJni
 * Method:    setEnabled
 * Signature: (Z)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_SensorFeedbackJni_setEnabled
  (JNIEnv *, jclass, jboolean aEnabled)
{
    HALSIM_SetDriverStationDsAttached(aEnabled);
    HALSIM_SetDriverStationEnabled(aEnabled);
}

/*
 * Class:     com_snobot_simulator_jni_SensorFeedbackJni
 * Method:    setAutonomous
 * Signature: (Z)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_SensorFeedbackJni_setAutonomous
  (JNIEnv *, jclass, jboolean aAuton)
{
    HALSIM_SetDriverStationAutonomous(aAuton);
}

/*
 * Class:     com_snobot_simulator_jni_SensorFeedbackJni
 * Method:    notifyDsOfData
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_SensorFeedbackJni_notifyDsOfData
  (JNIEnv *, jclass)
{
    HALSIM_NotifyDriverStationNewData();
}


/*
 * Class:     com_snobot_simulator_jni_SensorFeedbackJni
 * Method:    setJoystickInformation
 * Signature: (I[F[SII)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_SensorFeedbackJni_setJoystickInformation
  (JNIEnv * env, jclass,
          jint aHandle, jfloatArray aAxes, jshortArray aPovs, jint aButtonCount, jint aButtonMask)
{

    HAL_JoystickAxes newAxes;
    HAL_JoystickPOVs newPov;
    HAL_JoystickButtons newButtons;

    newButtons.count = aButtonCount;
    newButtons.buttons = aButtonMask;

    float* axes = env->GetFloatArrayElements(aAxes, NULL);
    newAxes.count = env->GetArrayLength(aAxes);
    for (int i = 0; i < newAxes.count; ++i)
    {
        newAxes.axes[i] = axes[i];
    }
    env->ReleaseFloatArrayElements(aAxes, axes, 0);

    short* povs = env->GetShortArrayElements(aPovs, NULL);
    newPov.count = env->GetArrayLength(aPovs);
    for (int i = 0; i < newPov.count; ++i)
    {
        newPov.povs[i] = povs[i];
    }
    env->ReleaseShortArrayElements(aPovs, povs, 0);

    HALSIM_SetJoystickAxes(aHandle, newAxes);
    HALSIM_SetJoystickPOVs(aHandle, newPov);
    HALSIM_SetJoystickButtons(aHandle, newButtons);
}

}
