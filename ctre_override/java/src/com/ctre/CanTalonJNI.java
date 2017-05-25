package com.ctre;

    
    
public class CanTalonJNI
        extends CTREJNIWrapper
{
    public static final int kMotionProfileFlag_ActTraj_IsValid = 1;

    public static final int kMotionProfileFlag_HasUnderrun = 2;

    public static final int kMotionProfileFlag_IsUnderrun = 4;

    public static final int kMotionProfileFlag_ActTraj_IsLast = 8;

    public static final int kMotionProfileFlag_ActTraj_VelOnly = 16;

    public static String ERR_CANSessionMux_InvalidBuffer_MESSAGE = "CAN: Invalid Buffer";
    public static String ERR_CANSessionMux_MessageNotFound_MESSAGE = "CAN: Message not found";
    public static String WARN_CANSessionMux_NoToken_MESSAGE = "CAN: No token";
    public static String ERR_CANSessionMux_NotAllowed_MESSAGE = "CAN: Not allowed";
    public static String ERR_CANSessionMux_NotInitialized_MESSAGE = "CAN: Not initialized";
    public static String CTR_RxTimeout_MESSAGE = "CTRE CAN Receive Timeout";
    public static String CTR_TxTimeout_MESSAGE = "CTRE CAN Transmit Timeout";
    public static String CTR_InvalidParamValue_MESSAGE = "CTRE CAN Invalid Parameter";
    public static String CTR_UnexpectedArbId_MESSAGE = "CTRE Unexpected Arbitration ID (CAN Node ID)";
    public static String CTR_TxFailed_MESSAGE = "CTRE CAN Transmit Error";
    public static String CTR_SigNotUpdated_MESSAGE = "CTRE CAN Signal Not Updated";
    public static final int CTR_RxTimeout = 1;
    public static final int CTR_TxTimeout = 2;
    public static final int CTR_InvalidParamValue = 3;
    public static final int CTR_UnexpectedArbId = 4;
    public static final int CTR_TxFailed = 5;
    public static final int CTR_SigNotUpdated = 6;
    public static final int ERR_CANSessionMux_InvalidBuffer = -44086;
    public static final int ERR_CANSessionMux_MessageNotFound = -44087;
    public static final int WARN_CANSessionMux_NoToken = 44087;
    public static final int ERR_CANSessionMux_NotAllowed = -44088;

    public CanTalonJNI()
    {
    }

    public static native long new_CanTalonSRX(int paramInt1, int paramInt2, int paramInt3);

    public static native long new_CanTalonSRX(int paramInt1, int paramInt2);
  
  public static enum param_t
  {
    eProfileParamSlot0_P(1), 
    eProfileParamSlot0_I(2), 
    eProfileParamSlot0_D(3), 
    eProfileParamSlot0_F(4), 
    eProfileParamSlot0_IZone(5), 
    eProfileParamSlot0_CloseLoopRampRate(6), 
    eProfileParamSlot1_P(11), 
    eProfileParamSlot1_I(12), 
    eProfileParamSlot1_D(13), 
    eProfileParamSlot1_F(14), 
    eProfileParamSlot1_IZone(15), 
    eProfileParamSlot1_CloseLoopRampRate(16), 
    eProfileParamSoftLimitForThreshold(21), 
    eProfileParamSoftLimitRevThreshold(22), 
    eProfileParamSoftLimitForEnable(23), 
    eProfileParamSoftLimitRevEnable(24), 
    eOnBoot_BrakeMode(31), 
    eOnBoot_LimitSwitch_Forward_NormallyClosed(32), 
    eOnBoot_LimitSwitch_Reverse_NormallyClosed(33), 
    eOnBoot_LimitSwitch_Forward_Disable(34), 
    eOnBoot_LimitSwitch_Reverse_Disable(35), 
    eFault_OverTemp(41), 
    eFault_UnderVoltage(42), 
    eFault_ForLim(43), 
    eFault_RevLim(44), 
    eFault_HardwareFailure(45), 
    eFault_ForSoftLim(46), 
    eFault_RevSoftLim(47), 
    eStckyFault_OverTemp(48), 
    eStckyFault_UnderVoltage(49), 
    eStckyFault_ForLim(50), 
    eStckyFault_RevLim(51), 
    eStckyFault_ForSoftLim(52), 
    eStckyFault_RevSoftLim(53), 
    eAppliedThrottle(61), 
    eCloseLoopErr(62), 
    eFeedbackDeviceSelect(63), 
    eRevMotDuringCloseLoopEn(64), 
    eModeSelect(65), 
    eProfileSlotSelect(66), 
    eRampThrottle(67), 
    eRevFeedbackSensor(68), 
    eLimitSwitchEn(69), 
    eLimitSwitchClosedFor(70), 
    eLimitSwitchClosedRev(71), 
    eSensorPosition(73), 
    eSensorVelocity(74), 
    eCurrent(75), 
    eBrakeIsEnabled(76), 
    eEncPosition(77), 
    eEncVel(78), 
    eEncIndexRiseEvents(79), 
    eQuadApin(80), 
    eQuadBpin(81), 
    eQuadIdxpin(82), 
    eAnalogInWithOv(83), 
    eAnalogInVel(84), 
    eTemp(85), 
    eBatteryV(86), 
    eResetCount(87), 
    eResetFlags(88), 
    eFirmVers(89), 
    eSettingsChanged(90), 
    eQuadFilterEn(91), 
    ePidIaccum(93), 
    eStatus1FrameRate(94), 
    eStatus2FrameRate(95), 
    eStatus3FrameRate(96), 
    eStatus4FrameRate(97), 
    eStatus6FrameRate(98), 
    eStatus7FrameRate(99), 
    eClearPositionOnIdx(100), 
    


    ePeakPosOutput(104), 
    eNominalPosOutput(105), 
    ePeakNegOutput(106), 
    eNominalNegOutput(107), 
    eQuadIdxPolarity(108), 
    eStatus8FrameRate(109), 
    eAllowPosOverflow(110), 
    eProfileParamSlot0_AllowableClosedLoopErr(111), 
    eNumberPotTurns(112), 
    eNumberEncoderCPR(113), 
    ePwdPosition(114), 
    eAinPosition(115), 
    eProfileParamVcompRate(116), 
    eProfileParamSlot1_AllowableClosedLoopErr(117), 
    eStatus9FrameRate(118), 
    eMotionProfileHasUnderrunErr(119), 
    eReserved120(120), 
    eLegacyControlMode(121), 
    eMotMag_Accel(122), 
    eMotMag_VelCruise(123), 
    eStatus10FrameRate(124), 
    eCurrentLimThreshold(125), 
    
    eBldcStatus1FrameRate(129), 
    eBldcStatus2FrameRate(130), 
    eBldcStatus3FrameRate(131), 
    
    eCustomParam0(137), 
    eCustomParam1(138), 
    ePersStorageSaving(139), 
    eClearPositionOnLimitF(144), 
    eClearPositionOnLimitR(145), 
    eMotionMeas_YawOffset(160), 
    eMotionMeas_CompassOffset(161), 
    eMotionMeas_BetaGain(162), 
    eMotionMeas_Reserved163(163), 
    eMotionMeas_GyroNoMotionCal(164), 
    eMotionMeas_EnterCalibration(165), 
    eMotionMeas_FusedHeadingOffset(166);
    

        public final int value;
    
        private param_t(int value)
        {
            this.value = value;
        }
    }
  
    public static native long new_CanTalonSRX(int paramInt);

    public static native long new_CanTalonSRX();

    public static native void delete_CanTalonSRX(long paramLong);

    public static native void GetMotionProfileStatus(long paramLong, Object paramObject1, Object paramObject2);

    public static native void Set(long paramLong, double paramDouble);

    public static native void SetParam(long paramLong, int paramInt, double paramDouble);

    public static native void RequestParam(long paramLong, int paramInt);

    public static native double GetParamResponse(long paramLong, int paramInt);

    public static native int GetParamResponseInt32(long paramLong, int paramInt);

    public static native void SetPgain(long paramLong, int paramInt, double paramDouble);

    public static native void SetIgain(long paramLong, int paramInt, double paramDouble);

    public static native void SetDgain(long paramLong, int paramInt, double paramDouble);

    public static native void SetFgain(long paramLong, int paramInt, double paramDouble);

    public static native void SetIzone(long paramLong, int paramInt1, int paramInt2);

    public static native void SetCloseLoopRampRate(long paramLong, int paramInt1, int paramInt2);

    public static native void SetVoltageCompensationRate(long paramLong, double paramDouble);

    public static native void SetSensorPosition(long paramLong, int paramInt);

    public static native void SetForwardSoftLimit(long paramLong, int paramInt);

    public static native void SetReverseSoftLimit(long paramLong, int paramInt);

    public static native void SetForwardSoftEnable(long paramLong, int paramInt);

    public static native void SetReverseSoftEnable(long paramLong, int paramInt);

    public static native double GetPgain(long paramLong, int paramInt);

    public static native double GetIgain(long paramLong, int paramInt);

    public static native double GetDgain(long paramLong, int paramInt);

    public static native double GetFgain(long paramLong, int paramInt);

    public static native int GetIzone(long paramLong, int paramInt);

    public static native int GetCloseLoopRampRate(long paramLong, int paramInt);

    public static native double GetVoltageCompensationRate(long paramLong);

    public static native int GetForwardSoftLimit(long paramLong);

    public static native int GetReverseSoftLimit(long paramLong);

    public static native int GetForwardSoftEnable(long paramLong);

    public static native int GetReverseSoftEnable(long paramLong);

    public static native int GetPulseWidthRiseToFallUs(long paramLong);

    public static native int IsPulseWidthSensorPresent(long paramLong);

    public static native void SetModeSelect2(long paramLong, int paramInt1, int paramInt2);

    public static native void SetStatusFrameRate(long paramLong, int paramInt1, int paramInt2);

    public static native void ClearStickyFaults(long paramLong);

    public static native void ChangeMotionControlFramePeriod(long paramLong, int paramInt);

    public static native void ClearMotionProfileTrajectories(long paramLong);

    public static native int GetMotionProfileTopLevelBufferCount(long paramLong);

    public static native boolean IsMotionProfileTopLevelBufferFull(long paramLong);

    public static native void PushMotionProfileTrajectory(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5,
            int paramInt6, int paramInt7);

    public static native void ProcessMotionProfileBuffer(long paramLong);

    public static native int GetFault_OverTemp(long paramLong);

    public static native int GetFault_UnderVoltage(long paramLong);

    public static native int GetFault_ForLim(long paramLong);

    public static native int GetFault_RevLim(long paramLong);

    public static native int GetFault_HardwareFailure(long paramLong);

    public static native int GetFault_ForSoftLim(long paramLong);

    public static native int GetFault_RevSoftLim(long paramLong);

    public static native int GetStckyFault_OverTemp(long paramLong);

    public static native int GetStckyFault_UnderVoltage(long paramLong);

    public static native int GetStckyFault_ForLim(long paramLong);

    public static native int GetStckyFault_RevLim(long paramLong);

    public static native int GetStckyFault_ForSoftLim(long paramLong);

    public static native int GetStckyFault_RevSoftLim(long paramLong);

    public static native int GetAppliedThrottle(long paramLong);

    public static native int GetCloseLoopErr(long paramLong);

    public static native int GetFeedbackDeviceSelect(long paramLong);

    public static native int GetModeSelect(long paramLong);

    public static native int GetLimitSwitchEn(long paramLong);

    public static native int GetLimitSwitchClosedFor(long paramLong);

    public static native int GetLimitSwitchClosedRev(long paramLong);

    public static native int GetClearPosOnIdx(long paramLong);

    public static native int GetClearPosOnLimR(long paramLong);

    public static native int GetClearPosOnLimF(long paramLong);

    public static native int GetSensorPosition(long paramLong);

    public static native int GetSensorVelocity(long paramLong);

    public static native double GetCurrent(long paramLong);

    public static native int GetBrakeIsEnabled(long paramLong);

    public static native int GetEncPosition(long paramLong);

    public static native int GetEncVel(long paramLong);

    public static native int GetEncIndexRiseEvents(long paramLong);

    public static native int GetQuadApin(long paramLong);

    public static native int GetQuadBpin(long paramLong);

    public static native int GetQuadIdxpin(long paramLong);

    public static native int GetAnalogInWithOv(long paramLong);

    public static native int GetAnalogInVel(long paramLong);

    public static native double GetTemp(long paramLong);

    public static native double GetBatteryV(long paramLong);

    public static native int GetResetCount(long paramLong);

    public static native int GetResetFlags(long paramLong);

    public static native int GetFirmVers(long paramLong);

    public static native int GetPulseWidthPosition(long paramLong);

    public static native int GetPulseWidthVelocity(long paramLong);

    public static native int GetPulseWidthRiseToRiseUs(long paramLong);

    public static native int GetActTraj_IsValid(long paramLong);

    public static native int GetActTraj_ProfileSlotSelect(long paramLong);

    public static native int GetActTraj_VelOnly(long paramLong);

    public static native int GetActTraj_IsLast(long paramLong);

    public static native int GetOutputType(long paramLong);

    public static native int GetHasUnderrun(long paramLong);

    public static native int GetIsUnderrun(long paramLong);

    public static native int GetNextID(long paramLong);

    public static native int GetBufferIsFull(long paramLong);

    public static native int GetCount(long paramLong);

    public static native int GetActTraj_Velocity(long paramLong);

    public static native int GetActTraj_Position(long paramLong);

    public static native void SetDemand(long paramLong, int paramInt);

    public static native void SetOverrideLimitSwitchEn(long paramLong, int paramInt);

    public static native void SetFeedbackDeviceSelect(long paramLong, int paramInt);

    public static native void SetRevMotDuringCloseLoopEn(long paramLong, int paramInt);

    public static native void SetOverrideBrakeType(long paramLong, int paramInt);

    public static native void SetModeSelect(long paramLong, int paramInt);

    public static native void SetProfileSlotSelect(long paramLong, int paramInt);

    public static native void SetRampThrottle(long paramLong, int paramInt);

    public static native void SetRevFeedbackSensor(long paramLong, int paramInt);

    public static native void SetCurrentLimEnable(long paramLong, boolean paramBoolean);

    public static native int GetLastError(long paramLong);
}
