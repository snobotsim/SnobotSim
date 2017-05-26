#include "CANTalon.h"
#include <iostream>

#ifndef __FUNCTION_NAME__
    #ifdef WIN32   //WINDOWS
        #define __FUNCTION_NAME__   __FUNCTION__
    #else          //*NIX
        #define __FUNCTION_NAME__   __func__
    #endif
#endif

#define LOG_UNSUPPORTED() std::cerr << "Unsupported function at " << __FILE__ << ":" << __LINE__ << " - " << __FUNCTION_NAME__ << std::endl


CANTalon::CANTalon(int deviceNumber)
{
    LOG_UNSUPPORTED();
}

CANTalon::CANTalon(int deviceNumber, int controlPeriodMs)
{
    LOG_UNSUPPORTED();
}


CANTalon::~CANTalon()
{
    LOG_UNSUPPORTED();
}

int CANTalon::GetDeviceID()
{
    LOG_UNSUPPORTED();
    return 0;
}

void CANTalon::PIDWrite(double output)
{
    LOG_UNSUPPORTED();
}

double CANTalon::PIDGet()
{
    LOG_UNSUPPORTED();
    return 0;
}

void CANTalon::SetExpiration(double timeout)
{
    LOG_UNSUPPORTED();
}

double CANTalon::GetExpiration()  const
{
    LOG_UNSUPPORTED();
    return 0;
}

bool CANTalon::IsAlive()  const
{
    LOG_UNSUPPORTED();
    return false;
}

void CANTalon::StopMotor()
{
    LOG_UNSUPPORTED();
}

void CANTalon::SetSafetyEnabled(bool enabled)
{
    LOG_UNSUPPORTED();
}

bool CANTalon::IsSafetyEnabled()  const
{
    LOG_UNSUPPORTED();
    return false;
}

void CANTalon::GetDescription(std::ostringstream& desc)  const
{
    LOG_UNSUPPORTED();
}

double CANTalon::Get()  const
{
    LOG_UNSUPPORTED();
    return 0;
}

void CANTalon::Set(double value)
{
    LOG_UNSUPPORTED();
}

void CANTalon::Reset()
{
    LOG_UNSUPPORTED();
}

void CANTalon::SetSetpoint(double value)
{
    LOG_UNSUPPORTED();
}

void CANTalon::Disable()
{
    LOG_UNSUPPORTED();
}

void CANTalon::EnableControl()
{
    LOG_UNSUPPORTED();
}

void CANTalon::Enable()
{
    LOG_UNSUPPORTED();
}

void CANTalon::SetP(double p)
{
    LOG_UNSUPPORTED();
}

void CANTalon::SetI(double i)
{
    LOG_UNSUPPORTED();
}

void CANTalon::SetD(double d)
{
    LOG_UNSUPPORTED();
}

void CANTalon::SetF(double f)
{
    LOG_UNSUPPORTED();
}

void CANTalon::SetIzone(unsigned iz)
{
    LOG_UNSUPPORTED();
}

void CANTalon::SetPID(double p, double i, double d)
{
    LOG_UNSUPPORTED();
}

void  CANTalon::SetPID(double p, double i, double d, double f)
{
    LOG_UNSUPPORTED();
}

double CANTalon::GetP()  const
{
    LOG_UNSUPPORTED();
    return 0;
}

double CANTalon::GetI()  const
{
    LOG_UNSUPPORTED();
    return 0;
}

double CANTalon::GetD()  const
{
    LOG_UNSUPPORTED();
    return 0;
}

double  CANTalon::GetF()  const
{
    LOG_UNSUPPORTED();
    return 0;
}

bool CANTalon::IsModePID(CANSpeedController::ControlMode mode)  const
{
    LOG_UNSUPPORTED();
    return false;
}

double CANTalon::GetBusVoltage()  const
{
    LOG_UNSUPPORTED();
    return 0;
}

double CANTalon::GetOutputVoltage()  const
{
    LOG_UNSUPPORTED();
    return 0;
}

double CANTalon::GetOutputCurrent()  const
{
    LOG_UNSUPPORTED();
    return 0;
}

double CANTalon::GetTemperature()  const
{
    LOG_UNSUPPORTED();
    return 0;
}

void CANTalon::SetPosition(double pos)
{
    LOG_UNSUPPORTED();
}

double CANTalon::GetPosition()  const
{
    LOG_UNSUPPORTED();
    return 0;
}

double CANTalon::GetSpeed()  const
{
    LOG_UNSUPPORTED();
    return 0;
}

int CANTalon::GetClosedLoopError()  const
{
    LOG_UNSUPPORTED();
    return 0;
}

void CANTalon::SetAllowableClosedLoopErr(uint32_t allowableCloseLoopError)
{
    LOG_UNSUPPORTED();
}

int CANTalon::GetAnalogIn()  const
{
    LOG_UNSUPPORTED();
    return 0;
}

void CANTalon::SetAnalogPosition(int newPosition)
{
    LOG_UNSUPPORTED();
}

int CANTalon::GetAnalogInRaw()  const
{
    LOG_UNSUPPORTED();
    return 0;
}

int CANTalon::GetAnalogInVel()  const
{
    LOG_UNSUPPORTED();
    return 0;
}

int CANTalon::GetEncPosition()  const
{
    LOG_UNSUPPORTED();
    return 0;
}

void CANTalon::SetEncPosition(int)
{
    LOG_UNSUPPORTED();
}

int CANTalon::GetEncVel()  const
{
    LOG_UNSUPPORTED();
    return 0;
}

int CANTalon::GetPinStateQuadA()  const
{
    LOG_UNSUPPORTED();
    return 0;
}

int CANTalon::GetPinStateQuadB()  const
{
    LOG_UNSUPPORTED();
    return 0;
}

int CANTalon::GetPinStateQuadIdx()  const
{
    LOG_UNSUPPORTED();
    return 0;
}

int CANTalon::IsFwdLimitSwitchClosed()  const
{
    LOG_UNSUPPORTED();
    return 0;
}

int CANTalon::IsRevLimitSwitchClosed()  const
{
    LOG_UNSUPPORTED();
    return 0;
}

int CANTalon::IsZeroSensorPositionOnForwardLimitEnabled()  const
{
    LOG_UNSUPPORTED();
    return 0;
}

int CANTalon::IsZeroSensorPositionOnReverseLimitEnabled()  const
{
    LOG_UNSUPPORTED();
    return 0;
}

int CANTalon::IsZeroSensorPositionOnIndexEnabled()  const
{
    LOG_UNSUPPORTED();
    return 0;
}

int CANTalon::GetNumberOfQuadIdxRises()  const
{
    LOG_UNSUPPORTED();
    return 0;
}

void CANTalon::SetNumberOfQuadIdxRises(int rises)
{
    LOG_UNSUPPORTED();
}

int CANTalon::GetPulseWidthPosition()  const
{
    LOG_UNSUPPORTED();
    return 0;
}

void CANTalon::SetPulseWidthPosition(int newpos)
{
    LOG_UNSUPPORTED();
}

int CANTalon::GetPulseWidthVelocity()  const
{
    LOG_UNSUPPORTED();
    return 0;
}

int CANTalon::GetPulseWidthRiseToFallUs()  const
{
    LOG_UNSUPPORTED();
    return 0;
}

int CANTalon::GetPulseWidthRiseToRiseUs()  const
{
    LOG_UNSUPPORTED();
    return 0;
}

CANTalon::FeedbackDeviceStatus CANTalon::IsSensorPresent(
    FeedbackDevice feedbackDevice) const
{
    LOG_UNSUPPORTED();
	return (CANTalon::FeedbackDeviceStatus) 0;
}

bool CANTalon::GetForwardLimitOK()  const
{
    LOG_UNSUPPORTED();
    return false;
}

bool CANTalon::GetReverseLimitOK()  const
{
    LOG_UNSUPPORTED();
    return false;
}

uint16_t CANTalon::GetFaults()  const
{
    LOG_UNSUPPORTED();
    return 0;
}

uint16_t CANTalon::GetStickyFaults()  const
{
    LOG_UNSUPPORTED();
    return 0;
}

void CANTalon::ClearStickyFaults()
{
    LOG_UNSUPPORTED();
}

void CANTalon::SetVoltageRampRate(double rampRate)
{
    LOG_UNSUPPORTED();
}

void CANTalon::SetVoltageCompensationRampRate(double rampRate)
{
    LOG_UNSUPPORTED();
}

int CANTalon::GetFirmwareVersion()  const
{
    LOG_UNSUPPORTED();
    return 0;
}

void CANTalon::ConfigNeutralMode(NeutralMode mode)
{
    LOG_UNSUPPORTED();
}

void CANTalon::ConfigEncoderCodesPerRev(uint16_t codesPerRev)
{
    LOG_UNSUPPORTED();
}

void CANTalon::ConfigPotentiometerTurns(uint16_t turns)
{
    LOG_UNSUPPORTED();
}

void CANTalon::ConfigSoftPositionLimits(
		double forwardLimitPosition,
        double reverseLimitPosition)
{
    LOG_UNSUPPORTED();
}

void CANTalon::DisableSoftPositionLimits()
{
    LOG_UNSUPPORTED();
}

void CANTalon::ConfigLimitMode(LimitMode mode)
{
    LOG_UNSUPPORTED();
}

void CANTalon::ConfigForwardLimit(double forwardLimitPosition)
{
    LOG_UNSUPPORTED();
}

void CANTalon::ConfigReverseLimit(double reverseLimitPosition)
{
    LOG_UNSUPPORTED();
}

void CANTalon::ConfigLimitSwitchOverrides(
		bool bForwardLimitSwitchEn,
        bool bReverseLimitSwitchEn)
{
    LOG_UNSUPPORTED();
}

void CANTalon::ConfigForwardSoftLimitEnable(bool bForwardSoftLimitEn)
{
    LOG_UNSUPPORTED();
}

void CANTalon::ConfigReverseSoftLimitEnable(bool bReverseSoftLimitEn)
{
    LOG_UNSUPPORTED();
}

void CANTalon::ConfigFwdLimitSwitchNormallyOpen(bool normallyOpen)
{
    LOG_UNSUPPORTED();
}

void CANTalon::ConfigRevLimitSwitchNormallyOpen(bool normallyOpen)
{
    LOG_UNSUPPORTED();
}

void CANTalon::ConfigMaxOutputVoltage(double voltage)
{
    LOG_UNSUPPORTED();
}

void CANTalon::ConfigPeakOutputVoltage(double forwardVoltage, double reverseVoltage)
{
    LOG_UNSUPPORTED();
}

void CANTalon::ConfigNominalOutputVoltage(double forwardVoltage, double reverseVoltage)
{
    LOG_UNSUPPORTED();
}

void CANTalon::EnableZeroSensorPositionOnIndex(bool enable, bool risingEdge)
{
    LOG_UNSUPPORTED();
}

void CANTalon::EnableZeroSensorPositionOnForwardLimit(bool enable)
{
    LOG_UNSUPPORTED();
}

void CANTalon::EnableZeroSensorPositionOnReverseLimit(bool enable)
{
    LOG_UNSUPPORTED();
}

int CANTalon::ConfigSetParameter(uint32_t paramEnum, double value)
{
    LOG_UNSUPPORTED();
    return 0;
}

bool CANTalon::GetParameter(uint32_t paramEnum, double& dvalue)  const
{
    LOG_UNSUPPORTED();
    return false;
}

void CANTalon::ConfigFaultTime(double faultTime)
{
    LOG_UNSUPPORTED();
}

void CANTalon::SetControlMode(ControlMode mode)
{
    LOG_UNSUPPORTED();
}

void CANTalon::SetTalonControlMode(TalonControlMode talonControlMode)
{
    LOG_UNSUPPORTED();
}

CANTalon::TalonControlMode CANTalon::GetTalonControlMode()   const
{
    LOG_UNSUPPORTED();
    return (CANTalon::TalonControlMode) 0;
}

void CANTalon::SetFeedbackDevice(FeedbackDevice device)
{
    LOG_UNSUPPORTED();
}

void CANTalon::SetStatusFrameRateMs(StatusFrameRate stateFrame, int periodMs)
{
    LOG_UNSUPPORTED();
}

CANTalon::ControlMode CANTalon::GetControlMode()  const
{
    LOG_UNSUPPORTED();
    return (CANTalon::ControlMode) 0;
}

void CANTalon::SetSensorDirection(bool reverseSensor)
{
    LOG_UNSUPPORTED();
}

void CANTalon::SetClosedLoopOutputDirection(bool reverseOutput)
{
    LOG_UNSUPPORTED();
}

void CANTalon::SetCloseLoopRampRate(double rampRate)
{
    LOG_UNSUPPORTED();
}

void CANTalon::SelectProfileSlot(int slotIdx)
{
    LOG_UNSUPPORTED();
}

int CANTalon::GetIzone()  const
{
    LOG_UNSUPPORTED();
    return 0;
}

int CANTalon::GetIaccum()  const
{
    LOG_UNSUPPORTED();
    return 0;
}

void CANTalon::ClearIaccum()
{
    LOG_UNSUPPORTED();
}

int CANTalon::GetBrakeEnableDuringNeutral()  const
{
    LOG_UNSUPPORTED();
    return 0;
}

bool CANTalon::IsControlEnabled()  const
{
    LOG_UNSUPPORTED();
    return false;
}

bool CANTalon::IsEnabled()  const
{
    LOG_UNSUPPORTED();
    return false;
}

double CANTalon::GetSetpoint()  const
{
    LOG_UNSUPPORTED();
    return 0;
}

void CANTalon::ChangeMotionControlFramePeriod(int periodMs)
{
    LOG_UNSUPPORTED();
}

void CANTalon::ClearMotionProfileTrajectories()
{
    LOG_UNSUPPORTED();
}

int CANTalon::GetMotionProfileTopLevelBufferCount()
{
    LOG_UNSUPPORTED();
    return 0;
}

bool CANTalon::PushMotionProfileTrajectory(const TrajectoryPoint& trajPt)
{
    LOG_UNSUPPORTED();
    return false;
}

bool CANTalon::IsMotionProfileTopLevelBufferFull()
{
    LOG_UNSUPPORTED();
    return false;
}

void CANTalon::ProcessMotionProfileBuffer()
{
    LOG_UNSUPPORTED();
}

void CANTalon::GetMotionProfileStatus(MotionProfileStatus& motionProfileStatus)
{
    LOG_UNSUPPORTED();
}

void CANTalon::ClearMotionProfileHasUnderrun()
{
    LOG_UNSUPPORTED();
}

int CANTalon::SetMotionMagicCruiseVelocity(double motMagicCruiseVeloc)
{
    LOG_UNSUPPORTED();
    return 0;
}

int CANTalon::SetMotionMagicAcceleration(double motMagicAccel)
{
    LOG_UNSUPPORTED();
    return 0;
}

double CANTalon::GetMotionMagicCruiseVelocity()
{
    LOG_UNSUPPORTED();
    return 0;
}

double CANTalon::GetMotionMagicAcceleration()
{
    LOG_UNSUPPORTED();
    return 0;
}

int CANTalon::SetCurrentLimit(uint32_t amps)
{
    LOG_UNSUPPORTED();
    return 0;
}

int CANTalon::EnableCurrentLimit(bool enable)
{
    LOG_UNSUPPORTED();
    return 0;
}

bool CANTalon::HasResetOccured()
{
    LOG_UNSUPPORTED();
    return false;
}

int CANTalon::GetCustomParam0(int32_t & value)
{
    LOG_UNSUPPORTED();
    return 0;
}

int CANTalon::GetCustomParam1(int32_t & value)
{
    LOG_UNSUPPORTED();
    return 0;
}

int CANTalon::IsPersStorageSaving(bool & isSaving)
{
    LOG_UNSUPPORTED();
    return 0;
}

int CANTalon::SetCustomParam0(int32_t value)
{
    LOG_UNSUPPORTED();
    return 0;
}

int CANTalon::SetCustomParam1(int32_t value)
{
    LOG_UNSUPPORTED();
    return 0;
}

int CANTalon::GetGadgeteerStatus(IGadgeteerUartClient::GadgeteerUartStatus & status)
{
    LOG_UNSUPPORTED();
    return 0;
}

void CANTalon::UpdateTable()
{
    LOG_UNSUPPORTED();
}

void CANTalon::ValueChanged(
		ITable* source,
		llvm::StringRef key,
		std::shared_ptr<nt::Value> value,
		bool isNew)
{

}

void CANTalon::StartLiveWindowMode()
{
    LOG_UNSUPPORTED();
}

void CANTalon::StopLiveWindowMode()
{
    LOG_UNSUPPORTED();
}

std::string CANTalon::GetSmartDashboardType()  const
{
    LOG_UNSUPPORTED();
    return "";
}

void CANTalon::InitTable(std::shared_ptr<ITable> subTable)
{
    LOG_UNSUPPORTED();
}

std::shared_ptr<ITable> CANTalon::GetTable()  const
{
    LOG_UNSUPPORTED();
    return std::shared_ptr<ITable>();
}

void CANTalon::SetInverted(bool isInverted)
{
    LOG_UNSUPPORTED();
}

bool CANTalon::GetInverted()  const
{
    LOG_UNSUPPORTED();
    return false;
}

void CANTalon::ApplyUsageStats(UsageFlags Usage)
{
    LOG_UNSUPPORTED();
}

CANTalon::UsageFlags CANTalon::ControlModeUsage(TalonControlMode mode)
{
    LOG_UNSUPPORTED();
    return (CANTalon::UsageFlags) 0;
}

double CANTalon::GetNativeUnitsPerRotationScalar(FeedbackDevice devToLookup)  const
{
    LOG_UNSUPPORTED();
    return 0;
}

void CANTalon::ApplyControlMode(TalonControlMode mode)
{
    LOG_UNSUPPORTED();
}

int32_t CANTalon::ScaleRotationsToNativeUnits(
		FeedbackDevice devToLookup,
		double fullRotations) const
{
    LOG_UNSUPPORTED();
    return 0;
}

int32_t CANTalon::ScaleVelocityToNativeUnits(
		FeedbackDevice devToLookup,
		double rpm) const
{
    LOG_UNSUPPORTED();
    return 0;
}

double CANTalon::ScaleNativeUnitsToRotations(
		FeedbackDevice devToLookup,
		int32_t nativePos) const
{
    LOG_UNSUPPORTED();
    return 0;
}


double CANTalon::ScaleNativeUnitsToRpm(
		FeedbackDevice devToLookup,
		int32_t nativeVel) const
{
    LOG_UNSUPPORTED();
    return 0;
}

CANSpeedController::ControlMode CANTalon::AdaptCm(TalonControlMode talonControlMode)
{
    LOG_UNSUPPORTED();
    return (CANSpeedController::ControlMode) 0;
}

CANTalon::TalonControlMode CANTalon::AdaptCm(CANSpeedController::ControlMode controlMode)
{
    LOG_UNSUPPORTED();
    return (CANTalon::TalonControlMode) 0;
}

