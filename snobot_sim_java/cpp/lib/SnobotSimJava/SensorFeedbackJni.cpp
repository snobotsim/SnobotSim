
#include <assert.h>
#include <jni.h>
#include "support/jni_util.h"

#include "com_snobot_simulator_jni_SensorFeedbackJni.h"

#include "MockData/AnalogInData.h"
#include "MockData/AnalogGyroData.h"
#include "MockData/DIOData.h"
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

}
