
#include <jni.h>
#include <assert.h>

#include "com_ctre_phoenix_Sensors_PigeonImuJNI.h"
#include "ctre/phoenix/CCI/PigeonIMU_CCI.h"

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_new_PigeonImu_Talon
 * Signature: (I)J
 */
JNIEXPORT jlong JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1new_1PigeonImu_1Talon
  (JNIEnv *, jclass, jint talonId)
{
    return (jlong) c_PigeonIMU_Create2(talonId);
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_new_PigeonImu
 * Signature: (I)J
 */
JNIEXPORT jlong JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1new_1PigeonImu
  (JNIEnv *, jclass, jint pigeonId)
{
    return (jlong) c_PigeonIMU_Create1(pigeonId);
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_ConfigSetParameter
 * Signature: (JID)V
 */
JNIEXPORT void JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1ConfigSetParameter
  (JNIEnv * env, jclass, jlong handle, jint, jdouble)
{
    LOG_UNSUPPORTED_CAN_FUNC("");
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_SetStatusFrameRateMs
 * Signature: (JII)V
 */
JNIEXPORT void JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1SetStatusFrameRateMs
  (JNIEnv * env, jclass, jlong handle, jint, jint)
{
    LOG_UNSUPPORTED_CAN_FUNC("");
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_SetYaw
 * Signature: (JD)V
 */
JNIEXPORT void JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1SetYaw
  (JNIEnv * env, jclass, jlong handle, jdouble)
{
    LOG_UNSUPPORTED_CAN_FUNC("");
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_AddYaw
 * Signature: (JD)V
 */
JNIEXPORT void JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1AddYaw
  (JNIEnv * env, jclass, jlong handle, jdouble)
{
    LOG_UNSUPPORTED_CAN_FUNC("");
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_SetYawToCompass
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1SetYawToCompass
  (JNIEnv * env, jclass, jlong handle)
{
    LOG_UNSUPPORTED_CAN_FUNC("");
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_SetFusedHeading
 * Signature: (JD)V
 */
JNIEXPORT void JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1SetFusedHeading
  (JNIEnv * env, jclass, jlong handle, jdouble)
{
    LOG_UNSUPPORTED_CAN_FUNC("");
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_AddFusedHeading
 * Signature: (JD)V
 */
JNIEXPORT void JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1AddFusedHeading
  (JNIEnv * env, jclass, jlong handle, jdouble)
{
    LOG_UNSUPPORTED_CAN_FUNC("");
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_SetFusedHeadingToCompass
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1SetFusedHeadingToCompass
  (JNIEnv * env, jclass, jlong handle)
{
    LOG_UNSUPPORTED_CAN_FUNC("");
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_SetAccumZAngle
 * Signature: (JD)V
 */
JNIEXPORT void JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1SetAccumZAngle
  (JNIEnv * env, jclass, jlong handle, jdouble)
{
    LOG_UNSUPPORTED_CAN_FUNC("");
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_EnableTemperatureCompensation
 * Signature: (JI)V
 */
JNIEXPORT void JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1EnableTemperatureCompensation
  (JNIEnv * env, jclass, jlong handle, jint)
{
    LOG_UNSUPPORTED_CAN_FUNC("");
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_SetCompassDeclination
 * Signature: (JD)V
 */
JNIEXPORT void JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1SetCompassDeclination
  (JNIEnv * env, jclass, jlong handle, jdouble)
{
    LOG_UNSUPPORTED_CAN_FUNC("");
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_SetCompassAngle
 * Signature: (JD)V
 */
JNIEXPORT void JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1SetCompassAngle
  (JNIEnv * env, jclass, jlong handle, jdouble)
{
    LOG_UNSUPPORTED_CAN_FUNC("");
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_EnterCalibrationMode
 * Signature: (JI)V
 */
JNIEXPORT void JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1EnterCalibrationMode
  (JNIEnv * env, jclass, jlong handle, jint)
{
    LOG_UNSUPPORTED_CAN_FUNC("");
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_GetGeneralStatus
 * Signature: (JLjava/lang/Object;Ljava/lang/Object;)V
 */
JNIEXPORT void JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1GetGeneralStatus
  (JNIEnv * env, jclass, jlong handle, jobject, jobject)
{
    LOG_UNSUPPORTED_CAN_FUNC("");
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_Get6dQuaternion
 * Signature: (J)[D
 */
JNIEXPORT jdoubleArray JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1Get6dQuaternion
  (JNIEnv * env, jclass, jlong handle)
{
    LOG_UNSUPPORTED_CAN_FUNC("");

    jdoubleArray output;
    return output;
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_GetYawPitchRoll
 * Signature: (J)[D
 */
JNIEXPORT jdoubleArray JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1GetYawPitchRoll
  (JNIEnv * env, jclass, jlong handle)
{
    LOG_UNSUPPORTED_CAN_FUNC("");

    jdoubleArray output;
    return output;
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_GetAccumGyro
 * Signature: (J)[D
 */
JNIEXPORT jdoubleArray JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1GetAccumGyro
  (JNIEnv * env, jclass, jlong handle)
{
    LOG_UNSUPPORTED_CAN_FUNC("");

    jdoubleArray output;
    return output;
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_GetAbsoluteCompassHeading
 * Signature: (J)D
 */
JNIEXPORT jdouble JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1GetAbsoluteCompassHeading
  (JNIEnv * env, jclass, jlong handle)
{
    LOG_UNSUPPORTED_CAN_FUNC("");
    return 0;
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_GetCompassHeading
 * Signature: (J)D
 */
JNIEXPORT jdouble JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1GetCompassHeading
  (JNIEnv * env, jclass, jlong handle)
{
    LOG_UNSUPPORTED_CAN_FUNC("");
    return 0;
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_GetCompassFieldStrength
 * Signature: (J)D
 */
JNIEXPORT jdouble JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1GetCompassFieldStrength
  (JNIEnv * env, jclass, jlong handle)
{
    LOG_UNSUPPORTED_CAN_FUNC("");
    return 0;
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_GetTemp
 * Signature: (J)D
 */
JNIEXPORT jdouble JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1GetTemp
  (JNIEnv * env, jclass, jlong handle)
{
    LOG_UNSUPPORTED_CAN_FUNC("");
    return 0;
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_GetUpTime
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1GetUpTime
  (JNIEnv * env, jclass, jlong handle)
{
    LOG_UNSUPPORTED_CAN_FUNC("");
    return 0;
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_GetRawMagnetometer
 * Signature: (J)[S
 */
JNIEXPORT jshortArray JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1GetRawMagnetometer
  (JNIEnv * env, jclass, jlong handle)
{
    LOG_UNSUPPORTED_CAN_FUNC("");

    jshortArray output;
    return output;
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_GetBiasedMagnetometer
 * Signature: (J)[S
 */
JNIEXPORT jshortArray JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1GetBiasedMagnetometer
  (JNIEnv * env, jclass, jlong handle)
{
    LOG_UNSUPPORTED_CAN_FUNC("");

    jshortArray output;
    return output;
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_GetBiasedAccelerometer
 * Signature: (J)[S
 */
JNIEXPORT jshortArray JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1GetBiasedAccelerometer
  (JNIEnv * env, jclass, jlong handle)
{
    LOG_UNSUPPORTED_CAN_FUNC("");

    jshortArray output;
    return output;
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_GetRawGyro
 * Signature: (J)[D
 */
JNIEXPORT jdoubleArray JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1GetRawGyro
  (JNIEnv * env, jclass, jlong handle)
{
    double angles[3];
    c_PigeonIMU_GetRawGyro(&handle, angles);

    jdoubleArray result;
    result = env->NewDoubleArray(3);
    if (result == NULL) {
        return NULL;
    }

    jdouble fill[3];
    for(int i = 0; i < 3; ++i)
    {
        fill[i] = angles[i];
    }

    env->SetDoubleArrayRegion(result, 0, 3, fill);
    return result;
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_GetAccelerometerAngles
 * Signature: (J)[D
 */
JNIEXPORT jdoubleArray JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1GetAccelerometerAngles
  (JNIEnv * env, jclass, jlong handle)
{
    LOG_UNSUPPORTED_CAN_FUNC("");

    jdoubleArray output;
    return output;
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_GetFusedHeading
 * Signature: (J)D
 */
JNIEXPORT jdouble JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1GetFusedHeading__J
  (JNIEnv * env, jclass, jlong handle)
{
    LOG_UNSUPPORTED_CAN_FUNC("");
    return 0;
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_GetFusedHeading
 * Signature: (JLjava/lang/Object;Ljava/lang/Object;)D
 */
JNIEXPORT jdouble JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1GetFusedHeading__JLjava_lang_Object_2Ljava_lang_Object_2
  (JNIEnv * env, jclass, jlong handle, jobject, jobject)
{
    LOG_UNSUPPORTED_CAN_FUNC("");
    return 0;
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_GetState
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1GetState
  (JNIEnv * env, jclass, jlong handle)
{
    LOG_UNSUPPORTED_CAN_FUNC("");
    return 0;
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_GetResetCount
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1GetResetCount
  (JNIEnv * env, jclass, jlong handle)
{
    LOG_UNSUPPORTED_CAN_FUNC("");
    return 0;
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_GetResetFlags
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1GetResetFlags
  (JNIEnv * env, jclass, jlong handle)
{
    LOG_UNSUPPORTED_CAN_FUNC("");
    return 0;
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_GetFirmVers
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1GetFirmVers
  (JNIEnv * env, jclass, jlong handle)
{
    LOG_UNSUPPORTED_CAN_FUNC("");
    return 0;
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    GetLastError
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_GetLastError
  (JNIEnv * env, jclass, jlong handle)
{
    LOG_UNSUPPORTED_CAN_FUNC("");
    return 0;
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_HasResetOccured
 * Signature: (J)Z
 */
JNIEXPORT jboolean JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1HasResetOccured
  (JNIEnv * env, jclass, jlong handle)
{
    LOG_UNSUPPORTED_CAN_FUNC("");
    return false;
}
