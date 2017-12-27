
#include <jni.h>
#include <assert.h>

#include "com_ctre_phoenix_Sensors_PigeonImuJNI.h"
#include "HAL/CAN.h"


#ifdef _WIN32
#include "Winsock2.h"
#pragma comment(lib, "ws2_32.lib")
#else
#include <arpa/inet.h>
#endif

#include <iostream>

#ifndef __FUNCTION_NAME__
    #ifdef WIN32   //WINDOWS
        #define __FUNCTION_NAME__   __FUNCTION__
    #else          //*NIX
        #define __FUNCTION_NAME__   __func__
    #endif
#endif

#define CAN_LOG_UNSUPPORTED(x) std::cerr << __FUNCTION_NAME__ << " Unsupported " << x << std::endl;


class PigeonImuSimulatorWrapper
{
public:
	PigeonImuSimulatorWrapper(int deviceId) :
		mDeviceId(deviceId)
	{
		uint8_t data[8];
		std::memset(&data[0], 0, sizeof(data));

		SendMessage(0x00042800, data);
	}

	void SendMessage(int messageId, uint8_t* data)
	{
		int status = 0;
        HAL_CAN_SendMessage(messageId | mDeviceId, data, 5, 0, &status);
	}

	void ReceiveMessage(int messageId, uint8_t* recvBuffer)
	{
		int32_t status = 0;
		uint8_t len = 0;
		uint32_t timeStamp = 0;
		uint32_t arbId = messageId | mDeviceId;

		HAL_CAN_ReceiveMessage(&arbId, 0xFFFFFFFF, recvBuffer, &len, &timeStamp, &status);
	}

protected:

	int mDeviceId;

};


/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_new_PigeonImu_Talon
 * Signature: (I)J
 */
JNIEXPORT jlong JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1new_1PigeonImu_1Talon
  (JNIEnv *, jclass, jint talonNumber)
{
	PigeonImuSimulatorWrapper* wrapper = new PigeonImuSimulatorWrapper(0x02000000 | talonNumber);

	return (jlong) wrapper;
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_new_PigeonImu
 * Signature: (I)J
 */
JNIEXPORT jlong JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1new_1PigeonImu
  (JNIEnv *, jclass, jint deviceNumber)
{

	PigeonImuSimulatorWrapper* wrapper = new PigeonImuSimulatorWrapper(0x15000000 | deviceNumber);


	return (jlong) wrapper;
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_ConfigSetParameter
 * Signature: (JID)V
 */
JNIEXPORT void JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1ConfigSetParameter
  (JNIEnv *, jclass, jlong, jint, jdouble)
{
	CAN_LOG_UNSUPPORTED("");
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_SetStatusFrameRateMs
 * Signature: (JII)V
 */
JNIEXPORT void JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1SetStatusFrameRateMs
  (JNIEnv *, jclass, jlong, jint, jint)
{
	CAN_LOG_UNSUPPORTED("");
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_SetYaw
 * Signature: (JD)V
 */
JNIEXPORT void JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1SetYaw
  (JNIEnv *, jclass, jlong, jdouble)
{
	CAN_LOG_UNSUPPORTED("");
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_AddYaw
 * Signature: (JD)V
 */
JNIEXPORT void JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1AddYaw
  (JNIEnv *, jclass, jlong, jdouble)
{
	CAN_LOG_UNSUPPORTED("");
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_SetYawToCompass
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1SetYawToCompass
  (JNIEnv *, jclass, jlong)
{
	CAN_LOG_UNSUPPORTED("");
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_SetFusedHeading
 * Signature: (JD)V
 */
JNIEXPORT void JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1SetFusedHeading
  (JNIEnv *, jclass, jlong, jdouble)
{
	CAN_LOG_UNSUPPORTED("");
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_AddFusedHeading
 * Signature: (JD)V
 */
JNIEXPORT void JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1AddFusedHeading
  (JNIEnv *, jclass, jlong, jdouble)
{
	CAN_LOG_UNSUPPORTED("");
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_SetFusedHeadingToCompass
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1SetFusedHeadingToCompass
  (JNIEnv *, jclass, jlong)
{
	CAN_LOG_UNSUPPORTED("");
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_SetAccumZAngle
 * Signature: (JD)V
 */
JNIEXPORT void JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1SetAccumZAngle
  (JNIEnv *, jclass, jlong, jdouble)
{
	CAN_LOG_UNSUPPORTED("");
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_EnableTemperatureCompensation
 * Signature: (JI)V
 */
JNIEXPORT void JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1EnableTemperatureCompensation
  (JNIEnv *, jclass, jlong, jint)
{
	CAN_LOG_UNSUPPORTED("");
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_SetCompassDeclination
 * Signature: (JD)V
 */
JNIEXPORT void JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1SetCompassDeclination
  (JNIEnv *, jclass, jlong, jdouble)
{
	CAN_LOG_UNSUPPORTED("");
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_SetCompassAngle
 * Signature: (JD)V
 */
JNIEXPORT void JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1SetCompassAngle
  (JNIEnv *, jclass, jlong, jdouble)
{
	CAN_LOG_UNSUPPORTED("");
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_EnterCalibrationMode
 * Signature: (JI)V
 */
JNIEXPORT void JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1EnterCalibrationMode
  (JNIEnv *, jclass, jlong, jint)
{
	CAN_LOG_UNSUPPORTED("");
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_GetGeneralStatus
 * Signature: (JLjava/lang/Object;Ljava/lang/Object;)V
 */
JNIEXPORT void JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1GetGeneralStatus
  (JNIEnv *, jclass, jlong, jobject, jobject)
{
	CAN_LOG_UNSUPPORTED("");
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_Get6dQuaternion
 * Signature: (J)[D
 */
JNIEXPORT jdoubleArray JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1Get6dQuaternion
  (JNIEnv *, jclass, jlong)
{
	CAN_LOG_UNSUPPORTED("");

	jdoubleArray output;

	return output;
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_GetYawPitchRoll
 * Signature: (J)[D
 */
JNIEXPORT jdoubleArray JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1GetYawPitchRoll
  (JNIEnv *, jclass, jlong)
{
	CAN_LOG_UNSUPPORTED("");

	jdoubleArray output;

	return output;
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_GetAccumGyro
 * Signature: (J)[D
 */
JNIEXPORT jdoubleArray JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1GetAccumGyro
  (JNIEnv *, jclass, jlong)
{
	CAN_LOG_UNSUPPORTED("");

	jdoubleArray output;

	return output;
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_GetAbsoluteCompassHeading
 * Signature: (J)D
 */
JNIEXPORT jdouble JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1GetAbsoluteCompassHeading
  (JNIEnv *, jclass, jlong)
{
	CAN_LOG_UNSUPPORTED("");

	return 0;
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_GetCompassHeading
 * Signature: (J)D
 */
JNIEXPORT jdouble JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1GetCompassHeading
  (JNIEnv *, jclass, jlong)
{
	CAN_LOG_UNSUPPORTED("");

	return 0;
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_GetCompassFieldStrength
 * Signature: (J)D
 */
JNIEXPORT jdouble JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1GetCompassFieldStrength
  (JNIEnv *, jclass, jlong)
{
	CAN_LOG_UNSUPPORTED("");

	return 0;
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_GetTemp
 * Signature: (J)D
 */
JNIEXPORT jdouble JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1GetTemp
  (JNIEnv *, jclass, jlong)
{
	CAN_LOG_UNSUPPORTED("");

	return 0;
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_GetUpTime
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1GetUpTime
  (JNIEnv *, jclass, jlong)
{
	CAN_LOG_UNSUPPORTED("");

	return 0;
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_GetRawMagnetometer
 * Signature: (J)[S
 */
JNIEXPORT jshortArray JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1GetRawMagnetometer
  (JNIEnv *, jclass, jlong)
{
	CAN_LOG_UNSUPPORTED("");

	jshortArray output;

	return output;
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_GetBiasedMagnetometer
 * Signature: (J)[S
 */
JNIEXPORT jshortArray JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1GetBiasedMagnetometer
  (JNIEnv *, jclass, jlong)
{
	CAN_LOG_UNSUPPORTED("");

	jshortArray output;

	return output;
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_GetBiasedAccelerometer
 * Signature: (J)[S
 */
JNIEXPORT jshortArray JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1GetBiasedAccelerometer
  (JNIEnv *, jclass, jlong)
{
	CAN_LOG_UNSUPPORTED("");

	jshortArray output;

	return output;
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_GetRawGyro
 * Signature: (J)[D
 */
JNIEXPORT jdoubleArray JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1GetRawGyro
  (JNIEnv * env, jclass, jlong aDevicePointer)
{
	PigeonImuSimulatorWrapper* wrapper = (PigeonImuSimulatorWrapper*) aDevicePointer;

	uint8_t buffer[8];
	wrapper->ReceiveMessage(0x00041C40, buffer);

	short yawRaw = 0;
	short pitchRaw = 0;
	short rollRaw = 0;

	std::memcpy(&yawRaw, &buffer[0], sizeof(yawRaw));
	std::memcpy(&pitchRaw, &buffer[2], sizeof(pitchRaw));
	std::memcpy(&rollRaw, &buffer[4], sizeof(rollRaw));

	yawRaw = ntohs(yawRaw);
	pitchRaw = ntohs(pitchRaw);
	rollRaw = ntohs(rollRaw);

	const double LSB =  1.0 / 16.4;

	double yaw = yawRaw * LSB;
	double pitch = pitchRaw * LSB;
	double roll = rollRaw * LSB;

	jdoubleArray result;
	 result = env->NewDoubleArray(3);
	 if (result == NULL) {
	     return NULL;
	 }

	 jdouble fill[3];
	 fill[0] = yaw;
	 fill[1] = pitch;
	 fill[2] = roll;

     env->SetDoubleArrayRegion(result, 0, 3, fill);
	 return result;
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_GetAccelerometerAngles
 * Signature: (J)[D
 */
JNIEXPORT jdoubleArray JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1GetAccelerometerAngles
  (JNIEnv *, jclass, jlong)
{
	CAN_LOG_UNSUPPORTED("");

	jdoubleArray output;

	return output;
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_GetFusedHeading
 * Signature: (J)D
 */
JNIEXPORT jdouble JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1GetFusedHeading__J
  (JNIEnv *, jclass, jlong)
{
	CAN_LOG_UNSUPPORTED("");

	return 0;
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_GetFusedHeading
 * Signature: (JLjava/lang/Object;Ljava/lang/Object;)D
 */
JNIEXPORT jdouble JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1GetFusedHeading__JLjava_lang_Object_2Ljava_lang_Object_2
  (JNIEnv *, jclass, jlong, jobject, jobject)
{
	CAN_LOG_UNSUPPORTED("");

	return 0;
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_GetState
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1GetState
  (JNIEnv *, jclass, jlong)
{
	CAN_LOG_UNSUPPORTED("");

	return 0;
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_GetResetCount
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1GetResetCount
  (JNIEnv *, jclass, jlong)
{
	CAN_LOG_UNSUPPORTED("");

	return 0;
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_GetResetFlags
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1GetResetFlags
  (JNIEnv *, jclass, jlong)
{
	CAN_LOG_UNSUPPORTED("");

	return 0;
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_GetFirmVers
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1GetFirmVers
  (JNIEnv *, jclass, jlong)
{
	CAN_LOG_UNSUPPORTED("");

	return 0;
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    GetLastError
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_GetLastError
  (JNIEnv *, jclass, jlong)
{
	return 0;
}

/*
 * Class:     com_ctre_phoenix_Sensors_PigeonImuJNI
 * Method:    JNI_HasResetOccured
 * Signature: (J)Z
 */
JNIEXPORT jboolean JNICALL Java_com_ctre_phoenix_Sensors_PigeonImuJNI_JNI_1HasResetOccured
  (JNIEnv *, jclass, jlong)
{
	return false;
}
