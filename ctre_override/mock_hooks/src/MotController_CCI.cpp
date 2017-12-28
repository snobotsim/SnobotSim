

#include "ctre/phoenix/CCI/MotController_CCI.h"
#include "MockHooks.h"

#include <iostream>
#include <string>
#include <vector>


std::vector<SnobotSim::CTRE_CallbackFunc> gMotorControllerCallbacks;

void SnobotSim::SetMotControllerCallback(SnobotSim::CTRE_CallbackFunc callback)
{
	std::cout << "Setting callback " << std::endl;
	gMotorControllerCallbacks.clear();
	gMotorControllerCallbacks.push_back(callback);
}


class MotorControllerWrapper
{
public:

	const int mDeviceId;

	MotorControllerWrapper(int deviceId) :
		mDeviceId(deviceId & 0x3F)
	{
		Send("Create");
	}
	void Send(const std::string& aName, uint8_t* aBuffer)
	{
		std::cout << "Sending..." << std::endl;
		if(!gMotorControllerCallbacks.empty())
		{
			std::cout << "About to call.... " << this << "  *** " << aName.c_str() << ", " << mDeviceId << std::endl;
			gMotorControllerCallbacks[0](aName.c_str(), mDeviceId, aBuffer);
		}
		else
		{
			LOG_UNSUPPORTED_CAN_FUNC("Callback " << aName << " not registered");
		}
	}

	void Receive(const std::string& aName, uint8_t* aBuffer)
	{
		if(!gMotorControllerCallbacks.empty())
		{
			std::cout << "About to call SEND.... " << this << "  *** " << aName.c_str() << ", " << mDeviceId << std::endl;
			gMotorControllerCallbacks[0](aName.c_str(), mDeviceId, aBuffer);
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




MotorControllerWrapper* ConvertToWrapper(void* param)
{
	long handle = *static_cast<long*>(param);

	return (MotorControllerWrapper*) handle;
}


extern "C"{
	void c_Testblah()
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
	}
	void* c_MotController_Create1(int baseArbId)
	{
		MotorControllerWrapper* output = new MotorControllerWrapper(baseArbId);
		return output;
	}

	ErrorCode c_MotController_GetDeviceNumber(void *handle, int *deviceNumber)
	{
		MotorControllerWrapper* wrapper = ConvertToWrapper(handle);
		*deviceNumber = wrapper->mDeviceId;
		return (ErrorCode) 0;
	}
	void c_MotController_SetDemand(void *handle, int mode, int demand0, int demand1)
	{
		MotorControllerWrapper* wrapper = ConvertToWrapper(handle);
		wrapper->Send("SetDemand", mode, demand0, demand1);
	}
	void c_MotController_SetNeutralMode(void *handle, int neutralMode)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
	}
	void c_MotController_SetSensorPhase(void *handle, bool PhaseSensor)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
	}
	void c_MotController_SetInverted(void *handle, bool invert)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
	}
	ErrorCode c_MotController_ConfigOpenLoopRamp(void *handle, float secondsFromNeutralToFull, int timeoutMs)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_ConfigClosedLoopRamp(void *handle, float secondsFromNeutralToFull, int timeoutMs)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_ConfigPeakOutputForward(void *handle, float percentOut, int timeoutMs)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_ConfigPeakOutputReverse(void *handle, float percentOut, int timeoutMs)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_ConfigNominalOutputForward(void *handle, float percentOut, int timeoutMs)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_ConfigNominalOutputReverse(void *handle, float percentOut, int timeoutMs)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_ConfigNeutralDeadband(void *handle, float percentDeadband, int timeoutMs)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_ConfigVoltageCompSaturation(void *handle, float voltage, int timeoutMs)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_ConfigVoltageMeasurementFilter(void *handle, int filterWindowSamples, int timeoutMs)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return (ErrorCode) 0;
	}
	void c_MotController_EnableVoltageCompensation(void *handle, bool enable)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
	}
	ErrorCode c_MotController_GetBusVoltage(void *handle, float *voltage)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_GetMotorOutputPercent(void *handle, float *percentOutput)
	{
		MotorControllerWrapper* wrapper = ConvertToWrapper(handle);
		uint8_t buffer[4];
		wrapper->Receive("GetMotorOutputPercent", buffer);

		std::memcpy(percentOutput, &buffer[0], 4);

		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_GetOutputCurrent(void *handle, float *current)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_GetTemperature(void *handle, float *temperature)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_ConfigRemoteFeedbackFilter(void *handle, int arbId, int peripheralIdx, int reserved, int timeoutMs)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_ConfigSelectedFeedbackSensor(void *handle, int feedbackDevice, int timeoutMs)
	{
		MotorControllerWrapper* wrapper = ConvertToWrapper(handle);
		wrapper->Send("ConfigSelectedFeedbackSensor", feedbackDevice);

		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_GetSelectedSensorPosition(void *handle, int *param)
	{
		MotorControllerWrapper* wrapper = ConvertToWrapper(handle);
		uint8_t buffer[4];
		wrapper->Receive("GetSelectedSensorPosition", buffer);

		std::memcpy(param, &buffer[0], 4);

		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_GetSelectedSensorVelocity(void *handle, int *param)
	{
		MotorControllerWrapper* wrapper = ConvertToWrapper(handle);
		uint8_t buffer[4];
		wrapper->Receive("GetSelectedSensorVelocity", buffer);

		std::memcpy(param, &buffer[0], 4);
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_SetSelectedSensorPosition(void *handle, int sensorPos, int timeoutMs)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_SetControlFramePeriod(void *handle, int frame, int periodMs)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_SetStatusFramePeriod(void *handle, int frame, int periodMs, int timeoutMs)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_GetStatusFramePeriod(void *handle, int frame, int *periodMs, int timeoutMs)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_ConfigVelocityMeasurementPeriod(void *handle, int period, int timeoutMs)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_ConfigVelocityMeasurementWindow(void *handle, int windowSize, int timeoutMs)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_ConfigForwardLimitSwitchSource(void *handle, int type, int normalOpenOrClose, int deviceID, int timeoutMs)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_ConfigReverseLimitSwitchSource(void *handle, int type, int normalOpenOrClose, int deviceID, int timeoutMs)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return (ErrorCode) 0;
	}
	void c_MotController_EnableLimitSwitches(void *handle, bool enable)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
	}
	ErrorCode c_MotController_ConfigForwardSoftLimit(void *handle, int forwardSensorLimit, int timeoutMs)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_ConfigReverseSoftLimit(void *handle, int reverseSensorLimit, int timeoutMs)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return (ErrorCode) 0;
	}
	void c_MotController_EnableSoftLimits(void *handle, bool enable)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
	}
	ErrorCode c_MotController_Config_kP(void *handle, int slotIdx, float value, int timeoutMs)
	{
		MotorControllerWrapper* wrapper = ConvertToWrapper(handle);
		wrapper->Send("Config_kP", slotIdx, value);
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_Config_kI(void *handle, int slotIdx, float value, int timeoutMs)
	{
		MotorControllerWrapper* wrapper = ConvertToWrapper(handle);
		wrapper->Send("Config_kI", slotIdx, value);
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_Config_kD(void *handle, int slotIdx, float value, int timeoutMs)
	{
		MotorControllerWrapper* wrapper = ConvertToWrapper(handle);
		wrapper->Send("Config_kD", slotIdx, value);
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_Config_kF(void *handle, int slotIdx, float value, int timeoutMs)
	{
		MotorControllerWrapper* wrapper = ConvertToWrapper(handle);
		wrapper->Send("Config_kF", slotIdx, value);
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_Config_IntegralZone(void *handle, int slotIdx, float izone, int timeoutMs)
	{
		MotorControllerWrapper* wrapper = ConvertToWrapper(handle);
		wrapper->Send("Config_IntegralZone", slotIdx, izone);
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_ConfigAllowableClosedloopError(void *handle, int slotIdx, int allowableClosedLoopError, int timeoutMs)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_ConfigMaxIntegralAccumulator(void *handle, int slotIdx, float iaccum, int timeoutMs)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_SetIntegralAccumulator(void *handle, float iaccum, int timeoutMs)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_GetClosedLoopError(void *handle, int *closedLoopError, int slotIdx)
	{
		MotorControllerWrapper* wrapper = ConvertToWrapper(handle);
		uint8_t buffer[4];
		wrapper->Receive("GetClosedLoopError", buffer);

		std::memcpy(closedLoopError, &buffer[0], 4);
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_GetIntegralAccumulator(void *handle, float *iaccum, int slotIdx)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_GetErrorDerivative(void *handle, float *derror, int slotIdx)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return (ErrorCode) 0;
	}
	void c_MotController_SelectProfileSlot(void *handle, int slotIdx)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
	}
	ErrorCode c_MotController_ConfigMotionCruiseVelocity(void *handle, int sensorUnitsPer100ms, int timeoutMs)
	{
		MotorControllerWrapper* wrapper = ConvertToWrapper(handle);
		wrapper->Send("ConfigMotionCruiseVelocity", sensorUnitsPer100ms);
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_ConfigMotionAcceleration(void *handle, int sensorUnitsPer100msPerSec, int timeoutMs)
	{
		MotorControllerWrapper* wrapper = ConvertToWrapper(handle);
		wrapper->Send("ConfigMotionAcceleration", sensorUnitsPer100msPerSec);
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_GetLastError(void *handle)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return (ErrorCode) 0;
	}
	int c_MotController_GetFirmwareVersion(void *handle)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return 0;
	}
	bool c_MotController_HasResetOccurred(void *handle)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return false;
	}
	ErrorCode c_MotController_ConfigSetCustomParam(void *handle, int newValue, int paramIndex, int timeoutMs)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_ConfigGetCustomParam(void *handle, int *readValue, int paramIndex, int timoutMs)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_ConfigSetParameter(void *handle, int param, float value, int subValue, int ordinal, int timeoutMs)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_ConfigGetParameter(void *handle, int param, float *value, int ordinal, int timeoutMs)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_ConfigPeakCurrentLimit(void *handle, int amps, int timeoutMs)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_ConfigPeakCurrentDuration(void *handle, int milliseconds, int timeoutMs)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_ConfigContinuousCurrentLimit(void *handle, int amps, int timeoutMs)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return (ErrorCode) 0;
	}
	void c_MotController_EnableCurrentLimit(void *handle, bool enable)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
	}
	ErrorCode c_MotController_SetLastError(void *handle, int error)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return (ErrorCode) 0;
	}
}
