#include "ctre/Phoenix/core/ErrorCode.h"

#include "MockHookUtilities.h"

extern "C"{
	void c_Testblah();
	EXPORT_ void* c_MotController_Create1(int baseArbId);

	EXPORT_ ErrorCode c_MotController_GetDeviceNumber(void *handle, int *deviceNumber);
	EXPORT_ void c_MotController_SetDemand(void *handle, int mode, int demand0, int demand1);
	EXPORT_ void c_MotController_SetNeutralMode(void *handle, int neutralMode);
	EXPORT_ void c_MotController_SetSensorPhase(void *handle, bool PhaseSensor);
	EXPORT_ void c_MotController_SetInverted(void *handle, bool invert);
	EXPORT_ ErrorCode c_MotController_ConfigOpenLoopRamp(void *handle, float secondsFromNeutralToFull, int timeoutMs);
	EXPORT_ ErrorCode c_MotController_ConfigClosedLoopRamp(void *handle, float secondsFromNeutralToFull, int timeoutMs);
	EXPORT_ ErrorCode c_MotController_ConfigPeakOutputForward(void *handle, float percentOut, int timeoutMs);
	EXPORT_ ErrorCode c_MotController_ConfigPeakOutputReverse(void *handle, float percentOut, int timeoutMs);
	EXPORT_ ErrorCode c_MotController_ConfigNominalOutputForward(void *handle, float percentOut, int timeoutMs);
	EXPORT_ ErrorCode c_MotController_ConfigNominalOutputReverse(void *handle, float percentOut, int timeoutMs);
	EXPORT_ ErrorCode c_MotController_ConfigNeutralDeadband(void *handle, float percentDeadband, int timeoutMs);
	EXPORT_ ErrorCode c_MotController_ConfigVoltageCompSaturation(void *handle, float voltage, int timeoutMs);
	EXPORT_ ErrorCode c_MotController_ConfigVoltageMeasurementFilter(void *handle, int filterWindowSamples, int timeoutMs);
	EXPORT_ void c_MotController_EnableVoltageCompensation(void *handle, bool enable);
	EXPORT_ ErrorCode c_MotController_GetBusVoltage(void *handle, float *voltage);
	EXPORT_ ErrorCode c_MotController_GetMotorOutputPercent(void *handle, float *percentOutput);
	EXPORT_ ErrorCode c_MotController_GetOutputCurrent(void *handle, float *current);
	EXPORT_ ErrorCode c_MotController_GetTemperature(void *handle, float *temperature);
	EXPORT_ ErrorCode c_MotController_ConfigRemoteFeedbackFilter(void *handle, int arbId, int peripheralIdx, int reserved, int timeoutMs);
	EXPORT_ ErrorCode c_MotController_ConfigSelectedFeedbackSensor(void *handle, int feedbackDevice, int timeoutMs);
	EXPORT_ ErrorCode c_MotController_GetSelectedSensorPosition(void *handle, int *param);
	EXPORT_ ErrorCode c_MotController_GetSelectedSensorVelocity(void *handle, int *param);
	EXPORT_ ErrorCode c_MotController_SetSelectedSensorPosition(void *handle, int sensorPos, int timeoutMs);
	EXPORT_ ErrorCode c_MotController_SetControlFramePeriod(void *handle, int frame, int periodMs);
	EXPORT_ ErrorCode c_MotController_SetStatusFramePeriod(void *handle, int frame, int periodMs, int timeoutMs);
	EXPORT_ ErrorCode c_MotController_GetStatusFramePeriod(void *handle, int frame, int *periodMs, int timeoutMs);
	EXPORT_ ErrorCode c_MotController_ConfigVelocityMeasurementPeriod(void *handle, int period, int timeoutMs);
	EXPORT_ ErrorCode c_MotController_ConfigVelocityMeasurementWindow(void *handle, int windowSize, int timeoutMs);
	EXPORT_ ErrorCode c_MotController_ConfigForwardLimitSwitchSource(void *handle, int type, int normalOpenOrClose, int deviceID, int timeoutMs);
	EXPORT_ ErrorCode c_MotController_ConfigReverseLimitSwitchSource(void *handle, int type, int normalOpenOrClose, int deviceID, int timeoutMs);
	EXPORT_ void c_MotController_EnableLimitSwitches(void *handle, bool enable);
	EXPORT_ ErrorCode c_MotController_ConfigForwardSoftLimit(void *handle, int forwardSensorLimit, int timeoutMs);
	EXPORT_ ErrorCode c_MotController_ConfigReverseSoftLimit(void *handle, int reverseSensorLimit, int timeoutMs);
	EXPORT_ void c_MotController_EnableSoftLimits(void *handle, bool enable);
	EXPORT_ ErrorCode c_MotController_Config_kP(void *handle, int slotIdx, float value, int timeoutMs);
	EXPORT_ ErrorCode c_MotController_Config_kI(void *handle, int slotIdx, float value, int timeoutMs);
	EXPORT_ ErrorCode c_MotController_Config_kD(void *handle, int slotIdx, float value, int timeoutMs);
	EXPORT_ ErrorCode c_MotController_Config_kF(void *handle, int slotIdx, float value, int timeoutMs);
	EXPORT_ ErrorCode c_MotController_Config_IntegralZone(void *handle, int slotIdx, float izone, int timeoutMs);
	EXPORT_ ErrorCode c_MotController_ConfigAllowableClosedloopError(void *handle, int slotIdx, int allowableClosedLoopError, int timeoutMs);
	EXPORT_ ErrorCode c_MotController_ConfigMaxIntegralAccumulator(void *handle, int slotIdx, float iaccum, int timeoutMs);
	EXPORT_ ErrorCode c_MotController_SetIntegralAccumulator(void *handle, float iaccum, int timeoutMs);
	EXPORT_ ErrorCode c_MotController_GetClosedLoopError(void *handle, int *closedLoopError, int slotIdx);
	EXPORT_ ErrorCode c_MotController_GetIntegralAccumulator(void *handle, float *iaccum, int slotIdx);
	EXPORT_ ErrorCode c_MotController_GetErrorDerivative(void *handle, float *derror, int slotIdx);
	EXPORT_ void c_MotController_SelectProfileSlot(void *handle, int slotIdx);
	EXPORT_ ErrorCode c_MotController_ConfigMotionCruiseVelocity(void *handle, int sensorUnitsPer100ms, int timeoutMs);
	EXPORT_ ErrorCode c_MotController_ConfigMotionAcceleration(void *handle, int sensorUnitsPer100msPerSec, int timeoutMs);
	EXPORT_ ErrorCode c_MotController_GetLastError(void *handle);
	EXPORT_ int c_MotController_GetFirmwareVersion(void *handle);
	EXPORT_ bool c_MotController_HasResetOccurred(void *handle);
	EXPORT_ ErrorCode c_MotController_ConfigSetCustomParam(void *handle, int newValue, int paramIndex, int timeoutMs);
	EXPORT_ ErrorCode c_MotController_ConfigGetCustomParam(void *handle, int *readValue, int paramIndex, int timoutMs);
	EXPORT_ ErrorCode c_MotController_ConfigSetParameter(void *handle, int param, float value, int subValue, int ordinal, int timeoutMs);
	EXPORT_ ErrorCode c_MotController_ConfigGetParameter(void *handle, int param, float *value, int ordinal, int timeoutMs);
	EXPORT_ ErrorCode c_MotController_ConfigPeakCurrentLimit(void *handle, int amps, int timeoutMs);
	EXPORT_ ErrorCode c_MotController_ConfigPeakCurrentDuration(void *handle, int milliseconds, int timeoutMs);
	EXPORT_ ErrorCode c_MotController_ConfigContinuousCurrentLimit(void *handle, int amps, int timeoutMs);
	EXPORT_ void c_MotController_EnableCurrentLimit(void *handle, bool enable);
	EXPORT_ ErrorCode c_MotController_SetLastError(void *handle, int error);
}
