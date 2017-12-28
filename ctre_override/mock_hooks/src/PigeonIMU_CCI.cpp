
#include "MockHooks.h"
#include "ctre/phoenix/CCI/PigeonIMU_CCI.h"
#include <vector>


#define RECEIVE_HELPER(paramName, size)                             \
	PigeonImuSimulatorWrapper* wrapper = ConvertToWrapper(handle);  \
	uint8_t buffer[size];                                           \
	std::memset(&buffer[0], 0, size);                               \
	wrapper->Receive(paramName, buffer);

std::vector<SnobotSim::CTRE_CallbackFunc> gPigeonCallbacks;

void SnobotSim::SetPigeonCallback(SnobotSim::CTRE_CallbackFunc callback)
{
	gPigeonCallbacks.clear();
	gPigeonCallbacks.push_back(callback);
}

class PigeonImuSimulatorWrapper
{
public:

	const int mDeviceId;

	PigeonImuSimulatorWrapper(int deviceId) :
		mDeviceId(deviceId & 0x3F)
	{
		Send("Create");
	}

	void Send(const std::string& aName, uint8_t* aBuffer)
	{
		if(!gPigeonCallbacks.empty())
		{
			gPigeonCallbacks[0](aName.c_str(), mDeviceId, aBuffer);
		}
		else
		{
			LOG_UNSUPPORTED_CAN_FUNC("Callback " << aName << " not registered");
		}
	}

	void Receive(const std::string& aName, uint8_t* aBuffer)
	{
		if(!gPigeonCallbacks.empty())
		{
			gPigeonCallbacks[0](aName.c_str(), mDeviceId, aBuffer);
		}
		else
		{
			LOG_UNSUPPORTED_CAN_FUNC("Callback " << aName << " not registered");
		}
	}
	void Send(const std::string& aName)
	{
		uint8_t buffer[1];
		Send(aName, buffer);
	}

	template<typename T0>
	void Send(const std::string& aName, T0& param0)
	{
		int size = sizeof(T0);

		uint8_t* buffer = new uint8_t[size];
		std::memset(&buffer[0], 0, size);

		uint32_t offset = 0;
		PushValue(buffer, param0, offset);
		Send(aName, buffer);

		delete buffer;

	}

	template<typename T0, typename T1>
	void Send(const std::string& aName, T0& param0, T1& param1)
	{
		int size = sizeof(T0) + sizeof(T1);

		uint8_t* buffer = new uint8_t[size];
		std::memset(&buffer[0], 0, size);

		uint32_t offset = 0;
		PushValue(buffer, param0, offset);
		PushValue(buffer, param1, offset);
		Send(aName, buffer);

		delete buffer;
	}

	template<typename T0, typename T1, typename T2>
	void Send(const std::string& aName, T0& param0, T1& param1, T2& param2)
	{
		int size = sizeof(T0) + sizeof(T1) + sizeof(T2);

		uint8_t* buffer = new uint8_t[size];
		std::memset(&buffer[0], 0, size);

		uint32_t offset = 0;
		PushValue(buffer, param0, offset);
		PushValue(buffer, param1, offset);
		PushValue(buffer, param2, offset);
		Send(aName, buffer);

		delete buffer;
	}

	template <typename T>
	void PushValue(uint8_t* buffer, T& value, uint32_t& offset)
	{
		std::memcpy(&buffer[offset], &value, sizeof(value));
		offset += sizeof(value);
	}

};


PigeonImuSimulatorWrapper* ConvertToWrapper(void* param)
{
	long handle = *static_cast<long*>(param);

	return (PigeonImuSimulatorWrapper*) handle;
}

 extern "C"{
	void *c_PigeonIMU_Create2(int talonDeviceID)
	{
		PigeonImuSimulatorWrapper* output = new PigeonImuSimulatorWrapper(talonDeviceID);
		return output;
	}
	void *c_PigeonIMU_Create1(int deviceNumber)
	{
		PigeonImuSimulatorWrapper* output = new PigeonImuSimulatorWrapper(deviceNumber);
		return output;
	}
	// void c_PigeonIMU_Destroy(void *handle);
	CTR_Code c_PigeonIMU_ConfigSetParameter(void *handle, int paramEnum, double paramValue)
	{
		PigeonImuSimulatorWrapper* wrapper = ConvertToWrapper(handle);
		wrapper->Send("ConfigSetParameter", paramEnum, paramValue);
		return (CTR_Code) 0;
	}
	CTR_Code c_PigeonIMU_SetStatusFrameRateMs(void *handle, int statusFrameRate, int periodMs)
	{
		PigeonImuSimulatorWrapper* wrapper = ConvertToWrapper(handle);
		wrapper->Send("SetStatusFrameRateMs", statusFrameRate, periodMs);
		return (CTR_Code) 0;
	}
	CTR_Code c_PigeonIMU_SetYaw(void *handle, double angleDeg)
	{
		PigeonImuSimulatorWrapper* wrapper = ConvertToWrapper(handle);
		wrapper->Send("SetYaw", angleDeg);
		return (CTR_Code) 0;
	}
	CTR_Code c_PigeonIMU_AddYaw(void *handle, double angleDeg)
	{
		PigeonImuSimulatorWrapper* wrapper = ConvertToWrapper(handle);
		wrapper->Send("AddYaw", angleDeg);
		return (CTR_Code) 0;
	}
	CTR_Code c_PigeonIMU_SetYawToCompass(void *handle)
	{
		PigeonImuSimulatorWrapper* wrapper = ConvertToWrapper(handle);
		wrapper->Send("SetYawToCompass");
		return (CTR_Code) 0;
	}
	CTR_Code c_PigeonIMU_SetFusedHeading(void *handle, double angleDeg)
	{
		PigeonImuSimulatorWrapper* wrapper = ConvertToWrapper(handle);
		wrapper->Send("SetFusedHeading", angleDeg);
		return (CTR_Code) 0;
	}
	CTR_Code c_PigeonIMU_AddFusedHeading(void *handle, double angleDeg)
	{
		PigeonImuSimulatorWrapper* wrapper = ConvertToWrapper(handle);
		wrapper->Send("AddFusedHeading", angleDeg);
		return (CTR_Code) 0;
	}
	CTR_Code c_PigeonIMU_SetFusedHeadingToCompass(void *handle)
	{
		PigeonImuSimulatorWrapper* wrapper = ConvertToWrapper(handle);
		wrapper->Send("SetFusedHeadingToCompass");
		return (CTR_Code) 0;
	}
	CTR_Code c_PigeonIMU_SetAccumZAngle(void *handle, double angleDeg)
	{
		PigeonImuSimulatorWrapper* wrapper = ConvertToWrapper(handle);
		wrapper->Send("SetAccumZAngle", angleDeg);
		return (CTR_Code) 0;
	}
	CTR_Code c_PigeonIMU_EnableTemperatureCompensation(void *handle, int bTempCompEnable)
	{
		PigeonImuSimulatorWrapper* wrapper = ConvertToWrapper(handle);
		wrapper->Send("EnableTemperatureCompensation", bTempCompEnable);
		return (CTR_Code) 0;
	}
	CTR_Code c_PigeonIMU_SetCompassDeclination(void *handle, double angleDegOffset)
	{
		PigeonImuSimulatorWrapper* wrapper = ConvertToWrapper(handle);
		wrapper->Send("SetCompassDeclination", angleDegOffset);
		return (CTR_Code) 0;
	}
	CTR_Code c_PigeonIMU_SetCompassAngle(void *handle, double angleDeg)
	{
		PigeonImuSimulatorWrapper* wrapper = ConvertToWrapper(handle);
		wrapper->Send("SetCompassAngle", angleDeg);
		return (CTR_Code) 0;
	}
	CTR_Code c_PigeonIMU_EnterCalibrationMode(void *handle, int calMode)
	{
		PigeonImuSimulatorWrapper* wrapper = ConvertToWrapper(handle);
		wrapper->Send("EnterCalibrationMode", calMode);
		return (CTR_Code) 0;
	}
	CTR_Code c_PigeonIMU_GetGeneralStatus(void *handle,
			int *state, int *currentMode, int *calibrationError, int *bCalIsBooting,
			double *tempC, int *upTimeSec, int *noMotionBiasCount, int *tempCompensationCount, int *lastError)
	{
		RECEIVE_HELPER("GetGeneralStatus", sizeof(int) * 8 + sizeof(double));
		std::memcpy(state, 					&buffer[0], sizeof(int));
		std::memcpy(currentMode, 			&buffer[0], sizeof(int));
		std::memcpy(calibrationError, 		&buffer[0], sizeof(int));
		std::memcpy(bCalIsBooting, 			&buffer[0], sizeof(int));
		std::memcpy(tempC, 					&buffer[0], sizeof(double));
		std::memcpy(upTimeSec, 				&buffer[0], sizeof(int));
		std::memcpy(noMotionBiasCount, 		&buffer[0], sizeof(int));
		std::memcpy(tempCompensationCount, 	&buffer[0], sizeof(int));
		std::memcpy(lastError, 				&buffer[0], sizeof(int));
		return (CTR_Code) 0;
	}
	CTR_Code c_PigeonIMU_GetLastError(void *handle)
	{
		return (CTR_Code) 0;
	}
	CTR_Code c_PigeonIMU_Get6dQuaternion(void *handle, double wxyz[4])
	{
		RECEIVE_HELPER("Get6dQuaternion", sizeof(double) * 4);
		std::memcpy(&wxyz[0], &buffer[0], sizeof(double));
		std::memcpy(&wxyz[1], &buffer[8], sizeof(double));
		std::memcpy(&wxyz[2], &buffer[16], sizeof(double));
		std::memcpy(&wxyz[3], &buffer[24], sizeof(double));
		return (ErrorCode) 0;
	}
	CTR_Code c_PigeonIMU_GetYawPitchRoll(void *handle, double ypr[3])
	{
		RECEIVE_HELPER("GetYawPitchRoll", sizeof(double) * 3);
		std::memcpy(&ypr[0], &buffer[0], sizeof(double));
		std::memcpy(&ypr[1], &buffer[8], sizeof(double));
		std::memcpy(&ypr[2], &buffer[16], sizeof(double));
		return NotImplemented;
	}
	CTR_Code c_PigeonIMU_GetAccumGyro(void *handle, double xyz_deg[3])
	{
		RECEIVE_HELPER("GetAccumGyro", sizeof(double) * 3);
		std::memcpy(&xyz_deg[0], &buffer[0], sizeof(double));
		std::memcpy(&xyz_deg[1], &buffer[8], sizeof(double));
		std::memcpy(&xyz_deg[2], &buffer[16], sizeof(double));
		return NotImplemented;
	}
	CTR_Code c_PigeonIMU_GetAbsoluteCompassHeading(void *handle, double *value)
	{
		RECEIVE_HELPER("GetAbsoluteCompassHeading", sizeof(double));
		std::memcpy(value, &buffer[0], 8);
		return (CTR_Code) 0;
	}
	CTR_Code c_PigeonIMU_GetCompassHeading(void *handle, double *value)
	{
		RECEIVE_HELPER("GetCompassHeading", sizeof(double));
		std::memcpy(value, &buffer[0], 8);
		return (CTR_Code) 0;
	}
	CTR_Code c_PigeonIMU_GetCompassFieldStrength(void *handle, double *value)
	{
		RECEIVE_HELPER("GetCompassFieldStrength", sizeof(double));
		std::memcpy(value, &buffer[0], 8);
		return (CTR_Code) 0;
	}
	CTR_Code c_PigeonIMU_GetTemp(void *handle, double *value)
	{
		RECEIVE_HELPER("GetTemp", sizeof(double));
		std::memcpy(value, &buffer[0], 8);
		return (CTR_Code) 0;
	}
	CTR_Code c_PigeonIMU_GetState(void *handle, int *state)
	{
		RECEIVE_HELPER("GetState", sizeof(int));
		std::memcpy(state, &buffer[0], 4);
		return (CTR_Code) 0;
	}
	CTR_Code c_PigeonIMU_GetUpTime(void *handle, int *value)
	{
		RECEIVE_HELPER("GetUpTime", sizeof(int));
		std::memcpy(value, &buffer[0], 4);
		return (CTR_Code) 0;
	}
	CTR_Code c_PigeonIMU_GetRawMagnetometer(void *handle, short rm_xyz[3])
	{
		RECEIVE_HELPER("GetRawMagnetometer", sizeof(short) * 3);
		std::memcpy(&rm_xyz[0], &buffer[0], sizeof(short));
		std::memcpy(&rm_xyz[1], &buffer[2], sizeof(short));
		std::memcpy(&rm_xyz[2], &buffer[4], sizeof(short));
		return (CTR_Code) 0;
	}
	CTR_Code c_PigeonIMU_GetBiasedMagnetometer(void *handle, short bm_xyz[3])
	{
		RECEIVE_HELPER("GetBiasedMagnetometer", sizeof(short) * 3);
		std::memcpy(&bm_xyz[0], &buffer[0], sizeof(short));
		std::memcpy(&bm_xyz[1], &buffer[2], sizeof(short));
		std::memcpy(&bm_xyz[2], &buffer[4], sizeof(short));
		return (CTR_Code) 0;
	}
	CTR_Code c_PigeonIMU_GetBiasedAccelerometer(void *handle, short ba_xyz[3])
	{
		RECEIVE_HELPER("GetBiasedAccelerometer", sizeof(short) * 3);
		std::memcpy(&ba_xyz[0], &buffer[0], sizeof(short));
		std::memcpy(&ba_xyz[1], &buffer[2], sizeof(short));
		std::memcpy(&ba_xyz[2], &buffer[4], sizeof(short));
		return (CTR_Code) 0;
	}
	CTR_Code c_PigeonIMU_GetRawGyro(void *handle, double xyz_dps[3])
	{
		RECEIVE_HELPER("GetRawGyro", sizeof(double) * 3);
		std::memcpy(&xyz_dps[0], &buffer[0], sizeof(double));
		std::memcpy(&xyz_dps[1], &buffer[8], sizeof(double));
		std::memcpy(&xyz_dps[2], &buffer[16], sizeof(double));
		return (ErrorCode) 0;
	}
	CTR_Code c_PigeonIMU_GetAccelerometerAngles(void *handle, double tiltAngles[3])
	{
		RECEIVE_HELPER("GetAccelerometerAngles", sizeof(double) * 3);
		std::memcpy(&tiltAngles[0], &buffer[0], sizeof(double));
		std::memcpy(&tiltAngles[1], &buffer[8], sizeof(double));
		std::memcpy(&tiltAngles[2], &buffer[16], sizeof(double));
		return (ErrorCode) 0;
	}
	CTR_Code c_PigeonIMU_GetFusedHeading2(void *handle, int *bIsFusing, int *bIsValid, double *value, int *lastError)
	{
		RECEIVE_HELPER("GetFusedHeading", sizeof(double));
		std::memcpy(value, &buffer[0], sizeof(double));

		*bIsFusing = 5;
		*bIsValid = 5;
		*lastError = NotImplemented;

		return (ErrorCode) 0;
	}
	CTR_Code c_PigeonIMU_GetFusedHeading1(void *handle, double *value)
	{
		RECEIVE_HELPER("GetFusedHeading", sizeof(double));
		std::memcpy(value, &buffer[0], sizeof(double));
		return (CTR_Code) 0;
	}
	CTR_Code c_PigeonIMU_GetResetCount(void *handle, int *value)
	{
		RECEIVE_HELPER("GetResetCount", sizeof(int));
		std::memcpy(value, &buffer[0], 4);
		return (CTR_Code) 0;
	}
	CTR_Code c_PigeonIMU_GetResetFlags(void *handle, int *value)
	{
		RECEIVE_HELPER("GetResetFlags", sizeof(int));
		std::memcpy(value, &buffer[0], 4);
		return (CTR_Code) 0;
	}
	CTR_Code c_PigeonIMU_GetFirmVers(void *handle, int *value)
	{
		RECEIVE_HELPER("GetFirmVers", sizeof(int));
		std::memcpy(value, &buffer[0], 4);
		return (CTR_Code) 0;
	}
	CTR_Code c_PigeonIMU_HasResetOccured(void *handle, bool *value)
	{
		RECEIVE_HELPER("HasResetOccured", sizeof(bool));
		std::memcpy(value, &buffer[0], 1);
		return (CTR_Code) 0;
	}
	void c_PigeonIMU_SetLastError(void *handle, int value)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
	}
}
