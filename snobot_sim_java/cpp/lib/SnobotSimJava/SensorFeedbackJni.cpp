
#include <assert.h>
#include <jni.h>
#include "support/jni_util.h"

#include <chrono>

#include "com_snobot_simulator_jni_SensorFeedbackJni.h"

#include "MockData/AnalogInData.h"
#include "MockData/AnalogGyroData.h"
#include "MockData/DIOData.h"
#include "MockData/DriverStationData.h"
#include "MockData/EncoderData.h"
#include "MockData/I2CData.h"
#include "MockData/MockHooks.h"
#include "MockData/SPIData.h"

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
 * Method:    setSpiAccumulatorValue
 * Signature: (IJ)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_SensorFeedbackJni_setSpiAccumulatorValue
  (JNIEnv *, jclass, jint aHandle, jlong aValue)
{
    HALSIM_SetSPIGetAccumulatorValue(aHandle, aValue);
}


/*
 * Class:     com_snobot_simulator_jni_SensorFeedbackJni
 * Method:    setSpiValueForRead
 * Signature: ([BI)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_SensorFeedbackJni_setSpiValueForRead
  (JNIEnv * env, jclass, jint aHandle, jobject data, jint size)
{
    uint8_t *dataPtr = nullptr;
    if (data != 0) {
        dataPtr = (uint8_t *)env->GetDirectBufferAddress(data);
    }

    HALSIM_SetSPISetValueForRead(aHandle, dataPtr, size);
}

/*
 * Class:     com_snobot_simulator_jni_SensorFeedbackJni
 * Method:    setSpiLastWrite
 * Signature: (ILjava/nio/ByteBuffer;I)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_SensorFeedbackJni_getSpiLastWrite
  (JNIEnv * env, jclass, jint aHandle, jobject data, jint size)
{
    uint8_t *dataPtr = nullptr;
    if (data != 0) {
        dataPtr = (uint8_t *)env->GetDirectBufferAddress(data);
    }

    HALSIM_GetSPIGetWriteBuffer(aHandle, dataPtr, size);
}


/*
 * Class:     com_snobot_simulator_jni_SensorFeedbackJni
 * Method:    setI2CValueForRead
 * Signature: ([BI)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_SensorFeedbackJni_setI2CValueForRead
  (JNIEnv * env, jclass, jint aHandle, jobject data, jint size)
{
    uint8_t *dataPtr = nullptr;
    if (data != 0) {
        dataPtr = (uint8_t *)env->GetDirectBufferAddress(data);
    }

    HALSIM_SetI2CSetValueForRead(aHandle, dataPtr, size);
}

/*
 * Class:     com_snobot_simulator_jni_SensorFeedbackJni
 * Method:    setI2CLastWrite
 * Signature: (ILjava/nio/ByteBuffer;I)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_SensorFeedbackJni_getI2CLastWrite
  (JNIEnv * env, jclass, jint aHandle, jobject data, jint size)
{
    uint8_t *dataPtr = nullptr;
    if (data != 0) {
        dataPtr = (uint8_t *)env->GetDirectBufferAddress(data);
    }

    HALSIM_GetI2CGetWriteBuffer(aHandle, dataPtr, size);
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
 * Method:    waitForProgramToStart
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_SensorFeedbackJni_waitForProgramToStart
  (JNIEnv *, jclass)
{
    HALSIM_WaitForProgramStart();
}

/*
 * Class:     com_snobot_simulator_jni_SensorFeedbackJni
 * Method:    delayForNextUpdateLoop
 * Signature: (D)V
 */
JNIEXPORT void JNICALL Java_com_snobot_simulator_jni_SensorFeedbackJni_delayForNextUpdateLoop
  (JNIEnv *, jclass, jdouble aDelayPeriod)
{
    if(aDelayPeriod > 0)
    {
        std::this_thread::sleep_for(std::chrono::milliseconds((int) (aDelayPeriod * 1000)));
        HALSIM_SetDriverStationMatchTime(HALSIM_GetDriverStationMatchTime() + aDelayPeriod);
        HALSIM_NotifyDriverStationNewData();
    }
    else
    {
        std::cerr << "Unsupported... Cannot delay 0 at the moment" << std::endl;
    }
}

/*
 * Class:     com_snobot_simulator_jni_SensorFeedbackJni
 * Method:    getMatchTime
 * Signature: ()D
 */
JNIEXPORT jdouble JNICALL Java_com_snobot_simulator_jni_SensorFeedbackJni_getMatchTime
  (JNIEnv *, jclass)
{
    return HALSIM_GetDriverStationMatchTime();
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
