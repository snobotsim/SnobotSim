
#include <jni.h>
#include <assert.h>

#include "com_ctre_phoenix_Sensors_PigeonImuJNI.h"
#include "ctre/phoenix/CCI/PigeonIMU_CCI.h"

#define GET_THREE_AXIS(type, capType, funcName, size)    \
                                                      \
   type angles[size];                                    \
   funcName(&handle, angles);                         \
                                                      \
   j##type##Array result;                             \
   result = env->New##capType##Array(size);              \
   if (result == NULL) {                              \
       return NULL;                                   \
   }                                                  \
                                                      \
   j##type fill[size];                                   \
   for(int i = 0; i < size; ++i)                         \
   {                                                  \
       fill[i] = angles[i];                           \
   }                                                  \
                                                      \
   env->Set##capType##ArrayRegion(result, 0, size, fill);     \
   return result;                                     \


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
  (JNIEnv * env, jclass, jlong handle, jint paramEnum, jdouble paramValue)
{
	c_PigeonIMU_ConfigSetParameter(&handle, paramEnum, paramValue);
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_SetStatusFrameRateMs
 * Signature: (JII)V
 */
JNIEXPORT void JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1SetStatusFrameRateMs
  (JNIEnv * env, jclass, jlong handle, jint statusRate, jint periodMs)
{
	c_PigeonIMU_SetStatusFrameRateMs(&handle, statusRate, periodMs);
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_SetYaw
 * Signature: (JD)V
 */
JNIEXPORT void JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1SetYaw
  (JNIEnv * env, jclass, jlong handle, jdouble value)
{
	c_PigeonIMU_SetYaw(&handle, value);
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_AddYaw
 * Signature: (JD)V
 */
JNIEXPORT void JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1AddYaw
  (JNIEnv * env, jclass, jlong handle, jdouble value)
{
	c_PigeonIMU_AddYaw(&handle, value);
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_SetYawToCompass
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1SetYawToCompass
  (JNIEnv * env, jclass, jlong handle)
{
	c_PigeonIMU_SetYawToCompass(&handle);
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_SetFusedHeading
 * Signature: (JD)V
 */
JNIEXPORT void JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1SetFusedHeading
  (JNIEnv * env, jclass, jlong handle, jdouble angleDeg)
{
	c_PigeonIMU_SetFusedHeading(&handle, angleDeg);
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_AddFusedHeading
 * Signature: (JD)V
 */
JNIEXPORT void JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1AddFusedHeading
  (JNIEnv * env, jclass, jlong handle, jdouble angleDeg)
{
	c_PigeonIMU_AddFusedHeading(&handle, angleDeg);
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_SetFusedHeadingToCompass
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1SetFusedHeadingToCompass
  (JNIEnv * env, jclass, jlong handle)
{
	c_PigeonIMU_SetFusedHeadingToCompass(&handle);
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_SetAccumZAngle
 * Signature: (JD)V
 */
JNIEXPORT void JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1SetAccumZAngle
  (JNIEnv * env, jclass, jlong handle, jdouble angleDeg)
{
	c_PigeonIMU_SetAccumZAngle(&handle, angleDeg);
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_EnableTemperatureCompensation
 * Signature: (JI)V
 */
JNIEXPORT void JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1EnableTemperatureCompensation
  (JNIEnv * env, jclass, jlong handle, jint value)
{
	c_PigeonIMU_EnableTemperatureCompensation(&handle, value);
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_SetCompassDeclination
 * Signature: (JD)V
 */
JNIEXPORT void JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1SetCompassDeclination
  (JNIEnv * env, jclass, jlong handle, jdouble angleDegOffset)
{
	c_PigeonIMU_SetCompassDeclination(&handle, angleDegOffset);
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_SetCompassAngle
 * Signature: (JD)V
 */
JNIEXPORT void JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1SetCompassAngle
  (JNIEnv * env, jclass, jlong handle, jdouble angleDeg)
{
	c_PigeonIMU_SetCompassAngle(&handle, angleDeg);
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_EnterCalibrationMode
 * Signature: (JI)V
 */
JNIEXPORT void JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1EnterCalibrationMode
  (JNIEnv * env, jclass, jlong handle, jint value)
{
	c_PigeonIMU_EnterCalibrationMode(&handle, value);
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_GetGeneralStatus
 * Signature: (JLjava/lang/Object;Ljava/lang/Object;)V
 */
JNIEXPORT void JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1GetGeneralStatus
  (JNIEnv * env, jclass, jlong handle, jobject, jobject)
{
	int state = 0;
	int currentMode = 0;
	int calibrationError = 0;
	int bCalIsBooting = 0;
	double tempC = 0;
	int upTimeSec = 0;
	int noMotionBiasCount = 0;
	int tempCompensationCount = 0;
	int lastError = 0;

	c_PigeonIMU_GetGeneralStatus(&handle, &state, &currentMode, &calibrationError, &bCalIsBooting,
			&tempC, &upTimeSec, &noMotionBiasCount, &tempCompensationCount, &lastError);
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_Get6dQuaternion
 * Signature: (J)[D
 */
JNIEXPORT jdoubleArray JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1Get6dQuaternion
  (JNIEnv * env, jclass, jlong handle)
{
	GET_THREE_AXIS(double, Double, c_PigeonIMU_Get6dQuaternion, 4);
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_GetYawPitchRoll
 * Signature: (J)[D
 */
JNIEXPORT jdoubleArray JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1GetYawPitchRoll
  (JNIEnv * env, jclass, jlong handle)
{
	GET_THREE_AXIS(double, Double, c_PigeonIMU_GetYawPitchRoll, 3);
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_GetAccumGyro
 * Signature: (J)[D
 */
JNIEXPORT jdoubleArray JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1GetAccumGyro
  (JNIEnv * env, jclass, jlong handle)
{
	GET_THREE_AXIS(double, Double, c_PigeonIMU_GetAccumGyro, 3);
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_GetAbsoluteCompassHeading
 * Signature: (J)D
 */
JNIEXPORT jdouble JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1GetAbsoluteCompassHeading
  (JNIEnv * env, jclass, jlong handle)
{
	double output = 0;
	c_PigeonIMU_GetAbsoluteCompassHeading(&handle, &output);
    return output;
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_GetCompassHeading
 * Signature: (J)D
 */
JNIEXPORT jdouble JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1GetCompassHeading
  (JNIEnv * env, jclass, jlong handle)
{
	double output = 0;
	c_PigeonIMU_GetCompassHeading(&handle, &output);
    return output;
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_GetCompassFieldStrength
 * Signature: (J)D
 */
JNIEXPORT jdouble JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1GetCompassFieldStrength
  (JNIEnv * env, jclass, jlong handle)
{
	double output = 0;
	c_PigeonIMU_GetCompassFieldStrength(&handle, &output);
    return output;
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_GetTemp
 * Signature: (J)D
 */
JNIEXPORT jdouble JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1GetTemp
  (JNIEnv * env, jclass, jlong handle)
{
	double output = 0;
	c_PigeonIMU_GetTemp(&handle, &output);
    return output;
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_GetUpTime
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1GetUpTime
  (JNIEnv * env, jclass, jlong handle)
{
	int output = 0;
	c_PigeonIMU_GetUpTime(&handle, &output);
    return output;
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_GetRawMagnetometer
 * Signature: (J)[S
 */
JNIEXPORT jshortArray JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1GetRawMagnetometer
  (JNIEnv * env, jclass, jlong handle)
{
	GET_THREE_AXIS(short, Short, c_PigeonIMU_GetRawMagnetometer, 3);
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_GetBiasedMagnetometer
 * Signature: (J)[S
 */
JNIEXPORT jshortArray JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1GetBiasedMagnetometer
  (JNIEnv * env, jclass, jlong handle)
{
	GET_THREE_AXIS(short, Short, c_PigeonIMU_GetBiasedMagnetometer, 3);
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_GetBiasedAccelerometer
 * Signature: (J)[S
 */
JNIEXPORT jshortArray JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1GetBiasedAccelerometer
  (JNIEnv * env, jclass, jlong handle)
{
	GET_THREE_AXIS(short, Short, c_PigeonIMU_GetBiasedAccelerometer, 3);
}


/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_GetRawGyro
 * Signature: (J)[D
 */
JNIEXPORT jdoubleArray JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1GetRawGyro
  (JNIEnv * env, jclass, jlong handle)
{
	GET_THREE_AXIS(double, Double, c_PigeonIMU_GetRawGyro, 3);
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_GetAccelerometerAngles
 * Signature: (J)[D
 */
JNIEXPORT jdoubleArray JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1GetAccelerometerAngles
  (JNIEnv * env, jclass, jlong handle)
{
	GET_THREE_AXIS(double, Double, c_PigeonIMU_GetAccelerometerAngles, 3);
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_GetFusedHeading
 * Signature: (J)D
 */
JNIEXPORT jdouble JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1GetFusedHeading__J
  (JNIEnv * env, jclass, jlong handle)
{
	double value = 0;
	c_PigeonIMU_GetFusedHeading1(&handle, &value);
    return value;
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_GetFusedHeading
 * Signature: (JLjava/lang/Object;Ljava/lang/Object;)D
 */
JNIEXPORT jdouble JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1GetFusedHeading__JLjava_lang_Object_2Ljava_lang_Object_2
  (JNIEnv * env, jclass, jlong handle, jobject pigeonImuObj, jobject fusionStatusObj)
{
	int bIsFusing = 0;
	int bIsValid = 0;
	double value = 0;
	int error = 0;

	c_PigeonIMU_GetFusedHeading2(&handle, &bIsFusing, &bIsValid, &value, &error);

	env->SetBooleanField(fusionStatusObj, env->GetFieldID(env->GetObjectClass(fusionStatusObj), "bIsFusing", "Z"), (bool) bIsFusing);
	env->SetBooleanField(fusionStatusObj, env->GetFieldID(env->GetObjectClass(fusionStatusObj), "bIsValid", "Z"), (bool) bIsValid);
	env->SetDoubleField(fusionStatusObj, env->GetFieldID(env->GetObjectClass(fusionStatusObj), "heading", "D"), value);
	env->SetIntField(fusionStatusObj, env->GetFieldID(env->GetObjectClass(fusionStatusObj), "lastError", "I"), error);

    return value;
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_GetState
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1GetState
  (JNIEnv * env, jclass, jlong handle)
{
	int output = 0;
	c_PigeonIMU_GetState(&handle, &output);
    return output;
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_GetResetCount
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1GetResetCount
  (JNIEnv * env, jclass, jlong handle)
{
	int output = 0;
	c_PigeonIMU_GetResetCount(&handle, &output);
    return output;
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_GetResetFlags
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1GetResetFlags
  (JNIEnv * env, jclass, jlong handle)
{
	int output = 0;
	c_PigeonIMU_GetResetFlags(&handle, &output);
    return output;
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_GetFirmVers
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1GetFirmVers
  (JNIEnv * env, jclass, jlong handle)
{
	int output = 0;
	c_PigeonIMU_GetFirmVers(&handle, &output);
    return output;
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    GetLastError
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_GetLastError
  (JNIEnv * env, jclass, jlong handle)
{
    return (jint) c_PigeonIMU_GetLastError(&handle);
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_HasResetOccured
 * Signature: (J)Z
 */
JNIEXPORT jboolean JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1HasResetOccured
  (JNIEnv * env, jclass, jlong handle)
{
	bool output = 0;
	c_PigeonIMU_HasResetOccured(&handle, &output);
    return output;
}
