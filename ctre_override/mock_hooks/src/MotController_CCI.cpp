

#include "ctre/phoenix/CCI/MotController_CCI.h"
#include "MockHooks.h"

#include <iostream>
#include <string>
#include <vector>

#define RECEIVE_HELPER(paramName, size)                          \
	MotorControllerWrapper* wrapper = ConvertToWrapper(handle);  \
	uint8_t buffer[size];                                        \
	std::memset(&buffer[0], 0, size);                            \
	wrapper->Receive(paramName, buffer);


std::vector<SnobotSim::CTRE_CallbackFunc> gMotorControllerCallbacks;

void SnobotSim::SetMotControllerCallback(SnobotSim::CTRE_CallbackFunc callback)
{
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
		if(!gMotorControllerCallbacks.empty())
		{
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

	template<typename T0, typename T1, typename T2, typename T3>
	void Send(const std::string& aName, T0& param0, T1& param1, T2& param2, T3& param3)
	{
		int size = sizeof(T0) + sizeof(T1) + sizeof(T2) + sizeof(T3);

		uint8_t* buffer = new uint8_t[size];
		std::memset(&buffer[0], 0, size);

		uint32_t offset = 0;
		PushValue(buffer, param0, offset);
		PushValue(buffer, param1, offset);
		PushValue(buffer, param2, offset);
		PushValue(buffer, param3, offset);
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
		MotorControllerWrapper* wrapper = ConvertToWrapper(handle);
		wrapper->Send("SetNeutralMode", neutralMode);
	}
	void c_MotController_SetSensorPhase(void *handle, bool PhaseSensor)
	{
		MotorControllerWrapper* wrapper = ConvertToWrapper(handle);
		wrapper->Send("SetSensorPhase", PhaseSensor);
	}
	void c_MotController_SetInverted(void *handle, bool invert)
	{
		MotorControllerWrapper* wrapper = ConvertToWrapper(handle);
		wrapper->Send("SetInverted", invert);
	}
	ErrorCode c_MotController_ConfigOpenLoopRamp(void *handle, float secondsFromNeutralToFull, int timeoutMs)
	{
		MotorControllerWrapper* wrapper = ConvertToWrapper(handle);
		wrapper->Send("ConfigOpenLoopRamp", secondsFromNeutralToFull);
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_ConfigClosedLoopRamp(void *handle, float secondsFromNeutralToFull, int timeoutMs)
	{
		MotorControllerWrapper* wrapper = ConvertToWrapper(handle);
		wrapper->Send("ConfigClosedLoopRamp", secondsFromNeutralToFull);
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_ConfigPeakOutputForward(void *handle, float percentOut, int timeoutMs)
	{
		MotorControllerWrapper* wrapper = ConvertToWrapper(handle);
		wrapper->Send("ConfigPeakOutputForward", percentOut);
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_ConfigPeakOutputReverse(void *handle, float percentOut, int timeoutMs)
	{
		MotorControllerWrapper* wrapper = ConvertToWrapper(handle);
		wrapper->Send("ConfigPeakOutputReverse", percentOut);
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_ConfigNominalOutputForward(void *handle, float percentOut, int timeoutMs)
	{
		MotorControllerWrapper* wrapper = ConvertToWrapper(handle);
		wrapper->Send("ConfigNominalOutputForward", percentOut);
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_ConfigNominalOutputReverse(void *handle, float percentOut, int timeoutMs)
	{
		MotorControllerWrapper* wrapper = ConvertToWrapper(handle);
		wrapper->Send("ConfigNominalOutputReverse", percentOut);
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_ConfigNeutralDeadband(void *handle, float percentDeadband, int timeoutMs)
	{
		MotorControllerWrapper* wrapper = ConvertToWrapper(handle);
		wrapper->Send("ConfigNeutralDeadband", percentDeadband);
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_ConfigVoltageCompSaturation(void *handle, float voltage, int timeoutMs)
	{
		MotorControllerWrapper* wrapper = ConvertToWrapper(handle);
		wrapper->Send("ConfigVoltageCompSaturation", voltage);
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_ConfigVoltageMeasurementFilter(void *handle, int filterWindowSamples, int timeoutMs)
	{
		MotorControllerWrapper* wrapper = ConvertToWrapper(handle);
		wrapper->Send("ConfigVoltageMeasurementFilter", filterWindowSamples);
		return (ErrorCode) 0;
	}
	void c_MotController_EnableVoltageCompensation(void *handle, bool enable)
	{
		MotorControllerWrapper* wrapper = ConvertToWrapper(handle);
		wrapper->Send("EnableVoltageCompensation", enable);
	}
	ErrorCode c_MotController_GetBusVoltage(void *handle, float *voltage)
	{
		RECEIVE_HELPER("GetBusVoltage", 4);
		std::memcpy(voltage, &buffer[0], 4);
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_GetMotorOutputPercent(void *handle, float *percentOutput)
	{
		RECEIVE_HELPER("GetMotorOutputPercent", 4);
		std::memcpy(percentOutput, &buffer[0], 4);
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_GetOutputCurrent(void *handle, float *current)
	{
		RECEIVE_HELPER("GetOutputCurrent", 4);
		std::memcpy(current, &buffer[0], 4);
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_GetTemperature(void *handle, float *temperature)
	{
		RECEIVE_HELPER("GetTemperature", 4);
		std::memcpy(temperature, &buffer[0], 4);
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_ConfigRemoteFeedbackFilter(void *handle, int arbId, int peripheralIdx, int reserved, int timeoutMs)
	{
		MotorControllerWrapper* wrapper = ConvertToWrapper(handle);
		wrapper->Send("ConfigRemoteFeedbackFilter", arbId, peripheralIdx, reserved);
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
		RECEIVE_HELPER("GetSelectedSensorPosition", 4);
		std::memcpy(param, &buffer[0], 4);
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_GetSelectedSensorVelocity(void *handle, int *param)
	{
		RECEIVE_HELPER("GetSelectedSensorVelocity", 4);
		std::memcpy(param, &buffer[0], 4);
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_SetSelectedSensorPosition(void *handle, int sensorPos, int timeoutMs)
	{
		MotorControllerWrapper* wrapper = ConvertToWrapper(handle);
		wrapper->Send("SetSelectedSensorPosition", sensorPos);
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_SetControlFramePeriod(void *handle, int frame, int periodMs)
	{
		MotorControllerWrapper* wrapper = ConvertToWrapper(handle);
		wrapper->Send("SetControlFramePeriod", frame);
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_SetStatusFramePeriod(void *handle, int frame, int periodMs, int timeoutMs)
	{
		MotorControllerWrapper* wrapper = ConvertToWrapper(handle);
		wrapper->Send("SetStatusFramePeriod", frame, periodMs);
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_GetStatusFramePeriod(void *handle, int frame, int *periodMs, int timeoutMs)
	{
		RECEIVE_HELPER("GetStatusFramePeriod", 4);
		std::memcpy(periodMs, &buffer[0], 4);
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_ConfigVelocityMeasurementPeriod(void *handle, int period, int timeoutMs)
	{
		MotorControllerWrapper* wrapper = ConvertToWrapper(handle);
		wrapper->Send("ConfigVelocityMeasurementPeriod", period);
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_ConfigVelocityMeasurementWindow(void *handle, int windowSize, int timeoutMs)
	{
		MotorControllerWrapper* wrapper = ConvertToWrapper(handle);
		wrapper->Send("ConfigVelocityMeasurementWindow", windowSize);
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_ConfigForwardLimitSwitchSource(void *handle, int type, int normalOpenOrClose, int deviceID, int timeoutMs)
	{
		MotorControllerWrapper* wrapper = ConvertToWrapper(handle);
		wrapper->Send("ConfigForwardLimitSwitchSource", type, normalOpenOrClose, deviceID);
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_ConfigReverseLimitSwitchSource(void *handle, int type, int normalOpenOrClose, int deviceID, int timeoutMs)
	{
		MotorControllerWrapper* wrapper = ConvertToWrapper(handle);
		wrapper->Send("ConfigReverseLimitSwitchSource", type, normalOpenOrClose, deviceID);
		return (ErrorCode) 0;
	}
	void c_MotController_EnableLimitSwitches(void *handle, bool enable)
	{
		MotorControllerWrapper* wrapper = ConvertToWrapper(handle);
		wrapper->Send("EnableLimitSwitches", enable);
	}
	ErrorCode c_MotController_ConfigForwardSoftLimit(void *handle, int forwardSensorLimit, int timeoutMs)
	{
		MotorControllerWrapper* wrapper = ConvertToWrapper(handle);
		wrapper->Send("ConfigForwardSoftLimit", forwardSensorLimit);
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_ConfigReverseSoftLimit(void *handle, int reverseSensorLimit, int timeoutMs)
	{
		MotorControllerWrapper* wrapper = ConvertToWrapper(handle);
		wrapper->Send("ConfigReverseSoftLimit", reverseSensorLimit);
		return (ErrorCode) 0;
	}
	void c_MotController_EnableSoftLimits(void *handle, bool enable)
	{
		MotorControllerWrapper* wrapper = ConvertToWrapper(handle);
		wrapper->Send("EnableSoftLimits", enable);
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
		MotorControllerWrapper* wrapper = ConvertToWrapper(handle);
		wrapper->Send("ConfigAllowableClosedloopError", slotIdx, allowableClosedLoopError);
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_ConfigMaxIntegralAccumulator(void *handle, int slotIdx, float iaccum, int timeoutMs)
	{
		MotorControllerWrapper* wrapper = ConvertToWrapper(handle);
		wrapper->Send("ConfigMaxIntegralAccumulator", slotIdx, iaccum);
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_SetIntegralAccumulator(void *handle, float iaccum, int timeoutMs)
	{
		MotorControllerWrapper* wrapper = ConvertToWrapper(handle);
		wrapper->Send("SetIntegralAccumulator", iaccum);
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_GetClosedLoopError(void *handle, int *closedLoopError, int slotIdx)
	{
		RECEIVE_HELPER("GetClosedLoopError", 4);
		std::memcpy(closedLoopError, &buffer[0], 4);
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_GetIntegralAccumulator(void *handle, float *iaccum, int slotIdx)
	{
		RECEIVE_HELPER("GetIntegralAccumulator", 4);
		std::memcpy(iaccum, &buffer[0], 4);
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_GetErrorDerivative(void *handle, float *derror, int slotIdx)
	{
		RECEIVE_HELPER("GetErrorDerivative", 4);
		std::memcpy(derror, &buffer[0], 4);
		return (ErrorCode) 0;
	}
	void c_MotController_SelectProfileSlot(void *handle, int slotIdx)
	{
		MotorControllerWrapper* wrapper = ConvertToWrapper(handle);
		wrapper->Send("SelectProfileSlot", slotIdx);
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
		return (ErrorCode) 0;
	}
	int c_MotController_GetFirmwareVersion(void *handle)
	{
		int version = 0;
		RECEIVE_HELPER("GetFirmwareVersion", 4);
		std::memcpy(&version, &buffer[0], 4);
		return version;
	}
	bool c_MotController_HasResetOccurred(void *handle)
	{
		bool output = 0;
		RECEIVE_HELPER("HasResetOccurred", 4);
		std::memcpy(&output, &buffer[0], 4);
		return output;
	}
	ErrorCode c_MotController_ConfigSetCustomParam(void *handle, int newValue, int paramIndex, int timeoutMs)
	{
		MotorControllerWrapper* wrapper = ConvertToWrapper(handle);
		wrapper->Send("ConfigSetCustomParam", newValue, paramIndex);
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_ConfigGetCustomParam(void *handle, int *readValue, int paramIndex, int timoutMs)
	{
		RECEIVE_HELPER("ConfigGetCustomParam", 4);
		std::memcpy(readValue, &buffer[0], 4);
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_ConfigSetParameter(void *handle, int param, float value, int subValue, int ordinal, int timeoutMs)
	{
		MotorControllerWrapper* wrapper = ConvertToWrapper(handle);
		wrapper->Send("ConfigSetParameter", param, value, subValue, ordinal);
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_ConfigGetParameter(void *handle, int param, float *value, int ordinal, int timeoutMs)
	{
		RECEIVE_HELPER("ConfigGetParameter", 4);
		std::memcpy(value, &buffer[0], 4);
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_ConfigPeakCurrentLimit(void *handle, int amps, int timeoutMs)
	{
		MotorControllerWrapper* wrapper = ConvertToWrapper(handle);
		wrapper->Send("ConfigPeakCurrentLimit", amps);
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_ConfigPeakCurrentDuration(void *handle, int milliseconds, int timeoutMs)
	{
		MotorControllerWrapper* wrapper = ConvertToWrapper(handle);
		wrapper->Send("ConfigPeakCurrentDuration", milliseconds);
		return (ErrorCode) 0;
	}
	ErrorCode c_MotController_ConfigContinuousCurrentLimit(void *handle, int amps, int timeoutMs)
	{
		MotorControllerWrapper* wrapper = ConvertToWrapper(handle);
		wrapper->Send("ConfigContinuousCurrentLimit", amps);
		return (ErrorCode) 0;
	}
	void c_MotController_EnableCurrentLimit(void *handle, bool enable)
	{
		MotorControllerWrapper* wrapper = ConvertToWrapper(handle);
		wrapper->Send("EnableCurrentLimit", enable);
	}
	ErrorCode c_MotController_SetLastError(void *handle, int error)
	{
		LOG_UNSUPPORTED_CAN_FUNC("");
		return (ErrorCode) 0;
	}
}
