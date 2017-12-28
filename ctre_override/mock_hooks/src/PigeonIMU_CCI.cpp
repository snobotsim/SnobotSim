
#include "MockHooks.h"
#include "ctre/phoenix/CCI/PigeonIMU_CCI.h"
#include <vector>


std::vector<SnobotSim::CTRE_CallbackFunc> gPigeonCallbacks;

void SnobotSim::SetPigeonCallback(SnobotSim::CTRE_CallbackFunc callback)
{
	std::cout << "Setting pigeon callback " << std::endl;
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
		std::cout << "Sending..." << std::endl;
		if(!gPigeonCallbacks.empty())
		{
			std::cout << "About to call.... " << this << "  *** " << aName.c_str() << ", " << mDeviceId << std::endl;
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
			std::cout << "About to call SEND.... " << this << "  *** " << aName.c_str() << ", " << mDeviceId << std::endl;
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
		LOG_UNSUPPORTED_CAN_FUNC("");
		return NotImplemented;
	}
	CTR_Code c_PigeonIMU_SetStatusFrameRateMs(void *handle, int statusFrameRate, int periodMs)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return NotImplemented;
	}
	CTR_Code c_PigeonIMU_SetYaw(void *handle, double angleDeg)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return NotImplemented;
	}
	CTR_Code c_PigeonIMU_AddYaw(void *handle, double angleDeg)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return NotImplemented;
	}
	CTR_Code c_PigeonIMU_SetYawToCompass(void *handle)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return NotImplemented;
	}
	CTR_Code c_PigeonIMU_SetFusedHeading(void *handle, double angleDeg)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return NotImplemented;
	}
	CTR_Code c_PigeonIMU_AddFusedHeading(void *handle, double angleDeg)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return NotImplemented;
	}
	CTR_Code c_PigeonIMU_SetFusedHeadingToCompass(void *handle)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return NotImplemented;
	}
	CTR_Code c_PigeonIMU_SetAccumZAngle(void *handle, double angleDeg)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return NotImplemented;
	}
	CTR_Code c_PigeonIMU_EnableTemperatureCompensation(void *handle, int bTempCompEnable)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return NotImplemented;
	}
	CTR_Code c_PigeonIMU_SetCompassDeclination(void *handle, double angleDegOffset)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return NotImplemented;
	}
	CTR_Code c_PigeonIMU_SetCompassAngle(void *handle, double angleDeg)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return NotImplemented;
	}
	CTR_Code c_PigeonIMU_EnterCalibrationMode(void *handle, int calMode)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return NotImplemented;
	}
	CTR_Code c_PigeonIMU_GetGeneralStatus(void *handle, int *state, int *currentMode, int *calibrationError, int *bCalIsBooting, double *tempC, int *upTimeSec, int *noMotionBiasCount, int *tempCompensationCount, int *lastError)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return NotImplemented;
	}
	CTR_Code c_PigeonIMU_GetLastError(void *handle)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return NotImplemented;
	}
	CTR_Code c_PigeonIMU_Get6dQuaternion(void *handle, double wxyz[4])
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return NotImplemented;
	}
	CTR_Code c_PigeonIMU_GetYawPitchRoll(void *handle, double ypr[3])
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return NotImplemented;
	}
	CTR_Code c_PigeonIMU_GetAccumGyro(void *handle, double xyz_deg[3])
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return NotImplemented;
	}
	CTR_Code c_PigeonIMU_GetAbsoluteCompassHeading(void *handle, double *value)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return NotImplemented;
	}
	CTR_Code c_PigeonIMU_GetCompassHeading(void *handle, double *value)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return NotImplemented;
	}
	CTR_Code c_PigeonIMU_GetCompassFieldStrength(void *handle, double *value)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return NotImplemented;
	}
	CTR_Code c_PigeonIMU_GetTemp(void *handle, double *value)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return NotImplemented;
	}
	CTR_Code c_PigeonIMU_GetState(void *handle, int *state)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return NotImplemented;
	}
	CTR_Code c_PigeonIMU_GetUpTime(void *handle, int *value)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return NotImplemented;
	}
	CTR_Code c_PigeonIMU_GetRawMagnetometer(void *handle, short rm_xyz[3])
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return NotImplemented;
	}
	CTR_Code c_PigeonIMU_GetBiasedMagnetometer(void *handle, short bm_xyz[3])
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return NotImplemented;
	}
	CTR_Code c_PigeonIMU_GetBiasedAccelerometer(void *handle, short ba_xyz[3])
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return NotImplemented;
	}
	CTR_Code c_PigeonIMU_GetRawGyro(void *handle, double xyz_dps[3])
	{
		PigeonImuSimulatorWrapper* wrapper = ConvertToWrapper(handle);
		uint8_t buffer[sizeof(double) * 3];
		wrapper->Receive("GetRawGyro", buffer);

		std::cout << "SIZE: " << (sizeof(double) * 3) << std::endl;
		std::memcpy(&xyz_dps[0], &buffer[0], sizeof(double));
		std::memcpy(&xyz_dps[1], &buffer[8], sizeof(double));
		std::memcpy(&xyz_dps[2], &buffer[16], sizeof(double));

		return (ErrorCode) 0;
	}
	CTR_Code c_PigeonIMU_GetAccelerometerAngles(void *handle, double tiltAngles[3])
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return NotImplemented;
	}
	CTR_Code c_PigeonIMU_GetFusedHeading2(void *handle, int *bIsFusing, int *bIsValid, double *value, int *lastError)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return NotImplemented;
	}
	CTR_Code c_PigeonIMU_GetFusedHeading1(void *handle, double *value)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return NotImplemented;
	}
	CTR_Code c_PigeonIMU_GetResetCount(void *handle, int *value)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return NotImplemented;
	}
	CTR_Code c_PigeonIMU_GetResetFlags(void *handle, int *value)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return NotImplemented;
	}
	CTR_Code c_PigeonIMU_GetFirmVers(void *handle, int *value)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return NotImplemented;
	}
	CTR_Code c_PigeonIMU_HasResetOccured(void *handle, bool *value)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return NotImplemented;
	}
	void c_PigeonIMU_SetLastError(void *handle, int value)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
	}
}
