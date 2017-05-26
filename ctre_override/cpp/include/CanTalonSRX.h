/**
 * @brief CAN TALON SRX driver.
 *
 * The TALON SRX is designed to instrument all runtime signals periodically.
 * The default periods are chosen to support 16 TALONs with 10ms update rate
 * for control (throttle or setpoint).  However these can be overridden with
 * SetStatusFrameRate. @see SetStatusFrameRate
 * The getters for these unsolicited signals are auto generated at the bottom
 * of this module.
 *
 * Likewise most control signals are sent periodically using the fire-and-forget
 * CAN API.  The setters for these unsolicited signals are auto generated at the
 * bottom of this module.
 *
 * Signals that are not available in an unsolicited fashion are the Close Loop
 * gains.  For teams that have a single profile for their TALON close loop they
 * can use either the webpage to configure their TALONs once or set the PIDF,
 * Izone, CloseLoopRampRate, etc... once in the robot application.  These
 * parameters are saved to flash so once they are loaded in the TALON, they
 * will persist through power cycles and mode changes.
 *
 * For teams that have one or two profiles to switch between, they can use the
 * same strategy since there are two slots to choose from and the
 * ProfileSlotSelect is periodically sent in the 10 ms control frame.
 *
 * For teams that require changing gains frequently, they can use the soliciting
 * API to get and set those parameters.  Most likely they will only need to set
 * them in a periodic fashion as a function of what motion the application is
 * attempting.  If this API is used, be mindful of the CAN utilization reported
 * in the driver station.
 *
 * If calling application has used the config routines to configure the
 * selected feedback sensor, then all positions are measured in floating point
 * precision rotations.  All sensor velocities are specified in floating point
 * precision RPM.
 * @see ConfigPotentiometerTurns
 * @see ConfigEncoderCodesPerRev
 * HOWEVER, if calling application has not called the config routine for
 * selected feedback sensor, then all getters/setters for position/velocity use
 * the native engineering units of the Talon SRX firm (just like in 2015).
 * Signals explained below.
 *
 * Encoder position is measured in encoder edges.  Every edge is counted
 * (similar to roboRIO 4X mode).  Analog position is 10 bits, meaning 1024
 * ticks per rotation (0V => 3.3V).  Use SetFeedbackDeviceSelect to select
 * which sensor type you need.  Once you do that you can use GetSensorPosition()
 * and GetSensorVelocity().  These signals are updated on CANBus every 20ms (by
 * default).  If a relative sensor is selected, you can zero (or change the
 * current value) using SetSensorPosition.
 *
 * Analog Input and quadrature position (and velocity) are also explicitly
 * reported in GetEncPosition, GetEncVel, GetAnalogInWithOv, GetAnalogInVel.
 * These signals are available all the time, regardless of what sensor is
 * selected at a rate of 100ms.  This allows easy instrumentation for "in the
 * pits" checking of all sensors regardless of modeselect.  The 100ms rate is
 * overridable for teams who want to acquire sensor data for processing, not
 * just instrumentation.  Or just select the sensor using
 * SetFeedbackDeviceSelect to get it at 20ms.
 *
 * Velocity is in position ticks / 100ms.
 *
 * All output units are in respect to duty cycle (throttle) which is -1023(full
 * reverse) to +1023 (full forward).  This includes demand (which specifies
 * duty cycle when in duty cycle mode) and rampRamp, which is in throttle units
 * per 10ms (if nonzero).
 *
 * Pos and velocity close loops are calc'd as
 *   err = target - posOrVel.
 *   iErr += err;
 *   if(   (IZone!=0)  and  abs(err) > IZone)
 *       ClearIaccum()
 *   output = P X err + I X iErr + D X dErr + F X target
 *   dErr = err - lastErr
 * P, I, and D gains are always positive. F can be negative.
 * Motor direction can be reversed using SetRevMotDuringCloseLoopEn if
 * sensor and motor are out of phase. Similarly feedback sensor can also be
 * reversed (multiplied by -1) if you prefer the sensor to be inverted.
 *
 * P gain is specified in throttle per error tick.  For example, a value of 102
 * is ~9.9% (which is 102/1023) throttle per 1 ADC unit(10bit) or 1 quadrature
 * encoder edge depending on selected sensor.
 *
 * I gain is specified in throttle per integrated error. For example, a value
 * of 10 equates to ~0.99% (which is 10/1023) for each accumulated ADC unit
 * (10 bit) or 1 quadrature encoder edge depending on selected sensor.
 * Close loop and integral accumulator runs every 1ms.
 *
 * D gain is specified in throttle per derivative error. For example a value of
 * 102 equates to ~9.9% (which is 102/1023) per change of 1 unit (ADC or
 * encoder) per ms.
 *
 * I Zone is specified in the same units as sensor position (ADC units or
 * quadrature edges).  If pos/vel error is outside of this value, the
 * integrated error will auto-clear...
 *   if(   (IZone!=0)  and  abs(err) > IZone)
 *       ClearIaccum()
 * ...this is very useful in preventing integral windup and is highly
 * recommended if using full PID to keep stability low.
 *
 * CloseLoopRampRate is in throttle units per 1ms.  Set to zero to disable
 * ramping.  Works the same as RampThrottle but only is in effect when a close
 * loop mode and profile slot is selected.
 *
 * auto generated using spreadsheet and wpiclassgen.py
 * @link https://docs.google.com/spreadsheets/d/1OU_ZV7fZLGYUQ-Uhc8sVAmUmWTlT8XBFYK8lfjg_tac/edit#gid=1766046967
 */
#ifndef CanTalonSRX_H_
#define CanTalonSRX_H_

#include "ctre/ctre.h"  //BIT Defines + Typedefs, TALON_Control_6_MotProfAddTrajPoint_t
#include "ctre/CtreCanNode.h"
#include "ctre/structs_mtrCntrl.h"
#include "GadgeteerUartClient.h"
#include <FRC_NetworkCommunication/CANSessionMux.h>  //CAN Comm
#include <map>
#include <atomic>
#include <deque>
#include <mutex>


extern "C" {
  void *c_TalonSRX_Create3(int deviceNumber, int controlPeriodMs, int enablePeriodMs);
  void *c_TalonSRX_Create2(int deviceNumber, int controlPeriodMs);
  void *c_TalonSRX_Create1(int deviceNumber);
  void c_TalonSRX_Destroy(void *handle);
  void c_TalonSRX_Set(void *handle, double value);
  CTR_Code c_TalonSRX_SetParam(void *handle, int paramEnum, double value);
  CTR_Code c_TalonSRX_RequestParam(void *handle, int paramEnum);
  CTR_Code c_TalonSRX_GetParamResponse(void *handle, int paramEnum, double *value);
  CTR_Code c_TalonSRX_GetParamResponseInt32(void *handle, int paramEnum, int *value);
  CTR_Code c_TalonSRX_SetPgain(void *handle, int slotIdx, double gain);
  CTR_Code c_TalonSRX_SetIgain(void *handle, int slotIdx, double gain);
  CTR_Code c_TalonSRX_SetDgain(void *handle, int slotIdx, double gain);
  CTR_Code c_TalonSRX_SetFgain(void *handle, int slotIdx, double gain);
  CTR_Code c_TalonSRX_SetIzone(void *handle, int slotIdx, int zone);
  CTR_Code c_TalonSRX_SetCloseLoopRampRate(void *handle, int slotIdx, int closeLoopRampRate);
  CTR_Code c_TalonSRX_SetVoltageCompensationRate(void *handle, double voltagePerMs);
  CTR_Code c_TalonSRX_SetSensorPosition(void *handle, int pos);
  CTR_Code c_TalonSRX_SetForwardSoftLimit(void *handle, int forwardLimit);
  CTR_Code c_TalonSRX_SetReverseSoftLimit(void *handle, int reverseLimit);
  CTR_Code c_TalonSRX_SetForwardSoftEnable(void *handle, int enable);
  CTR_Code c_TalonSRX_SetReverseSoftEnable(void *handle, int enable);
  CTR_Code c_TalonSRX_GetPgain(void *handle, int slotIdx, double *gain);
  CTR_Code c_TalonSRX_GetIgain(void *handle, int slotIdx, double *gain);
  CTR_Code c_TalonSRX_GetDgain(void *handle, int slotIdx, double *gain);
  CTR_Code c_TalonSRX_GetFgain(void *handle, int slotIdx, double *gain);
  CTR_Code c_TalonSRX_GetIzone(void *handle, int slotIdx, int *zone);
  CTR_Code c_TalonSRX_GetCloseLoopRampRate(void *handle, int slotIdx, int *closeLoopRampRate);
  CTR_Code c_TalonSRX_GetVoltageCompensationRate(void *handle, double *voltagePerMs);
  CTR_Code c_TalonSRX_GetForwardSoftLimit(void *handle, int *forwardLimit);
  CTR_Code c_TalonSRX_GetReverseSoftLimit(void *handle, int *reverseLimit);
  CTR_Code c_TalonSRX_GetForwardSoftEnable(void *handle, int *enable);
  CTR_Code c_TalonSRX_GetReverseSoftEnable(void *handle, int *enable);
  CTR_Code c_TalonSRX_GetPulseWidthRiseToFallUs(void *handle, int *param);
  CTR_Code c_TalonSRX_IsPulseWidthSensorPresent(void *handle, int *param);
  CTR_Code c_TalonSRX_SetModeSelect2(void *handle, int modeSelect, int demand);
  CTR_Code c_TalonSRX_SetStatusFrameRate(void *handle, int frameEnum, int periodMs);
  CTR_Code c_TalonSRX_ClearStickyFaults(void *handle);
  void c_TalonSRX_ChangeMotionControlFramePeriod(void *handle, int periodMs);
  void c_TalonSRX_ClearMotionProfileTrajectories(void *handle);
  int c_TalonSRX_GetMotionProfileTopLevelBufferCount(void *handle);
  int c_TalonSRX_IsMotionProfileTopLevelBufferFull(void *handle);
  CTR_Code c_TalonSRX_PushMotionProfileTrajectory(void *handle, int targPos, int targVel, int profileSlotSelect, int timeDurMs, int velOnly, int isLastPoint, int zeroPos);
  void c_TalonSRX_ProcessMotionProfileBuffer(void *handle);
  CTR_Code c_TalonSRX_GetMotionProfileStatus(void *handle, int *flags, int *profileSlotSelect, int *targPos, int *targVel, int *topBufferRemaining, int *topBufferCnt, int *btmBufferCnt, int *outputEnable);
  CTR_Code c_TalonSRX_GetFault_OverTemp(void *handle, int *param);
  CTR_Code c_TalonSRX_GetFault_UnderVoltage(void *handle, int *param);
  CTR_Code c_TalonSRX_GetFault_ForLim(void *handle, int *param);
  CTR_Code c_TalonSRX_GetFault_RevLim(void *handle, int *param);
  CTR_Code c_TalonSRX_GetFault_HardwareFailure(void *handle, int *param);
  CTR_Code c_TalonSRX_GetFault_ForSoftLim(void *handle, int *param);
  CTR_Code c_TalonSRX_GetFault_RevSoftLim(void *handle, int *param);
  CTR_Code c_TalonSRX_GetStckyFault_OverTemp(void *handle, int *param);
  CTR_Code c_TalonSRX_GetStckyFault_UnderVoltage(void *handle, int *param);
  CTR_Code c_TalonSRX_GetStckyFault_ForLim(void *handle, int *param);
  CTR_Code c_TalonSRX_GetStckyFault_RevLim(void *handle, int *param);
  CTR_Code c_TalonSRX_GetStckyFault_ForSoftLim(void *handle, int *param);
  CTR_Code c_TalonSRX_GetStckyFault_RevSoftLim(void *handle, int *param);
  CTR_Code c_TalonSRX_GetAppliedThrottle(void *handle, int *param);
  CTR_Code c_TalonSRX_GetCloseLoopErr(void *handle, int *param);
  CTR_Code c_TalonSRX_GetFeedbackDeviceSelect(void *handle, int *param);
  CTR_Code c_TalonSRX_GetModeSelect(void *handle, int *param);
  CTR_Code c_TalonSRX_GetLimitSwitchEn(void *handle, int *param);
  CTR_Code c_TalonSRX_GetLimitSwitchClosedFor(void *handle, int *param);
  CTR_Code c_TalonSRX_GetLimitSwitchClosedRev(void *handle, int *param);
  CTR_Code c_TalonSRX_GetSensorPosition(void *handle, int *param);
  CTR_Code c_TalonSRX_GetSensorVelocity(void *handle, int *param);
  CTR_Code c_TalonSRX_GetCurrent(void *handle, double *param);
  CTR_Code c_TalonSRX_GetBrakeIsEnabled(void *handle, int *param);
  CTR_Code c_TalonSRX_GetEncPosition(void *handle, int *param);
  CTR_Code c_TalonSRX_GetEncVel(void *handle, int *param);
  CTR_Code c_TalonSRX_GetEncIndexRiseEvents(void *handle, int *param);
  CTR_Code c_TalonSRX_GetQuadApin(void *handle, int *param);
  CTR_Code c_TalonSRX_GetQuadBpin(void *handle, int *param);
  CTR_Code c_TalonSRX_GetQuadIdxpin(void *handle, int *param);
  CTR_Code c_TalonSRX_GetAnalogInWithOv(void *handle, int *param);
  CTR_Code c_TalonSRX_GetAnalogInVel(void *handle, int *param);
  CTR_Code c_TalonSRX_GetTemp(void *handle, double *param);
  CTR_Code c_TalonSRX_GetBatteryV(void *handle, double *param);
  CTR_Code c_TalonSRX_GetResetCount(void *handle, int *param);
  CTR_Code c_TalonSRX_GetResetFlags(void *handle, int *param);
  CTR_Code c_TalonSRX_GetFirmVers(void *handle, int *param);
  CTR_Code c_TalonSRX_GetPulseWidthPosition(void *handle, int *param);
  CTR_Code c_TalonSRX_GetPulseWidthVelocity(void *handle, int *param);
  CTR_Code c_TalonSRX_GetPulseWidthRiseToRiseUs(void *handle, int *param);
  CTR_Code c_TalonSRX_GetActTraj_IsValid(void *handle, int *param);
  CTR_Code c_TalonSRX_GetActTraj_ProfileSlotSelect(void *handle, int *param);
  CTR_Code c_TalonSRX_GetActTraj_VelOnly(void *handle, int *param);
  CTR_Code c_TalonSRX_GetActTraj_IsLast(void *handle, int *param);
  CTR_Code c_TalonSRX_GetOutputType(void *handle, int *param);
  CTR_Code c_TalonSRX_GetHasUnderrun(void *handle, int *param);
  CTR_Code c_TalonSRX_GetIsUnderrun(void *handle, int *param);
  CTR_Code c_TalonSRX_GetNextID(void *handle, int *param);
  CTR_Code c_TalonSRX_GetBufferIsFull(void *handle, int *param);
  CTR_Code c_TalonSRX_GetCount(void *handle, int *param);
  CTR_Code c_TalonSRX_GetActTraj_Velocity(void *handle, int *param);
  CTR_Code c_TalonSRX_GetActTraj_Position(void *handle, int *param);
  CTR_Code c_TalonSRX_SetDemand(void *handle, int param);
  CTR_Code c_TalonSRX_SetOverrideLimitSwitchEn(void *handle, int param);
  CTR_Code c_TalonSRX_SetFeedbackDeviceSelect(void *handle, int param);
  CTR_Code c_TalonSRX_SetRevMotDuringCloseLoopEn(void *handle, int param);
  CTR_Code c_TalonSRX_SetOverrideBrakeType(void *handle, int param);
  CTR_Code c_TalonSRX_SetModeSelect(void *handle, int param);
  CTR_Code c_TalonSRX_SetProfileSlotSelect(void *handle, int param);
  CTR_Code c_TalonSRX_SetRampThrottle(void *handle, int param);
  CTR_Code c_TalonSRX_SetRevFeedbackSensor(void *handle, int param);
}
#endif
