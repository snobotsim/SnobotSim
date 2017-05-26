#include "CanTalonSRX.h"
#include <iostream>

#include "CanTalonSpeedController.h"
#include "SnobotSim/SensorActuatorRegistry.h"

int GetHandle(void* handle)
{
    return (int) *((long*)handle);
}

int GetEncoderHandle(void* talonHandlePtr)
{
    int talonHandle = GetHandle(talonHandlePtr);

    return talonHandle | 0xF0000000;
}

std::shared_ptr<CanTalonSpeedController> GetCanTalon(int handle)
{
    std::shared_ptr<SpeedControllerWrapper> speedController =
            SensorActuatorRegistry::Get().GetSpeedControllerWrapper(handle);

    return std::dynamic_pointer_cast<CanTalonSpeedController>(speedController);
}

std::shared_ptr<CanTalonSpeedController> GetCanTalon(void* handle)
{
    int intHandle = GetHandle(handle);
    return GetCanTalon(intHandle);
}


void* c_TalonSRX_Create3(int deviceNmber, int controlPeriodMs, int enablePeriodMs)
{
    return c_TalonSRX_Create1(deviceNmber);
}

void* c_TalonSRX_Create2(int deviceNumber, int controlPeriodMs)
{
    return c_TalonSRX_Create1(deviceNumber);
}

void* c_TalonSRX_Create1(int deviceNumber)
{
    std::shared_ptr<SpeedControllerWrapper> speedController(new CanTalonSpeedController(deviceNumber));
    SensorActuatorRegistry::Get().Register(deviceNumber, speedController);
    
    return speedController.get();
}

void c_TalonSRX_Destroy(void *handle)
{
    LOG_UNSUPPORTED();
}

void c_TalonSRX_Set(void *handle, double value)
{
    GetCanTalon(handle)->SmartSet(value);
}

CTR_Code c_TalonSRX_SetParam(void *handle, int paramEnum, double value)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_RequestParam(void *handle, int paramEnum)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_GetParamResponse(void *handle, int paramEnum, double *value)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_GetParamResponseInt32(void *handle, int paramEnum, int *value)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_SetPgain(void *handle, int slotIdx, double gain)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_SetIgain(void *handle, int slotIdx, double gain)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_SetDgain(void *handle, int slotIdx, double gain)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_SetFgain(void *handle, int slotIdx, double gain)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_SetIzone(void *handle, int slotIdx, int zone)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_SetCloseLoopRampRate(void *handle, int slotIdx, int closeLoopRampRate)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_SetVoltageCompensationRate(void *handle, double voltagePerMs)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_SetSensorPosition(void *handle, int pos)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_SetForwardSoftLimit(void *handle, int forwardLimit)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_SetReverseSoftLimit(void *handle, int reverseLimit)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_SetForwardSoftEnable(void *handle, int enable)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_SetReverseSoftEnable(void *handle, int enable)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_GetPgain(void *handle, int slotIdx, double *gain)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_GetIgain(void *handle, int slotIdx, double *gain)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_GetDgain(void *handle, int slotIdx, double *gain)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_GetFgain(void *handle, int slotIdx, double *gain)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_GetIzone(void *handle, int slotIdx, int *zone)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_GetCloseLoopRampRate(void *handle, int slotIdx, int *closeLoopRampRate)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_GetVoltageCompensationRate(void *handle, double *voltagePerMs)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_GetForwardSoftLimit(void *handle, int *forwardLimit)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_GetReverseSoftLimit(void *handle, int *reverseLimit)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_GetForwardSoftEnable(void *handle, int *enable)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_GetReverseSoftEnable(void *handle, int *enable)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_GetPulseWidthRiseToFallUs(void *handle, int *param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_IsPulseWidthSensorPresent(void *handle, int *param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_SetModeSelect2(void *handle, int modeSelect, int demand)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_SetStatusFrameRate(void *handle, int frameEnum, int periodMs)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_ClearStickyFaults(void *handle)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

void c_TalonSRX_ChangeMotionControlFramePeriod(void *handle, int periodMs)
{
    LOG_UNSUPPORTED();

}

void c_TalonSRX_ClearMotionProfileTrajectories(void *handle)
{
    LOG_UNSUPPORTED();

}

int c_TalonSRX_GetMotionProfileTopLevelBufferCount(void *handle)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

int c_TalonSRX_IsMotionProfileTopLevelBufferFull(void *handle)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_PushMotionProfileTrajectory(void *handle, int targPos, int targVel, int profileSlotSelect, int timeDurMs, int velOnly, int isLastPoint, int zeroPos)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

void c_TalonSRX_ProcessMotionProfileBuffer(void *handle)
{
    LOG_UNSUPPORTED();

}

CTR_Code c_TalonSRX_GetMotionProfileStatus(void *handle, int *flags, int *profileSlotSelect, int *targPos, int *targVel, int *topBufferRemaining, int *topBufferCnt, int *btmBufferCnt, int *outputEnable)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_GetFault_OverTemp(void *handle, int *param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_GetFault_UnderVoltage(void *handle, int *param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_GetFault_ForLim(void *handle, int *param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_GetFault_RevLim(void *handle, int *param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_GetFault_HardwareFailure(void *handle, int *param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_GetFault_ForSoftLim(void *handle, int *param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_GetFault_RevSoftLim(void *handle, int *param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_GetStckyFault_OverTemp(void *handle, int *param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_GetStckyFault_UnderVoltage(void *handle, int *param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_GetStckyFault_ForLim(void *handle, int *param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_GetStckyFault_RevLim(void *handle, int *param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_GetStckyFault_ForSoftLim(void *handle, int *param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_GetStckyFault_RevSoftLim(void *handle, int *param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_GetAppliedThrottle(void *handle, int *param)
{
    double voltagePercent = SensorActuatorRegistry::Get().GetSpeedControllerWrapper(GetHandle(handle))->GetVoltagePercentage();

    *param = (int) (voltagePercent * 1023);

    return CTR_OKAY;
}

CTR_Code c_TalonSRX_GetCloseLoopErr(void *handle, int *param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_GetFeedbackDeviceSelect(void *handle, int *param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_GetModeSelect(void *handle, int *param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_GetLimitSwitchEn(void *handle, int *param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_GetLimitSwitchClosedFor(void *handle, int *param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_GetLimitSwitchClosedRev(void *handle, int *param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_GetSensorPosition(void *handle, int *param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_GetSensorVelocity(void *handle, int *param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_GetCurrent(void *handle, double *param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_GetBrakeIsEnabled(void *handle, int *param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_GetEncPosition(void *handle, int *param)
{
    int encoderHandle = GetEncoderHandle(handle);
    std::shared_ptr<EncoderWrapper> wrapper =
            SensorActuatorRegistry::Get().GetEncoderWrapper(encoderHandle);

    if(wrapper)
    {
        double distance = wrapper->GetDistance();
        *param = (int) (distance);
        return CTR_OKAY;
    }

    std::cerr << "Encoder has not been hooked up for " << GetHandle(handle) << ".  The simulator is stupid, remember to call setFeedbackDevice" << std::endl;

    *param = 0;
    return CTR_InvalidParamValue;
}

CTR_Code c_TalonSRX_GetEncVel(void *handle, int *param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_GetEncIndexRiseEvents(void *handle, int *param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_GetQuadApin(void *handle, int *param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_GetQuadBpin(void *handle, int *param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_GetQuadIdxpin(void *handle, int *param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_GetAnalogInWithOv(void *handle, int *param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_GetAnalogInVel(void *handle, int *param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_GetTemp(void *handle, double *param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_GetBatteryV(void *handle, double *param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_GetResetCount(void *handle, int *param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_GetResetFlags(void *handle, int *param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_GetFirmVers(void *handle, int *param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_GetPulseWidthPosition(void *handle, int *param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_GetPulseWidthVelocity(void *handle, int *param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_GetPulseWidthRiseToRiseUs(void *handle, int *param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_GetActTraj_IsValid(void *handle, int *param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_GetActTraj_ProfileSlotSelect(void *handle, int *param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_GetActTraj_VelOnly(void *handle, int *param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_GetActTraj_IsLast(void *handle, int *param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_GetOutputType(void *handle, int *param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_GetHasUnderrun(void *handle, int *param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_GetIsUnderrun(void *handle, int *param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_GetNextID(void *handle, int *param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_GetBufferIsFull(void *handle, int *param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_GetCount(void *handle, int *param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_GetActTraj_Velocity(void *handle, int *param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_GetActTraj_Position(void *handle, int *param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_SetDemand(void *handle, int param)
{
    GetCanTalon(handle)->SmartSet(param);

    return CTR_OKAY;
}

CTR_Code c_TalonSRX_SetOverrideLimitSwitchEn(void *handle, int param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_SetFeedbackDeviceSelect(void *handle, int param)
{
    switch(param)
    {
    case 0:
    {
        int talonHandle = GetHandle(handle);
        int encoderHandle = GetEncoderHandle(handle);
        std::string encoderName = "CAN Encoder " + std::to_string(talonHandle);
        SensorActuatorRegistry::Get().Register(encoderHandle, std::shared_ptr<EncoderWrapper>(new EncoderWrapper(encoderName)));
        break;
    }
    default:
        std::cerr << "Unknown feedback device " << param << std::endl;

    }

    return CTR_OKAY;
}

CTR_Code c_TalonSRX_SetRevMotDuringCloseLoopEn(void *handle, int param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_SetOverrideBrakeType(void *handle, int param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_SetModeSelect(void *handle, int param)
{
    CanTalonSpeedController::ControlMode mode = CanTalonSpeedController::ControlMode_Unknown;

    switch(param)
    {
    case 0:
        mode = CanTalonSpeedController::ControlMode_ThrottleMode;
        break;
    case 5:
        mode = CanTalonSpeedController::ControlMode_Follower;
        break;
    case 15:
        mode = CanTalonSpeedController::ControlMode_Disabled;
        break;
    default:
        std::cerr << "Unsupported control mode " << param << std::endl;
    }

    std::shared_ptr<CanTalonSpeedController> speedController = GetCanTalon(handle);
    if(mode == CanTalonSpeedController::ControlMode_Follower)
    {
        std::shared_ptr<CanTalonSpeedController> scToFollow = GetCanTalon(speedController->GetLastSetValue());
        scToFollow->AddFollower(speedController);
    }
    else
    {
        speedController->SetControlMode(mode);
    }

    return CTR_OKAY;
}

CTR_Code c_TalonSRX_SetProfileSlotSelect(void *handle, int param)
{
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_SetRampThrottle(void *handle, int param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code c_TalonSRX_SetRevFeedbackSensor(void *handle, int param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}


