//JNIEXPORT (.*) JNICALL Java_com_ctre_CanTalonJNI_(.*)\R  \(JNIEnv \*, jclass(.*)\);
//JNIEXPORT \1 JNICALL Java_com_ctre_CanTalonJNI_\2\R  \(JNIEnv \*, jclass\3\)\R{\R    c_TalonSRX_\2(\3);\R}\R

#include <assert.h>
#include <jni.h>
#include "support/jni_util.h"

#include "CanTalonSRX.h"

#include "com_ctre_CanTalonJNI.h"

#include <iostream>



extern "C" {


inline bool CheckCTRStatus(JNIEnv *env, CTR_Code status) {
  if (status != CTR_OKAY)
  {
	  std::cerr << "CTR Error: " << status << std::endl;
  }
  return status == CTR_OKAY;
}


JNIEXPORT jlong JNICALL Java_com_ctre_CanTalonJNI_new_1CanTalonSRX__III
  (JNIEnv *env, jclass, jint deviceNumber, jint controlPeriodMs, jint enablePeriodMs)
{
	  return (jlong)(new CanTalonSRX((int)deviceNumber, (int)controlPeriodMs, (int)enablePeriodMs));
}

JNIEXPORT jlong JNICALL Java_com_ctre_CanTalonJNI_new_1CanTalonSRX__II
  (JNIEnv *env, jclass, jint deviceNumber, jint controlPeriodMs)
{
	  return (jlong)(new CanTalonSRX((int)deviceNumber, (int)controlPeriodMs));
}

JNIEXPORT jlong JNICALL Java_com_ctre_CanTalonJNI_new_1CanTalonSRX__I
  (JNIEnv *env, jclass, jint deviceNumber)
{
	  return (jlong)(new CanTalonSRX((int)deviceNumber));
}

JNIEXPORT jlong JNICALL Java_com_ctre_CanTalonJNI_new_1CanTalonSRX__
  (JNIEnv *env, jclass)
{
	  return (jlong)(new CanTalonSRX);
}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_delete_1CanTalonSRX
  (JNIEnv *env, jclass, jlong handle)
{
	  delete (CanTalonSRX*)handle;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetLastError
  (JNIEnv *env, jclass, jlong)
{
    LOG_UNSUPPORTED();
    return 0;
}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_GetMotionProfileStatus
  (JNIEnv *env, jclass, jlong handle, jobject, jobject)
{
//    int fakeInt = 0;
//    CTR_Code status = ((CanTalonSRX*)handle)->GetMotionProfileStatus(&fakeInt, &fakeInt, &fakeInt, &fakeInt, &fakeInt, &fakeInt, &fakeInt, &fakeInt);
//    CheckCTRStatus(env, status);

    LOG_UNSUPPORTED();
}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_Set
  (JNIEnv *env, jclass, jlong handle, jdouble value)
{
    ((CanTalonSRX*)handle)->Set(value);
}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_SetParam
  (JNIEnv *env, jclass, jlong handle, jint paramEnum, jdouble value)
{
	CanTalonSRX::param_t param = (CanTalonSRX::param_t) paramEnum;
    CTR_Code status = ((CanTalonSRX*)handle)->SetParam(param, value);
    CheckCTRStatus(env, status);

}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_RequestParam
  (JNIEnv *env, jclass, jlong handle, jint paramEnum)
{
	CanTalonSRX::param_t param = (CanTalonSRX::param_t) paramEnum;
    CTR_Code status = ((CanTalonSRX*)handle)->RequestParam(param);
    CheckCTRStatus(env, status);

}

JNIEXPORT jdouble JNICALL Java_com_ctre_CanTalonJNI_GetParamResponse
  (JNIEnv *env, jclass, jlong handle, jint paramEnum)
{
	CanTalonSRX::param_t param = (CanTalonSRX::param_t) paramEnum;

    double output = 0;
    CTR_Code status = ((CanTalonSRX*)handle)->GetParamResponse(param, output);
    CheckCTRStatus(env, status);

    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetParamResponseInt32
  (JNIEnv *env, jclass, jlong handle, jint paramEnum)
{
	CanTalonSRX::param_t param = (CanTalonSRX::param_t) paramEnum;

    int output = 0;
    CTR_Code status = ((CanTalonSRX*)handle)->GetParamResponseInt32(param, output);
    CheckCTRStatus(env, status);

    return output;
}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_SetPgain
  (JNIEnv *env, jclass, jlong handle, jint slotIdx, jdouble gain)
{
    CTR_Code status = ((CanTalonSRX*)handle)->SetPgain(slotIdx, gain);
    CheckCTRStatus(env, status);

}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_SetIgain
  (JNIEnv *env, jclass, jlong handle, jint slotIdx, jdouble gain)
{
    CTR_Code status = ((CanTalonSRX*)handle)->SetIgain(slotIdx, gain);
    CheckCTRStatus(env, status);

}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_SetDgain
  (JNIEnv *env, jclass, jlong handle, jint slotIdx, jdouble gain)
{
    CTR_Code status = ((CanTalonSRX*)handle)->SetDgain(slotIdx, gain);
    CheckCTRStatus(env, status);

}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_SetFgain
  (JNIEnv *env, jclass, jlong handle, jint slotIdx, jdouble gain)
{
    CTR_Code status = ((CanTalonSRX*)handle)->SetFgain(slotIdx, gain);
    CheckCTRStatus(env, status);

}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_SetIzone
  (JNIEnv *env, jclass, jlong handle, jint slotIdx, jint zone)
{
    CTR_Code status = ((CanTalonSRX*)handle)->SetIzone(slotIdx, zone);
    CheckCTRStatus(env, status);

}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_SetCloseLoopRampRate
  (JNIEnv *env, jclass, jlong handle, jint slotIdx, jint closeLoopRampRate)
{
    CTR_Code status = ((CanTalonSRX*)handle)->SetCloseLoopRampRate(slotIdx, closeLoopRampRate);
    CheckCTRStatus(env, status);

}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_SetVoltageCompensationRate
  (JNIEnv *env, jclass, jlong handle, jdouble voltagePerMs)
{
    CTR_Code status = ((CanTalonSRX*)handle)->SetVoltageCompensationRate(voltagePerMs);
    CheckCTRStatus(env, status);

}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_SetSensorPosition
  (JNIEnv *env, jclass, jlong handle, jint pos)
{
    CTR_Code status = ((CanTalonSRX*)handle)->SetSensorPosition(pos);
    CheckCTRStatus(env, status);

}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_SetForwardSoftLimit
  (JNIEnv *env, jclass, jlong handle, jint forwardLimit)
{
    CTR_Code status = ((CanTalonSRX*)handle)->SetForwardSoftLimit(forwardLimit);
    CheckCTRStatus(env, status);

}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_SetReverseSoftLimit
  (JNIEnv *env, jclass, jlong handle, jint reverseLimit)
{
    CTR_Code status = ((CanTalonSRX*)handle)->SetReverseSoftLimit(reverseLimit);
    CheckCTRStatus(env, status);

}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_SetForwardSoftEnable
  (JNIEnv *env, jclass, jlong handle, jint enable)
{
    CTR_Code status = ((CanTalonSRX*)handle)->SetForwardSoftEnable(enable);
    CheckCTRStatus(env, status);

}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_SetReverseSoftEnable
  (JNIEnv *env, jclass, jlong handle, jint enable)
{
    CTR_Code status = ((CanTalonSRX*)handle)->SetReverseSoftEnable(enable);
    CheckCTRStatus(env, status);

}

JNIEXPORT jdouble JNICALL Java_com_ctre_CanTalonJNI_GetPgain
  (JNIEnv *env, jclass, jlong handle, jint slotIdx)
{
    double output = 0;
    CTR_Code status = ((CanTalonSRX*)handle)->GetPgain(slotIdx, output);
    CheckCTRStatus(env, status);

    return output;
}

JNIEXPORT jdouble JNICALL Java_com_ctre_CanTalonJNI_GetIgain
  (JNIEnv *env, jclass, jlong handle, jint slotIdx)
{
    double output = 0;
    CTR_Code status = ((CanTalonSRX*)handle)->GetIgain(slotIdx, output);
    CheckCTRStatus(env, status);

    return output;
}

JNIEXPORT jdouble JNICALL Java_com_ctre_CanTalonJNI_GetDgain
  (JNIEnv *env, jclass, jlong handle, jint slotIdx)
{
    double output = 0;
    CTR_Code status = ((CanTalonSRX*)handle)->GetDgain(slotIdx, output);
    CheckCTRStatus(env, status);

    return output;
}

JNIEXPORT jdouble JNICALL Java_com_ctre_CanTalonJNI_GetFgain
  (JNIEnv *env, jclass, jlong handle, jint slotIdx)
{
    double output = 0;
    CTR_Code status = ((CanTalonSRX*)handle)->GetFgain(slotIdx, output);
    CheckCTRStatus(env, status);

    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetIzone
  (JNIEnv *env, jclass, jlong handle, jint slotIdx)
{
    int output = 0;
    CTR_Code status = ((CanTalonSRX*)handle)->GetIzone(slotIdx, output);
    CheckCTRStatus(env, status);

    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetCloseLoopRampRate
  (JNIEnv *env, jclass, jlong handle, jint slotIdx)
{
    int output = 0;
    CTR_Code status = ((CanTalonSRX*)handle)->GetCloseLoopRampRate(slotIdx, output);
    CheckCTRStatus(env, status);

    return output;
}

JNIEXPORT jdouble JNICALL Java_com_ctre_CanTalonJNI_GetVoltageCompensationRate
  (JNIEnv *env, jclass, jlong handle)
{
    double output = 0;
    CTR_Code status = ((CanTalonSRX*)handle)->GetVoltageCompensationRate(output);
    CheckCTRStatus(env, status);

    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetForwardSoftLimit
  (JNIEnv *env, jclass, jlong handle)
{
    int output = 0;
    CTR_Code status = ((CanTalonSRX*)handle)->GetForwardSoftLimit(output);
    CheckCTRStatus(env, status);

    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetReverseSoftLimit
  (JNIEnv *env, jclass, jlong handle)
{
    int output = 0;
    CTR_Code status = ((CanTalonSRX*)handle)->GetReverseSoftLimit(output);
    CheckCTRStatus(env, status);

    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetForwardSoftEnable
  (JNIEnv *env, jclass, jlong handle)
{
    int output = 0;
    CTR_Code status = ((CanTalonSRX*)handle)->GetForwardSoftEnable(output);
    CheckCTRStatus(env, status);

    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetReverseSoftEnable
  (JNIEnv *env, jclass, jlong handle)
{
    int output = 0;
    CTR_Code status = ((CanTalonSRX*)handle)->GetReverseSoftEnable(output);
    CheckCTRStatus(env, status);

    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetPulseWidthRiseToFallUs
  (JNIEnv *env, jclass, jlong handle)
{
    int output = 0;
    CTR_Code status = ((CanTalonSRX*)handle)->GetPulseWidthRiseToFallUs(output);
    CheckCTRStatus(env, status);

    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_IsPulseWidthSensorPresent
  (JNIEnv *env, jclass, jlong handle)
{
    int output = 0;
    CTR_Code status = ((CanTalonSRX*)handle)->IsPulseWidthSensorPresent(output);
    CheckCTRStatus(env, status);

    return output;
}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_SetModeSelect2
  (JNIEnv *env, jclass, jlong handle, jint modeSelect, jint demand)
{
//    CTR_Code status = ((CanTalonSRX*)handle)->SetModeSelect2(modeSelect, demand);
//    CheckCTRStatus(env, status);

    LOG_UNSUPPORTED();

}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_SetStatusFrameRate
  (JNIEnv *env, jclass, jlong handle, jint frameEnum, jint periodMs)
{
    CTR_Code status = ((CanTalonSRX*)handle)->SetStatusFrameRate(frameEnum, periodMs);
    CheckCTRStatus(env, status);

}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_ClearStickyFaults
  (JNIEnv *env, jclass, jlong handle)
{
    CTR_Code status = ((CanTalonSRX*)handle)->ClearStickyFaults();
    CheckCTRStatus(env, status);

}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_ChangeMotionControlFramePeriod
  (JNIEnv *env, jclass, jlong handle, jint periodMs)
{
    ((CanTalonSRX*)handle)->ChangeMotionControlFramePeriod(periodMs);


}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_ClearMotionProfileTrajectories
  (JNIEnv *env, jclass, jlong handle)
{
    ((CanTalonSRX*)handle)->ClearMotionProfileTrajectories();
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetMotionProfileTopLevelBufferCount
  (JNIEnv *env, jclass, jlong handle)
{
//    return CTR_Code status = ((CanTalonSRX*)handle)->GetMotionProfileTopLevelBufferCount();
//    CheckCTRStatus(env, status);

	LOG_UNSUPPORTED();
    return 0;

}

JNIEXPORT jboolean JNICALL Java_com_ctre_CanTalonJNI_IsMotionProfileTopLevelBufferFull
  (JNIEnv *env, jclass, jlong handle)
{
//    return CTR_Code status = ((CanTalonSRX*)handle)->IsMotionProfileTopLevelBufferFull();
//    CheckCTRStatus(env, status);

	LOG_UNSUPPORTED();
	return false;
}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_PushMotionProfileTrajectory
  (JNIEnv *env, jclass, jlong handle, jint targPos, jint targVel, jint profileSlotSelect, jint timeDurMs, jint velOnly, jint isLastPoint, jint zeroPos)
{
    CTR_Code status = ((CanTalonSRX*)handle)->PushMotionProfileTrajectory(targPos, targVel, profileSlotSelect, timeDurMs, velOnly, isLastPoint, zeroPos);
    CheckCTRStatus(env, status);

}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_ProcessMotionProfileBuffer
  (JNIEnv *env, jclass, jlong handle)
{
    ((CanTalonSRX*)handle)->ProcessMotionProfileBuffer();
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetFault_1OverTemp
  (JNIEnv *env, jclass, jlong)
{
    LOG_UNSUPPORTED();
    return 0;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetFault_1UnderVoltage
  (JNIEnv *env, jclass, jlong)
{
    LOG_UNSUPPORTED();
    return 0;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetFault_1ForLim
  (JNIEnv *env, jclass, jlong)
{
    LOG_UNSUPPORTED();
    return 0;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetFault_1RevLim
  (JNIEnv *env, jclass, jlong)
{
    LOG_UNSUPPORTED();
    return 0;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetFault_1HardwareFailure
  (JNIEnv *env, jclass, jlong)
{
    LOG_UNSUPPORTED();
    return 0;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetFault_1ForSoftLim
  (JNIEnv *env, jclass, jlong)
{
    LOG_UNSUPPORTED();
    return 0;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetFault_1RevSoftLim
  (JNIEnv *env, jclass, jlong)
{
    LOG_UNSUPPORTED();
    return 0;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetStckyFault_1OverTemp
  (JNIEnv *env, jclass, jlong)
{
    LOG_UNSUPPORTED();
    return 0;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetStckyFault_1UnderVoltage
  (JNIEnv *env, jclass, jlong)
{
    LOG_UNSUPPORTED();
    return 0;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetStckyFault_1ForLim
  (JNIEnv *env, jclass, jlong)
{
    LOG_UNSUPPORTED();
    return 0;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetStckyFault_1RevLim
  (JNIEnv *env, jclass, jlong)
{
    LOG_UNSUPPORTED();
    return 0;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetStckyFault_1ForSoftLim
  (JNIEnv *env, jclass, jlong)
{
    LOG_UNSUPPORTED();
    return 0;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetStckyFault_1RevSoftLim
  (JNIEnv *env, jclass, jlong)
{
    LOG_UNSUPPORTED();
    return 0;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetAppliedThrottle
  (JNIEnv *env, jclass, jlong handle)
{
    int output = 0;
    CTR_Code status = ((CanTalonSRX*)handle)->GetAppliedThrottle(output);
    CheckCTRStatus(env, status);

    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetCloseLoopErr
  (JNIEnv *env, jclass, jlong handle)
{
    int output = 0;
    CTR_Code status = ((CanTalonSRX*)handle)->GetCloseLoopErr(output);
    CheckCTRStatus(env, status);

    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetFeedbackDeviceSelect
  (JNIEnv *env, jclass, jlong handle)
{
    int output = 0;
    CTR_Code status = ((CanTalonSRX*)handle)->GetFeedbackDeviceSelect(output);
    CheckCTRStatus(env, status);

    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetModeSelect
  (JNIEnv *env, jclass, jlong handle)
{
    int output = 0;
    CTR_Code status = ((CanTalonSRX*)handle)->GetModeSelect(output);
    CheckCTRStatus(env, status);

    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetLimitSwitchEn
  (JNIEnv *env, jclass, jlong handle)
{
    int output = 0;
    CTR_Code status = ((CanTalonSRX*)handle)->GetLimitSwitchEn(output);
    CheckCTRStatus(env, status);

    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetLimitSwitchClosedFor
  (JNIEnv *env, jclass, jlong handle)
{
    int output = 0;
    CTR_Code status = ((CanTalonSRX*)handle)->GetLimitSwitchClosedFor(output);
    CheckCTRStatus(env, status);

    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetLimitSwitchClosedRev
  (JNIEnv *env, jclass, jlong handle)
{
    int output = 0;
    CTR_Code status = ((CanTalonSRX*)handle)->GetLimitSwitchClosedRev(output);
    CheckCTRStatus(env, status);

    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetSensorPosition
  (JNIEnv *env, jclass, jlong handle)
{
    int output = 0;
    CTR_Code status = ((CanTalonSRX*)handle)->GetSensorPosition(output);
    CheckCTRStatus(env, status);

    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetSensorVelocity
  (JNIEnv *env, jclass, jlong handle)
{
    int output = 0;
    CTR_Code status = ((CanTalonSRX*)handle)->GetSensorVelocity(output);
    CheckCTRStatus(env, status);

    return output;
}

JNIEXPORT jdouble JNICALL Java_com_ctre_CanTalonJNI_GetCurrent
  (JNIEnv *env, jclass, jlong handle)
{
    double output = 0;
    CTR_Code status = ((CanTalonSRX*)handle)->GetCurrent(output);
    CheckCTRStatus(env, status);

    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetBrakeIsEnabled
  (JNIEnv *env, jclass, jlong handle)
{
    int output = 0;
    CTR_Code status = ((CanTalonSRX*)handle)->GetBrakeIsEnabled(output);
    CheckCTRStatus(env, status);

    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetEncPosition
  (JNIEnv *env, jclass, jlong handle)
{
    int output = 0;
    CTR_Code status = ((CanTalonSRX*)handle)->GetEncPosition(output);
    CheckCTRStatus(env, status);

    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetEncVel
  (JNIEnv *env, jclass, jlong handle)
{
    int output = 0;
    CTR_Code status = ((CanTalonSRX*)handle)->GetEncVel(output);
    CheckCTRStatus(env, status);

    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetEncIndexRiseEvents
  (JNIEnv *env, jclass, jlong handle)
{
    int output = 0;
    CTR_Code status = ((CanTalonSRX*)handle)->GetEncIndexRiseEvents(output);
    CheckCTRStatus(env, status);

    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetQuadApin
  (JNIEnv *env, jclass, jlong handle)
{
    int output = 0;
    CTR_Code status = ((CanTalonSRX*)handle)->GetQuadApin(output);
    CheckCTRStatus(env, status);

    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetQuadBpin
  (JNIEnv *env, jclass, jlong handle)
{
    int output = 0;
    CTR_Code status = ((CanTalonSRX*)handle)->GetQuadBpin(output);
    CheckCTRStatus(env, status);

    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetQuadIdxpin
  (JNIEnv *env, jclass, jlong handle)
{
    int output = 0;
    CTR_Code status = ((CanTalonSRX*)handle)->GetQuadIdxpin(output);
    CheckCTRStatus(env, status);

    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetAnalogInWithOv
  (JNIEnv *env, jclass, jlong handle)
{
    int output = 0;
    CTR_Code status = ((CanTalonSRX*)handle)->GetAnalogInWithOv(output);
    CheckCTRStatus(env, status);

    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetAnalogInVel
  (JNIEnv *env, jclass, jlong handle)
{
    int output = 0;
    CTR_Code status = ((CanTalonSRX*)handle)->GetAnalogInVel(output);
    CheckCTRStatus(env, status);

    return output;
}

JNIEXPORT jdouble JNICALL Java_com_ctre_CanTalonJNI_GetTemp
  (JNIEnv *env, jclass, jlong handle)
{
    double output = 0;
    CTR_Code status = ((CanTalonSRX*)handle)->GetTemp(output);
    CheckCTRStatus(env, status);

    return output;
}

JNIEXPORT jdouble JNICALL Java_com_ctre_CanTalonJNI_GetBatteryV
  (JNIEnv *env, jclass, jlong handle)
{
    double output = 0;
    CTR_Code status = ((CanTalonSRX*)handle)->GetBatteryV(output);
    CheckCTRStatus(env, status);

    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetClearPosOnIdx
  (JNIEnv *env, jclass, jlong)
{
    LOG_UNSUPPORTED();
    return 0;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetClearPosOnLimR
  (JNIEnv *env, jclass, jlong)
{
    LOG_UNSUPPORTED();
    return 0;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetClearPosOnLimF
  (JNIEnv *env, jclass, jlong)
{
    LOG_UNSUPPORTED();
    return 0;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetResetCount
  (JNIEnv *env, jclass, jlong handle)
{
    int output = 0;
    CTR_Code status = ((CanTalonSRX*)handle)->GetResetCount(output);
    CheckCTRStatus(env, status);

    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetResetFlags
  (JNIEnv *env, jclass, jlong handle)
{
    int output = 0;
    CTR_Code status = ((CanTalonSRX*)handle)->GetResetFlags(output);
    CheckCTRStatus(env, status);

    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetFirmVers
  (JNIEnv *env, jclass, jlong handle)
{
    int output = 0;
    CTR_Code status = ((CanTalonSRX*)handle)->GetFirmVers(output);
    CheckCTRStatus(env, status);

    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetPulseWidthPosition
  (JNIEnv *env, jclass, jlong handle)
{
    int output = 0;
    CTR_Code status = ((CanTalonSRX*)handle)->GetPulseWidthPosition(output);
    CheckCTRStatus(env, status);

    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetPulseWidthVelocity
  (JNIEnv *env, jclass, jlong handle)
{
    int output = 0;
    CTR_Code status = ((CanTalonSRX*)handle)->GetPulseWidthVelocity(output);
    CheckCTRStatus(env, status);

    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetPulseWidthRiseToRiseUs
  (JNIEnv *env, jclass, jlong handle)
{
    int output = 0;
    CTR_Code status = ((CanTalonSRX*)handle)->GetPulseWidthRiseToRiseUs(output);
    CheckCTRStatus(env, status);

    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetActTraj_1IsValid
  (JNIEnv *env, jclass, jlong)
{
    LOG_UNSUPPORTED();
    return 0;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetActTraj_1ProfileSlotSelect
  (JNIEnv *env, jclass, jlong)
{
    LOG_UNSUPPORTED();
    return 0;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetActTraj_1VelOnly
  (JNIEnv *env, jclass, jlong)
{
    LOG_UNSUPPORTED();
    return 0;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetActTraj_1IsLast
  (JNIEnv *env, jclass, jlong)
{
    LOG_UNSUPPORTED();
    return 0;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetOutputType
  (JNIEnv *env, jclass, jlong handle)
{
    int output = 0;
    CTR_Code status = ((CanTalonSRX*)handle)->GetOutputType(output);
    CheckCTRStatus(env, status);

    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetHasUnderrun
  (JNIEnv *env, jclass, jlong handle)
{
    int output = 0;
    CTR_Code status = ((CanTalonSRX*)handle)->GetHasUnderrun(output);
    CheckCTRStatus(env, status);

    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetIsUnderrun
  (JNIEnv *env, jclass, jlong handle)
{
    int output = 0;
    CTR_Code status = ((CanTalonSRX*)handle)->GetIsUnderrun(output);
    CheckCTRStatus(env, status);

    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetNextID
  (JNIEnv *env, jclass, jlong handle)
{
    int output = 0;
    CTR_Code status = ((CanTalonSRX*)handle)->GetNextID(output);
    CheckCTRStatus(env, status);

    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetBufferIsFull
  (JNIEnv *env, jclass, jlong handle)
{
    int output = 0;
    CTR_Code status = ((CanTalonSRX*)handle)->GetBufferIsFull(output);
    CheckCTRStatus(env, status);

    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetCount
  (JNIEnv *env, jclass, jlong handle)
{
    int output = 0;
    CTR_Code status = ((CanTalonSRX*)handle)->GetCount(output);
    CheckCTRStatus(env, status);

    return output;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetActTraj_1Velocity
  (JNIEnv *env, jclass, jlong)
{
    LOG_UNSUPPORTED();
    return 0;
}

JNIEXPORT jint JNICALL Java_com_ctre_CanTalonJNI_GetActTraj_1Position
  (JNIEnv *env, jclass, jlong)
{
    LOG_UNSUPPORTED();
    return 0;
}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_SetDemand
  (JNIEnv *env, jclass, jlong handle, jint param)
{
    CTR_Code status = ((CanTalonSRX*)handle)->SetDemand(param);
    CheckCTRStatus(env, status);

}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_SetOverrideLimitSwitchEn
  (JNIEnv *env, jclass, jlong handle, jint param)
{
    CTR_Code status = ((CanTalonSRX*)handle)->SetOverrideLimitSwitchEn(param);
    CheckCTRStatus(env, status);

}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_SetFeedbackDeviceSelect
  (JNIEnv *env, jclass, jlong handle, jint param)
{
    CTR_Code status = ((CanTalonSRX*)handle)->SetFeedbackDeviceSelect(param);
    CheckCTRStatus(env, status);

}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_SetRevMotDuringCloseLoopEn
  (JNIEnv *env, jclass, jlong handle, jint param)
{
    CTR_Code status = ((CanTalonSRX*)handle)->SetRevMotDuringCloseLoopEn(param);
    CheckCTRStatus(env, status);

}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_SetOverrideBrakeType
  (JNIEnv *env, jclass, jlong handle, jint param)
{
    CTR_Code status = ((CanTalonSRX*)handle)->SetOverrideBrakeType(param);
    CheckCTRStatus(env, status);

}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_SetModeSelect
  (JNIEnv *env, jclass, jlong handle, jint param)
{
    CTR_Code status = ((CanTalonSRX*)handle)->SetModeSelect(param);
    CheckCTRStatus(env, status);

}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_SetProfileSlotSelect
  (JNIEnv *env, jclass, jlong handle, jint param)
{
    CTR_Code status = ((CanTalonSRX*)handle)->SetProfileSlotSelect(param);
    CheckCTRStatus(env, status);

}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_SetRampThrottle
  (JNIEnv *env, jclass, jlong handle, jint param)
{
    CTR_Code status = ((CanTalonSRX*)handle)->SetRampThrottle(param);
    CheckCTRStatus(env, status);

}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_SetRevFeedbackSensor
  (JNIEnv *env, jclass, jlong handle, jint param)
{
    CTR_Code status = ((CanTalonSRX*)handle)->SetRevFeedbackSensor(param);
    CheckCTRStatus(env, status);

}

JNIEXPORT void JNICALL Java_com_ctre_CanTalonJNI_SetCurrentLimEnable
  (JNIEnv *env, jclass, jlong, jboolean)
{
    LOG_UNSUPPORTED();
}


}

