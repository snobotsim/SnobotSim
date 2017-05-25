//JNIEXPORT (.*) JNICALL Java_com_ctre_CanTalonJNI_(.*)\R  \(JNIEnv \*, jclass(.*)\);
//JNIEXPORT \1 JNICALL Java_com_ctre_CanTalonJNI_\2\R  \(JNIEnv \*, jclass\3\)\R{\R    c_TalonSRX_\2(\3);\R}\R

#include <jni.h>
#include "support/jni_util.h"

#include "CanTalonSRX.h"

#include <iostream>

#define LOG_UNSUPPORTED()    std::cerr << "Unsupported function " << __LINE__ << std::endl
#define LOG_UNSUPPORTED222() std::cerr << "Unsupported function 2 " << __LINE__ << std::endl

JNIEXPORT jlong JNICALL Java_com_ctre_CanTalonJNI_new_1CanTalonSRX__III
  (JNIEnv *, jclass, jint, jint, jint)
{
    LOG_UNSUPPORTED();
    return 0;
}

JNIEXPORT jlong JNICALL Java_com_ctre_CanTalonJNI_new_1CanTalonSRX__II
  (JNIEnv *, jclass, jint, jint)
{
    LOG_UNSUPPORTED();
    return 0;
}

JNIEXPORT jlong JNICALL Java_com_ctre_CanTalonJNI_new_1CanTalonSRX__I
  (JNIEnv *, jclass, jint)
{
    LOG_UNSUPPORTED();
    return 0;
}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_delete_1CanTalonSRX
  (JNIEnv *, jclass, jlong)
{
    LOG_UNSUPPORTED();
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetLastError
  (JNIEnv *, jclass, jlong)
{
    LOG_UNSUPPORTED();
    return 0;
}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_GetMotionProfileStatus
  (JNIEnv *, jclass, jlong, jobject, jobject)
{
    LOG_UNSUPPORTED222();
}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_Set
  (JNIEnv *, jclass, jlong handle, jdouble value)
{
    c_TalonSRX_Set(&handle, value);
}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_SetParam
  (JNIEnv *, jclass, jlong handle, jint paramEnum, jdouble value)
{
    c_TalonSRX_SetParam(&handle, paramEnum, value);
}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_RequestParam
  (JNIEnv *, jclass, jlong handle, jint paramEnum)
{
    c_TalonSRX_RequestParam(&handle, paramEnum);
}

JNIEXPORT jdouble JNICALL Java_com_ctre_CanTalonJNI_GetParamResponse
  (JNIEnv *, jclass, jlong handle, jint paramEnum)
{
    double output = 0;
    output = c_TalonSRX_GetParamResponse(&handle, paramEnum, &output);
    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetParamResponseInt32
  (JNIEnv *, jclass, jlong handle, jint paramEnum)
{
    int output = 0;
    output = c_TalonSRX_GetParamResponseInt32(&handle, paramEnum, &output);
    return output;
}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_SetPgain
  (JNIEnv *, jclass, jlong handle, jint slotIdx, jdouble gain)
{
    c_TalonSRX_SetPgain(&handle, slotIdx, gain);
}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_SetIgain
  (JNIEnv *, jclass, jlong handle, jint slotIdx, jdouble gain)
{
    c_TalonSRX_SetIgain(&handle, slotIdx, gain);
}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_SetDgain
  (JNIEnv *, jclass, jlong handle, jint slotIdx, jdouble gain)
{
    c_TalonSRX_SetDgain(&handle, slotIdx, gain);
}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_SetFgain
  (JNIEnv *, jclass, jlong handle, jint slotIdx, jdouble gain)
{
    c_TalonSRX_SetFgain(&handle, slotIdx, gain);
}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_SetIzone
  (JNIEnv *, jclass, jlong handle, jint slotIdx, jint zone)
{
    c_TalonSRX_SetIzone(&handle, slotIdx, zone);
}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_SetCloseLoopRampRate
  (JNIEnv *, jclass, jlong handle, jint slotIdx, jint closeLoopRampRate)
{
    c_TalonSRX_SetCloseLoopRampRate(&handle, slotIdx, closeLoopRampRate);
}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_SetVoltageCompensationRate
  (JNIEnv *, jclass, jlong handle, jdouble voltagePerMs)
{
    c_TalonSRX_SetVoltageCompensationRate(&handle, voltagePerMs);
}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_SetSensorPosition
  (JNIEnv *, jclass, jlong handle, jint pos)
{
    c_TalonSRX_SetSensorPosition(&handle, pos);
}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_SetForwardSoftLimit
  (JNIEnv *, jclass, jlong handle, jint forwardLimit)
{
    c_TalonSRX_SetForwardSoftLimit(&handle, forwardLimit);
}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_SetReverseSoftLimit
  (JNIEnv *, jclass, jlong handle, jint reverseLimit)
{
    c_TalonSRX_SetReverseSoftLimit(&handle, reverseLimit);
}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_SetForwardSoftEnable
  (JNIEnv *, jclass, jlong handle, jint enable)
{
    c_TalonSRX_SetForwardSoftEnable(&handle, enable);
}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_SetReverseSoftEnable
  (JNIEnv *, jclass, jlong handle, jint enable)
{
    c_TalonSRX_SetReverseSoftEnable(&handle, enable);
}

JNIEXPORT jdouble JNICALL Java_com_ctre_CanTalonJNI_GetPgain
  (JNIEnv *, jclass, jlong handle, jint slotIdx)
{
    double output = 0;
    output = c_TalonSRX_GetPgain(&handle, slotIdx, &output);
    return output;
}

JNIEXPORT jdouble JNICALL Java_com_ctre_CanTalonJNI_GetIgain
  (JNIEnv *, jclass, jlong handle, jint slotIdx)
{
    double output = 0;
    output = c_TalonSRX_GetIgain(&handle, slotIdx, &output);
    return output;
}

JNIEXPORT jdouble JNICALL Java_com_ctre_CanTalonJNI_GetDgain
  (JNIEnv *, jclass, jlong handle, jint slotIdx)
{
    double output = 0;
    output = c_TalonSRX_GetDgain(&handle, slotIdx, &output);
    return output;
}

JNIEXPORT jdouble JNICALL Java_com_ctre_CanTalonJNI_GetFgain
  (JNIEnv *, jclass, jlong handle, jint slotIdx)
{
    double output = 0;
    output = c_TalonSRX_GetFgain(&handle, slotIdx, &output);
    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetIzone
  (JNIEnv *, jclass, jlong handle, jint slotIdx)
{
    int output = 0;
    output = c_TalonSRX_GetIzone(&handle, slotIdx, &output);
    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetCloseLoopRampRate
  (JNIEnv *, jclass, jlong handle, jint slotIdx)
{
    int output = 0;
    output = c_TalonSRX_GetCloseLoopRampRate(&handle, slotIdx, &output);
    return output;
}

JNIEXPORT jdouble JNICALL Java_com_ctre_CanTalonJNI_GetVoltageCompensationRate
  (JNIEnv *, jclass, jlong handle)
{
    double output = 0;
    output = c_TalonSRX_GetVoltageCompensationRate(&handle, &output);
    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetForwardSoftLimit
  (JNIEnv *, jclass, jlong handle)
{
    int output = 0;
    output = c_TalonSRX_GetForwardSoftLimit(&handle, &output);
    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetReverseSoftLimit
  (JNIEnv *, jclass, jlong handle)
{
    int output = 0;
    output = c_TalonSRX_GetReverseSoftLimit(&handle, &output);
    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetForwardSoftEnable
  (JNIEnv *, jclass, jlong handle)
{
    int output = 0;
    output = c_TalonSRX_GetForwardSoftEnable(&handle, &output);
    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetReverseSoftEnable
  (JNIEnv *, jclass, jlong handle)
{
    int output = 0;
    output = c_TalonSRX_GetReverseSoftEnable(&handle, &output);
    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetPulseWidthRiseToFallUs
  (JNIEnv *, jclass, jlong handle)
{
    int output = 0;
    output = c_TalonSRX_GetPulseWidthRiseToFallUs(&handle, &output);
    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_IsPulseWidthSensorPresent
  (JNIEnv *, jclass, jlong handle)
{
    int output = 0;
    output = c_TalonSRX_IsPulseWidthSensorPresent(&handle, &output);
    return output;
}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_SetModeSelect2
  (JNIEnv *, jclass, jlong handle, jint modeSelect, jint demand)
{
    c_TalonSRX_SetModeSelect2(&handle, modeSelect, demand);
}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_SetStatusFrameRate
  (JNIEnv *, jclass, jlong handle, jint frameEnum, jint periodMs)
{
    c_TalonSRX_SetStatusFrameRate(&handle, frameEnum, periodMs);
}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_ClearStickyFaults
  (JNIEnv *, jclass, jlong handle)
{
    c_TalonSRX_ClearStickyFaults(&handle);
}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_ChangeMotionControlFramePeriod
  (JNIEnv *, jclass, jlong handle, jint periodMs)
{
    c_TalonSRX_ChangeMotionControlFramePeriod(&handle, periodMs);
}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_ClearMotionProfileTrajectories
  (JNIEnv *, jclass, jlong handle)
{
    c_TalonSRX_ClearMotionProfileTrajectories(&handle);
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetMotionProfileTopLevelBufferCount
  (JNIEnv *, jclass, jlong handle)
{
    return c_TalonSRX_GetMotionProfileTopLevelBufferCount(&handle);
}

JNIEXPORT jboolean JNICALL Java_com_ctre_CanTalonJNI_IsMotionProfileTopLevelBufferFull
  (JNIEnv *, jclass, jlong handle)
{
    return c_TalonSRX_IsMotionProfileTopLevelBufferFull(&handle);
}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_PushMotionProfileTrajectory
  (JNIEnv *, jclass, jlong handle, jint targPos, jint targVel, jint profileSlotSelect, jint timeDurMs, jint velOnly, jint isLastPoint, jint zeroPos)
{
    c_TalonSRX_PushMotionProfileTrajectory(&handle, targPos, targVel, profileSlotSelect, timeDurMs, velOnly, isLastPoint, zeroPos);
}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_ProcessMotionProfileBuffer
  (JNIEnv *, jclass, jlong handle)
{
    c_TalonSRX_ProcessMotionProfileBuffer(&handle);
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetFault_1OverTemp
  (JNIEnv *, jclass, jlong)
{
    LOG_UNSUPPORTED();
    return 0;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetFault_1UnderVoltage
  (JNIEnv *, jclass, jlong)
{
    LOG_UNSUPPORTED();
    return 0;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetFault_1ForLim
  (JNIEnv *, jclass, jlong)
{
    LOG_UNSUPPORTED();
    return 0;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetFault_1RevLim
  (JNIEnv *, jclass, jlong)
{
    LOG_UNSUPPORTED();
    return 0;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetFault_1HardwareFailure
  (JNIEnv *, jclass, jlong)
{
    LOG_UNSUPPORTED();
    return 0;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetFault_1ForSoftLim
  (JNIEnv *, jclass, jlong)
{
    LOG_UNSUPPORTED();
    return 0;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetFault_1RevSoftLim
  (JNIEnv *, jclass, jlong)
{
    LOG_UNSUPPORTED();
    return 0;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetStckyFault_1OverTemp
  (JNIEnv *, jclass, jlong)
{
    LOG_UNSUPPORTED();
    return 0;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetStckyFault_1UnderVoltage
  (JNIEnv *, jclass, jlong)
{
    LOG_UNSUPPORTED();
    return 0;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetStckyFault_1ForLim
  (JNIEnv *, jclass, jlong)
{
    LOG_UNSUPPORTED();
    return 0;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetStckyFault_1RevLim
  (JNIEnv *, jclass, jlong)
{
    LOG_UNSUPPORTED();
    return 0;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetStckyFault_1ForSoftLim
  (JNIEnv *, jclass, jlong)
{
    LOG_UNSUPPORTED();
    return 0;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetStckyFault_1RevSoftLim
  (JNIEnv *, jclass, jlong)
{
    LOG_UNSUPPORTED();
    return 0;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetAppliedThrottle
  (JNIEnv *, jclass, jlong handle)
{
    int output = 0;
    output = c_TalonSRX_GetAppliedThrottle(&handle, &output);
    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetCloseLoopErr
  (JNIEnv *, jclass, jlong handle)
{
    int output = 0;
    output = c_TalonSRX_GetCloseLoopErr(&handle, &output);
    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetFeedbackDeviceSelect
  (JNIEnv *, jclass, jlong handle)
{
    int output = 0;
    output = c_TalonSRX_GetFeedbackDeviceSelect(&handle, &output);
    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetModeSelect
  (JNIEnv *, jclass, jlong handle)
{
    int output = 0;
    output = c_TalonSRX_GetModeSelect(&handle, &output);
    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetLimitSwitchEn
  (JNIEnv *, jclass, jlong handle)
{
    int output = 0;
    output = c_TalonSRX_GetLimitSwitchEn(&handle, &output);
    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetLimitSwitchClosedFor
  (JNIEnv *, jclass, jlong handle)
{
    int output = 0;
    output = c_TalonSRX_GetLimitSwitchClosedFor(&handle, &output);
    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetLimitSwitchClosedRev
  (JNIEnv *, jclass, jlong handle)
{
    int output = 0;
    output = c_TalonSRX_GetLimitSwitchClosedRev(&handle, &output);
    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetSensorPosition
  (JNIEnv *, jclass, jlong handle)
{
    int output = 0;
    output = c_TalonSRX_GetSensorPosition(&handle, &output);
    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetSensorVelocity
  (JNIEnv *, jclass, jlong handle)
{
    int output = 0;
    output = c_TalonSRX_GetSensorVelocity(&handle, &output);
    return output;
}

JNIEXPORT jdouble JNICALL Java_com_ctre_CanTalonJNI_GetCurrent
  (JNIEnv *, jclass, jlong handle)
{
    double output = 0;
    output = c_TalonSRX_GetCurrent(&handle, &output);
    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetBrakeIsEnabled
  (JNIEnv *, jclass, jlong handle)
{
    int output = 0;
    output = c_TalonSRX_GetBrakeIsEnabled(&handle, &output);
    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetEncPosition
  (JNIEnv *, jclass, jlong handle)
{
    int output = 0;
    output = c_TalonSRX_GetEncPosition(&handle, &output);
    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetEncVel
  (JNIEnv *, jclass, jlong handle)
{
    int output = 0;
    output = c_TalonSRX_GetEncVel(&handle, &output);
    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetEncIndexRiseEvents
  (JNIEnv *, jclass, jlong handle)
{
    int output = 0;
    output = c_TalonSRX_GetEncIndexRiseEvents(&handle, &output);
    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetQuadApin
  (JNIEnv *, jclass, jlong handle)
{
    int output = 0;
    output = c_TalonSRX_GetQuadApin(&handle, &output);
    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetQuadBpin
  (JNIEnv *, jclass, jlong handle)
{
    int output = 0;
    output = c_TalonSRX_GetQuadBpin(&handle, &output);
    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetQuadIdxpin
  (JNIEnv *, jclass, jlong handle)
{
    int output = 0;
    output = c_TalonSRX_GetQuadIdxpin(&handle, &output);
    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetAnalogInWithOv
  (JNIEnv *, jclass, jlong handle)
{
    int output = 0;
    output = c_TalonSRX_GetAnalogInWithOv(&handle, &output);
    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetAnalogInVel
  (JNIEnv *, jclass, jlong handle)
{
    int output = 0;
    output = c_TalonSRX_GetAnalogInVel(&handle, &output);
    return output;
}

JNIEXPORT jdouble JNICALL Java_com_ctre_CanTalonJNI_GetTemp
  (JNIEnv *, jclass, jlong handle)
{
    double output = 0;
    output = c_TalonSRX_GetTemp(&handle, &output);
    return output;
}

JNIEXPORT jdouble JNICALL Java_com_ctre_CanTalonJNI_GetBatteryV
  (JNIEnv *, jclass, jlong handle)
{
    double output = 0;
    output = c_TalonSRX_GetBatteryV(&handle, &output);
    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetClearPosOnIdx
  (JNIEnv *, jclass, jlong)
{
    LOG_UNSUPPORTED();
    return 0;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetClearPosOnLimR
  (JNIEnv *, jclass, jlong)
{
    LOG_UNSUPPORTED();
    return 0;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetClearPosOnLimF
  (JNIEnv *, jclass, jlong)
{
    LOG_UNSUPPORTED();
    return 0;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetResetCount
  (JNIEnv *, jclass, jlong handle)
{
    int output = 0;
    output = c_TalonSRX_GetResetCount(&handle, &output);
    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetResetFlags
  (JNIEnv *, jclass, jlong handle)
{
    int output = 0;
    output = c_TalonSRX_GetResetFlags(&handle, &output);
    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetFirmVers
  (JNIEnv *, jclass, jlong handle)
{
    int output = 0;
    output = c_TalonSRX_GetFirmVers(&handle, &output);
    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetPulseWidthPosition
  (JNIEnv *, jclass, jlong handle)
{
    int output = 0;
    output = c_TalonSRX_GetPulseWidthPosition(&handle, &output);
    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetPulseWidthVelocity
  (JNIEnv *, jclass, jlong handle)
{
    int output = 0;
    output = c_TalonSRX_GetPulseWidthVelocity(&handle, &output);
    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetPulseWidthRiseToRiseUs
  (JNIEnv *, jclass, jlong handle)
{
    int output = 0;
    output = c_TalonSRX_GetPulseWidthRiseToRiseUs(&handle, &output);
    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetActTraj_1IsValid
  (JNIEnv *, jclass, jlong)
{
    LOG_UNSUPPORTED();
    return 0;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetActTraj_1ProfileSlotSelect
  (JNIEnv *, jclass, jlong)
{
    LOG_UNSUPPORTED();
    return 0;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetActTraj_1VelOnly
  (JNIEnv *, jclass, jlong)
{
    LOG_UNSUPPORTED();
    return 0;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetActTraj_1IsLast
  (JNIEnv *, jclass, jlong)
{
    LOG_UNSUPPORTED();
    return 0;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetOutputType
  (JNIEnv *, jclass, jlong handle)
{
    int output = 0;
    output = c_TalonSRX_GetOutputType(&handle, &output);
    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetHasUnderrun
  (JNIEnv *, jclass, jlong handle)
{
    int output = 0;
    output = c_TalonSRX_GetHasUnderrun(&handle, &output);
    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetIsUnderrun
  (JNIEnv *, jclass, jlong handle)
{
    int output = 0;
    output = c_TalonSRX_GetIsUnderrun(&handle, &output);
    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetNextID
  (JNIEnv *, jclass, jlong handle)
{
    int output = 0;
    output = c_TalonSRX_GetNextID(&handle, &output);
    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetBufferIsFull
  (JNIEnv *, jclass, jlong handle)
{
    int output = 0;
    output = c_TalonSRX_GetBufferIsFull(&handle, &output);
    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetCount
  (JNIEnv *, jclass, jlong handle)
{
    int output = 0;
    output = c_TalonSRX_GetCount(&handle, &output);
    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetActTraj_1Velocity
  (JNIEnv *, jclass, jlong)
{
    LOG_UNSUPPORTED();
    return 0;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetActTraj_1Position
  (JNIEnv *, jclass, jlong)
{
    LOG_UNSUPPORTED();
    return 0;
}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_SetDemand
  (JNIEnv *, jclass, jlong handle, jint param)
{
    c_TalonSRX_SetDemand(&handle, param);
}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_SetOverrideLimitSwitchEn
  (JNIEnv *, jclass, jlong handle, jint param)
{
    c_TalonSRX_SetOverrideLimitSwitchEn(&handle, param);
}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_SetFeedbackDeviceSelect
  (JNIEnv *, jclass, jlong handle, jint param)
{
    c_TalonSRX_SetFeedbackDeviceSelect(&handle, param);
}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_SetRevMotDuringCloseLoopEn
  (JNIEnv *, jclass, jlong handle, jint param)
{
    c_TalonSRX_SetRevMotDuringCloseLoopEn(&handle, param);
}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_SetOverrideBrakeType
  (JNIEnv *, jclass, jlong handle, jint param)
{
    c_TalonSRX_SetOverrideBrakeType(&handle, param);
}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_SetModeSelect
  (JNIEnv *, jclass, jlong handle, jint param)
{
    c_TalonSRX_SetModeSelect(&handle, param);
}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_SetProfileSlotSelect
  (JNIEnv *, jclass, jlong handle, jint param)
{
    c_TalonSRX_SetProfileSlotSelect(&handle, param);
}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_SetRampThrottle
  (JNIEnv *, jclass, jlong handle, jint param)
{
    c_TalonSRX_SetRampThrottle(&handle, param);
}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_SetRevFeedbackSensor
  (JNIEnv *, jclass, jlong handle, jint param)
{
    c_TalonSRX_SetRevFeedbackSensor(&handle, param);
}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_SetCurrentLimEnable
  (JNIEnv *, jclass, jlong, jboolean)
{
    LOG_UNSUPPORTED();
}


