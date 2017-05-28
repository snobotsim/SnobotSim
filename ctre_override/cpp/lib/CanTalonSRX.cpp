#include "CanTalonSRX.h"
#include <iostream>

#include "CanTalonSpeedController.h"
#include "SnobotSim/SensorActuatorRegistry.h"



int GetHandle(void* handle)
{
    return (int) *((long*)handle);
}

int GetEncoderHandle(int talonHandle)
{
    return talonHandle | 0xF0000000;
}

int GetEncoderHandle(void* talonHandlePtr)
{
    int talonHandle = GetHandle(talonHandlePtr);
    return GetEncoderHandle(talonHandle);
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





CanTalonSRX::CanTalonSRX(int deviceNumber, int controlPeriodMs,
                         int enablePeriodMs):
	 mDeviceNumber(deviceNumber)
{
    std::shared_ptr<SpeedControllerWrapper> speedController(new CanTalonSpeedController(deviceNumber));
    SensorActuatorRegistry::Get().Register(deviceNumber, speedController);
}
/* CanTalonSRX D'tor
 */
CanTalonSRX::~CanTalonSRX() {

}
/**
 * @return true if Talon is reporting that it supports control5, and therefore
 *         RIO can send control5 to update control params (even when disabled).
 */
bool CanTalonSRX::IsControl5Supported() {
  return true;
}
/**
 * Get a copy of the control frame to send.
 * @param [out] pointer to eight byte array to fill.
 */
void CanTalonSRX::GetControlFrameCopy(uint8_t *toFill) {
}
/**
 * Called in various places to double check we are using the best control frame.
 * If the Talon firmware is too old, use control 1 framing, which does not allow
 * setting
 * control signals until robot is enabled.  If Talon firmware can suport
 * control5, use that
 * since that frame can be transmitted during robot-disable.  If calling
 * application
 * uses setParam to set the signal eLegacyControlMode, caller can force using
 * control1
 * if needed for some reason.
 */
void CanTalonSRX::UpdateControlId() {

}
void CanTalonSRX::OpenSessionIfNeedBe() {

}
void CanTalonSRX::ProcessStreamMessages() {
}
void CanTalonSRX::Set(double value) {
    std::shared_ptr<CanTalonSpeedController> speedController = GetCanTalon(mDeviceNumber);
    speedController->SmartSet(value);
}
/*---------------------setters and getters that use the param
 * request/response-------------*/
/**
 * Send a one shot frame to set an arbitrary signal.
 * Most signals are in the control frame so avoid using this API unless you have
 * to.
 * Use this api for...
 * -A motor controller profile signal eProfileParam_XXXs.  These are backed up
 * in flash.  If you are gain-scheduling then call this periodically.
 * -Default brake and limit switch signals... eOnBoot_XXXs.  Avoid doing this,
 * use the override signals in the control frame.
 * Talon will automatically send a PARAM_RESPONSE after the set, so
 * GetParamResponse will catch the latest value after a couple ms.
 */
CTR_Code CanTalonSRX::SetParamRaw(unsigned paramEnum, int rawBits) {
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
/**
 * Checks cached CAN frames and updating solicited signals.
 */
CTR_Code CanTalonSRX::GetParamResponseRaw(unsigned paramEnum, int &rawBits) {
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
/**
 * Asks TALON to immedietely respond with signal value.  This API is only used
 * for signals that are not sent periodically.
 * This can be useful for reading params that rarely change like Limit Switch
 * settings and PIDF values.
  * @param param to request.
 */
CTR_Code CanTalonSRX::RequestParam(param_t paramEnum) {
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

CTR_Code CanTalonSRX::SetParam(param_t paramEnum, double value) {
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::GetParamResponse(param_t paramEnum, double &value) {
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::GetParamResponseInt32(param_t paramEnum, int &value) {
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
/*----- getters and setters that use param request/response. These signals are
 * backed up in flash and will survive a power cycle. ---------*/
/*----- If your application requires changing these values consider using both
 * slots and switch between slot0 <=> slot1. ------------------*/
/*----- If your application requires changing these signals frequently then it
 * makes sense to leverage this API. --------------------------*/
/*----- Getters don't block, so it may require several calls to get the latest
 * value. --------------------------*/
CTR_Code CanTalonSRX::SetPgain(unsigned slotIdx, double gain) {
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::SetIgain(unsigned slotIdx, double gain) {
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::SetDgain(unsigned slotIdx, double gain) {
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::SetFgain(unsigned slotIdx, double gain) {
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::SetIzone(unsigned slotIdx, int zone) {
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::SetCloseLoopRampRate(unsigned slotIdx,
                                           int closeLoopRampRate) {
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::SetVoltageCompensationRate(double voltagePerMs) {
  return SetParam(eProfileParamVcompRate, voltagePerMs);
}
CTR_Code CanTalonSRX::GetPgain(unsigned slotIdx, double &gain) {
  if (slotIdx == 0) return GetParamResponse(eProfileParamSlot0_P, gain);
  return GetParamResponse(eProfileParamSlot1_P, gain);
}
CTR_Code CanTalonSRX::GetIgain(unsigned slotIdx, double &gain) {
  if (slotIdx == 0) return GetParamResponse(eProfileParamSlot0_I, gain);
  return GetParamResponse(eProfileParamSlot1_I, gain);
}
CTR_Code CanTalonSRX::GetDgain(unsigned slotIdx, double &gain) {
  if (slotIdx == 0) return GetParamResponse(eProfileParamSlot0_D, gain);
  return GetParamResponse(eProfileParamSlot1_D, gain);
}
CTR_Code CanTalonSRX::GetFgain(unsigned slotIdx, double &gain) {
  if (slotIdx == 0) return GetParamResponse(eProfileParamSlot0_F, gain);
  return GetParamResponse(eProfileParamSlot1_F, gain);
}
CTR_Code CanTalonSRX::GetIzone(unsigned slotIdx, int &zone) {
  if (slotIdx == 0)
    return GetParamResponseInt32(eProfileParamSlot0_IZone, zone);
  return GetParamResponseInt32(eProfileParamSlot1_IZone, zone);
}
CTR_Code CanTalonSRX::GetCloseLoopRampRate(unsigned slotIdx,
                                           int &closeLoopRampRate) {
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::GetVoltageCompensationRate(double &voltagePerMs) {
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::SetSensorPosition(int pos) {
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::SetForwardSoftLimit(int forwardLimit) {
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::SetReverseSoftLimit(int reverseLimit) {
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::SetForwardSoftEnable(int enable) {
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::SetReverseSoftEnable(int enable) {
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::GetForwardSoftLimit(int &forwardLimit) {
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::GetReverseSoftLimit(int &reverseLimit) {
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::GetForwardSoftEnable(int &enable) {
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::GetReverseSoftEnable(int &enable) {
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
/**
 * @param param [out] Rise to fall time period in microseconds.
 */
CTR_Code CanTalonSRX::GetPulseWidthRiseToFallUs(int &param) {
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::IsPulseWidthSensorPresent(int &param) {
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
/**
 * @param modeSelect selects which mode.
 * @param demand setpt or throttle or masterId to follow.
 * @return error code, 0 iff successful.
 * This function has the advantage of atomically setting mode and demand.
 */
CTR_Code CanTalonSRX::SetModeSelect(int modeSelect, int demand) {

    CanTalonSpeedController::ControlMode mode = CanTalonSpeedController::ControlMode_Unknown;

    switch(modeSelect)
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
        std::cerr << "Unsupported control mode " << modeSelect << std::endl;
    }

    std::shared_ptr<CanTalonSpeedController> speedController = GetCanTalon(mDeviceNumber);
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
/**
 * Change the periodMs of a TALON's status frame.  See kStatusFrame_* enums for
 * what's available.
 */
CTR_Code CanTalonSRX::SetStatusFrameRate(unsigned frameEnum,
                                         unsigned periodMs) {
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
/**
 * Clear all sticky faults in TALON.
 */
CTR_Code CanTalonSRX::ClearStickyFaults() {
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}

/**
 * Calling application can opt to speed up the handshaking between the robot API
 * and the Talon to increase the download rate of the Talon's Motion Profile.
 * Ideally the period should be no more than half the period of a trajectory
 * point.
 */
void CanTalonSRX::ChangeMotionControlFramePeriod(uint32_t periodMs) {
}
/**
 * Clear the buffered motion profile in both Talon RAM (bottom), and in the API
 * (top).
 */
void CanTalonSRX::ClearMotionProfileTrajectories() {
}
/**
 * Retrieve just the buffer count for the api-level (top) buffer.
 * This routine performs no CAN or data structure lookups, so its fast and ideal
 * if caller needs to quickly poll the progress of trajectory points being
 * emptied into Talon's RAM. Otherwise just use GetMotionProfileStatus.
 * @return number of trajectory points in the top buffer.
 */
uint32_t CanTalonSRX::GetMotionProfileTopLevelBufferCount() {
	return 0;
}
/**
 * Retrieve just the buffer full for the api-level (top) buffer.
 * This routine performs no CAN or data structure lookups, so its fast and ideal
 * if caller needs to quickly poll. Otherwise just use GetMotionProfileStatus.
 * @return number of trajectory points in the top buffer.
 */
bool CanTalonSRX::IsMotionProfileTopLevelBufferFull() {
  return false;
}
/**
 * Push another trajectory point into the top level buffer (which is emptied
 * into the Talon's bottom buffer as room allows).
 * @param targPos  servo position in native Talon units (sensor units).
 * @param targVel  velocity to feed-forward in native Talon units (sensor units
 *                 per 100ms).
 * @param profileSlotSelect  which slot to pull PIDF gains from.  Currently
 *                           supports 0 or 1.
 * @param timeDurMs  time in milliseconds of how long to apply this point.
 * @param velOnly  set to nonzero to signal Talon that only the feed-foward
 *                 velocity should be used, i.e. do not perform PID on position.
 *                 This is equivalent to setting PID gains to zero, but much
 *                 more efficient and synchronized to MP.
 * @param isLastPoint  set to nonzero to signal Talon to keep processing this
 *                     trajectory point, instead of jumping to the next one
 *                     when timeDurMs expires.  Otherwise MP executer will
 *                     eventually see an empty buffer after the last point
 *                     expires, causing it to assert the IsUnderRun flag.
 *                     However this may be desired if calling application
 *                     never wants to terminate the MP.
 * @param zeroPos  set to nonzero to signal Talon to "zero" the selected
 *                 position sensor before executing this trajectory point.
 *                 Typically the first point should have this set only thus
 *                 allowing the remainder of the MP positions to be relative to
 *                 zero.
 * @return CTR_OKAY if trajectory point push ok. CTR_BufferFull if buffer is
 *         full due to kMotionProfileTopBufferCapacity.
 */
CTR_Code CanTalonSRX::PushMotionProfileTrajectory(int targPos, int targVel,
                                                  int profileSlotSelect,
                                                  int timeDurMs, int velOnly,
                                                  int isLastPoint,
                                                  int zeroPos) {
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
void CanTalonSRX::CopyTrajPtIntoControl(
    TALON_Control_6_MotProfAddTrajPoint_t *control,
    const TALON_Control_6_MotProfAddTrajPoint_t *newPt) {

}
/**
 * Caller is either pushing a new motion profile point, or is
 * calling the Process buffer routine.  In either case check our
 * flow control to see if we need to start sending control6.
 */
void CanTalonSRX::ReactToMotionProfileCall() {

}
/**
 * This must be called periodically to funnel the trajectory points from the
 * API's top level buffer to the Talon's bottom level buffer.  Recommendation
 * is to call this twice as fast as the executation rate of the motion profile.
 * So if MP is running with 20ms trajectory points, try calling this routine
 * every 10ms.  All motion profile functions are thread-safe through the use of
 * a mutex, so there is no harm in having the caller utilize threading.
 */
void CanTalonSRX::ProcessMotionProfileBuffer() {

}
/**
 * Retrieve all status information.
 * Since this all comes from one CAN frame, its ideal to have one routine to
 * retrieve the frame once and decode everything.
 * @param [out] flags  bitfield for status bools. Starting with least
 *        significant bit: IsValid, HasUnderrun, IsUnderrun, IsLast, VelOnly.
 *
 *        IsValid  set when MP executer is processing a trajectory point,
 *                 and that point's status is instrumented with IsLast,
 *                 VelOnly, targPos, targVel.  However if MP executor is
 *                 not processing a trajectory point, then this flag is
 *                 false, and the instrumented signals will be zero.
 *        HasUnderrun  is set anytime the MP executer is ready to pop
 *                     another trajectory point from the Talon's RAM,
 *                     but the buffer is empty.  It can only be cleared
 *                     by using SetParam(eMotionProfileHasUnderrunErr,0);
 *        IsUnderrun  is set when the MP executer is ready for another
 *                    point, but the buffer is empty, and cleared when
 *                    the MP executer does not need another point.
 *                    HasUnderrun shadows this registor when this
 *                    register gets set, however HasUnderrun stays
 *                    asserted until application has process it, and
 *                    IsUnderrun auto-clears when the condition is
 *                    resolved.
 *        IsLast  is set/cleared based on the MP executer's current
 *                trajectory point's IsLast value.  This assumes
 *                IsLast was set when PushMotionProfileTrajectory
 *                was used to insert the currently processed trajectory
 *                point.
 *        VelOnly  is set/cleared based on the MP executer's current
 *                 trajectory point's VelOnly value.
 *
 * @param [out] profileSlotSelect  The currently processed trajectory point's
 *        selected slot.  This can differ in the currently selected slot used
 *        for Position and Velocity servo modes.
 * @param [out] targPos The currently processed trajectory point's position
 *        in native units.  This param is zero if IsValid is zero.
 * @param [out] targVel The currently processed trajectory point's velocity
 *        in native units.  This param is zero if IsValid is zero.
 * @param [out] topBufferRem The remaining number of points in the top level
 *        buffer.
 * @param [out] topBufferCnt The number of points in the top level buffer to
 *        be sent to Talon.
 * @param [out] btmBufferCnt The number of points in the bottom level buffer
 *        inside Talon.
 * @return CTR error code
 */
CTR_Code CanTalonSRX::GetMotionProfileStatus(
    uint32_t &flags, uint32_t &profileSlotSelect, int32_t &targPos,
    int32_t &targVel, uint32_t &topBufferRem, uint32_t &topBufferCnt,
    uint32_t &btmBufferCnt, uint32_t &outputEnable) {
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
//------------------------ auto generated ------------------------------------//
/* This API is optimal since it uses the fire-and-forget CAN interface.
 * These signals should cover the majority of all use cases.
 */
CTR_Code CanTalonSRX::GetFault_OverTemp(int &param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::GetFault_UnderVoltage(int &param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::GetFault_ForLim(int &param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::GetFault_RevLim(int &param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::GetFault_HardwareFailure(int &param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::GetFault_ForSoftLim(int &param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::GetFault_RevSoftLim(int &param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::GetStckyFault_OverTemp(int &param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::GetStckyFault_UnderVoltage(int &param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::GetStckyFault_ForLim(int &param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::GetStckyFault_RevLim(int &param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::GetStckyFault_ForSoftLim(int &param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::GetStckyFault_RevSoftLim(int &param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::GetAppliedThrottle(int &param)
{
    double voltagePercent = SensorActuatorRegistry::Get().GetSpeedControllerWrapper(mDeviceNumber)->GetVoltagePercentage();

    param = (int) (voltagePercent * 1023);

    return CTR_OKAY;
}
CTR_Code CanTalonSRX::GetCloseLoopErr(int &param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::GetFeedbackDeviceSelect(int &param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::GetModeSelect(int &param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::GetLimitSwitchEn(int &param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::GetLimitSwitchClosedFor(int &param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::GetLimitSwitchClosedRev(int &param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::GetSensorPosition(int &param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::GetSensorVelocity(int &param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::GetCurrent(double &param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::GetBrakeIsEnabled(int &param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::GetEncPosition(int &param)
{
    int encoderHandle = GetEncoderHandle(mDeviceNumber);
    std::shared_ptr<EncoderWrapper> wrapper =
            SensorActuatorRegistry::Get().GetEncoderWrapper(encoderHandle);

    if(wrapper)
    {
        double distance = wrapper->GetDistance();
        param = (int) (distance);
        return CTR_OKAY;
    }

    std::cerr << "Encoder has not been hooked up for " << mDeviceNumber << ".  The simulator is stupid, remember to call setFeedbackDevice" << std::endl;

    param = 0;
    return CTR_InvalidParamValue;
}
CTR_Code CanTalonSRX::GetEncVel(int &param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::GetEncIndexRiseEvents(int &param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::GetQuadApin(int &param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::GetQuadBpin(int &param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::GetQuadIdxpin(int &param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::GetAnalogInWithOv(int &param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::GetAnalogInVel(int &param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::GetTemp(double &param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::GetBatteryV(double &param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::GetResetCount(int &param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::GetResetFlags(int &param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::GetFirmVers(int &param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::GetPulseWidthPosition(int &param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::GetPulseWidthVelocity(int &param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::GetPulseWidthRiseToRiseUs(int &param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::GetActTraj_IsValid(int &param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::GetActTraj_ProfileSlotSelect(int &param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::GetActTraj_VelOnly(int &param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::GetActTraj_IsLast(int &param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::GetOutputType(int &param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::GetHasUnderrun(int &param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::GetIsUnderrun(int &param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::GetNextID(int &param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::GetBufferIsFull(int &param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::GetCount(int &param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::GetActTraj_Velocity(int &param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::GetActTraj_Position(int &param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::SetDemand(int param)
{
    GetCanTalon(mDeviceNumber)->SmartSet(param);
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::SetOverrideLimitSwitchEn(int param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::SetFeedbackDeviceSelect(int param)
{
    switch(param)
    {
    case 0:
    {
        int encoderHandle = GetEncoderHandle(mDeviceNumber);
        std::string encoderName = "CAN Encoder " + std::to_string(mDeviceNumber);
        SensorActuatorRegistry::Get().Register(encoderHandle, std::shared_ptr<EncoderWrapper>(new EncoderWrapper(encoderName)));
        break;
    }
    default:
        std::cerr << "Unknown feedback device " << param << std::endl;

    }

    return CTR_OKAY;
}

CTR_Code CanTalonSRX::SetRevMotDuringCloseLoopEn(int param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::SetOverrideBrakeType(int param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::SetModeSelect(int param)
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

    std::shared_ptr<CanTalonSpeedController> speedController = GetCanTalon(mDeviceNumber);
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
CTR_Code CanTalonSRX::SetProfileSlotSelect(int param)
{
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::SetRampThrottle(int param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}
CTR_Code CanTalonSRX::SetRevFeedbackSensor(int param)
{
    LOG_UNSUPPORTED();
    return CTR_OKAY;
}


//------------------ C interface --------------------------------------------//
extern "C" {
void *c_TalonSRX_Create3(int deviceNumber, int controlPeriodMs, int enablePeriodMs)
{
  return new CanTalonSRX(deviceNumber, controlPeriodMs, enablePeriodMs);
}
void *c_TalonSRX_Create2(int deviceNumber, int controlPeriodMs)
{
  return new CanTalonSRX(deviceNumber, controlPeriodMs);
}
void *c_TalonSRX_Create1(int deviceNumber)
{
  return new CanTalonSRX(deviceNumber);
}
void c_TalonSRX_Destroy(void *handle)
{
  delete (CanTalonSRX*)handle;
}
void c_TalonSRX_Set(void *handle, double value)
{
  return ((CanTalonSRX*)handle)->Set(value);
}
CTR_Code c_TalonSRX_SetParam(void *handle, int paramEnum, double value)
{
  return ((CanTalonSRX*)handle)->SetParam((CanTalonSRX::param_t)paramEnum, value);
}
CTR_Code c_TalonSRX_RequestParam(void *handle, int paramEnum)
{
  return ((CanTalonSRX*)handle)->RequestParam((CanTalonSRX::param_t)paramEnum);
}
CTR_Code c_TalonSRX_GetParamResponse(void *handle, int paramEnum, double *value)
{
  return ((CanTalonSRX*)handle)->GetParamResponse((CanTalonSRX::param_t)paramEnum, *value);
}
CTR_Code c_TalonSRX_GetParamResponseInt32(void *handle, int paramEnum, int *value)
{
  return ((CanTalonSRX*)handle)->GetParamResponseInt32((CanTalonSRX::param_t)paramEnum, *value);
}
CTR_Code c_TalonSRX_SetPgain(void *handle, int slotIdx, double gain)
{
  return ((CanTalonSRX*)handle)->SetPgain((unsigned)slotIdx, gain);
}
CTR_Code c_TalonSRX_SetIgain(void *handle, int slotIdx, double gain)
{
  return ((CanTalonSRX*)handle)->SetIgain((unsigned)slotIdx, gain);
}
CTR_Code c_TalonSRX_SetDgain(void *handle, int slotIdx, double gain)
{
  return ((CanTalonSRX*)handle)->SetDgain((unsigned)slotIdx, gain);
}
CTR_Code c_TalonSRX_SetFgain(void *handle, int slotIdx, double gain)
{
  return ((CanTalonSRX*)handle)->SetFgain((unsigned)slotIdx, gain);
}
CTR_Code c_TalonSRX_SetIzone(void *handle, int slotIdx, int zone)
{
  return ((CanTalonSRX*)handle)->SetIzone((unsigned)slotIdx, zone);
}
CTR_Code c_TalonSRX_SetCloseLoopRampRate(void *handle, int slotIdx, int closeLoopRampRate)
{
  return ((CanTalonSRX*)handle)->SetCloseLoopRampRate((unsigned)slotIdx, closeLoopRampRate);
}
CTR_Code c_TalonSRX_SetVoltageCompensationRate(void *handle, double voltagePerMs)
{
  return ((CanTalonSRX*)handle)->SetVoltageCompensationRate(voltagePerMs);
}
CTR_Code c_TalonSRX_SetSensorPosition(void *handle, int pos)
{
  return ((CanTalonSRX*)handle)->SetSensorPosition(pos);
}
CTR_Code c_TalonSRX_SetForwardSoftLimit(void *handle, int forwardLimit)
{
  return ((CanTalonSRX*)handle)->SetForwardSoftLimit(forwardLimit);
}
CTR_Code c_TalonSRX_SetReverseSoftLimit(void *handle, int reverseLimit)
{
  return ((CanTalonSRX*)handle)->SetReverseSoftLimit(reverseLimit);
}
CTR_Code c_TalonSRX_SetForwardSoftEnable(void *handle, int enable)
{
  return ((CanTalonSRX*)handle)->SetForwardSoftEnable(enable);
}
CTR_Code c_TalonSRX_SetReverseSoftEnable(void *handle, int enable)
{
  return ((CanTalonSRX*)handle)->SetReverseSoftEnable(enable);
}
CTR_Code c_TalonSRX_GetPgain(void *handle, int slotIdx, double *gain)
{
  return ((CanTalonSRX*)handle)->GetPgain((unsigned)slotIdx, *gain);
}
CTR_Code c_TalonSRX_GetIgain(void *handle, int slotIdx, double *gain)
{
  return ((CanTalonSRX*)handle)->GetIgain((unsigned)slotIdx, *gain);
}
CTR_Code c_TalonSRX_GetDgain(void *handle, int slotIdx, double *gain)
{
  return ((CanTalonSRX*)handle)->GetDgain((unsigned)slotIdx, *gain);
}
CTR_Code c_TalonSRX_GetFgain(void *handle, int slotIdx, double *gain)
{
  return ((CanTalonSRX*)handle)->GetFgain((unsigned)slotIdx, *gain);
}
CTR_Code c_TalonSRX_GetIzone(void *handle, int slotIdx, int *zone)
{
  return ((CanTalonSRX*)handle)->GetIzone((unsigned)slotIdx, *zone);
}
CTR_Code c_TalonSRX_GetCloseLoopRampRate(void *handle, int slotIdx, int *closeLoopRampRate)
{
  return ((CanTalonSRX*)handle)->GetCloseLoopRampRate((unsigned)slotIdx, *closeLoopRampRate);
}
CTR_Code c_TalonSRX_GetVoltageCompensationRate(void *handle, double *voltagePerMs)
{
  return ((CanTalonSRX*)handle)->GetVoltageCompensationRate(*voltagePerMs);
}
CTR_Code c_TalonSRX_GetForwardSoftLimit(void *handle, int *forwardLimit)
{
  return ((CanTalonSRX*)handle)->GetForwardSoftLimit(*forwardLimit);
}
CTR_Code c_TalonSRX_GetReverseSoftLimit(void *handle, int *reverseLimit)
{
  return ((CanTalonSRX*)handle)->GetReverseSoftLimit(*reverseLimit);
}
CTR_Code c_TalonSRX_GetForwardSoftEnable(void *handle, int *enable)
{
  return ((CanTalonSRX*)handle)->GetForwardSoftEnable(*enable);
}
CTR_Code c_TalonSRX_GetReverseSoftEnable(void *handle, int *enable)
{
  return ((CanTalonSRX*)handle)->GetReverseSoftEnable(*enable);
}
CTR_Code c_TalonSRX_GetPulseWidthRiseToFallUs(void *handle, int *param)
{
  return ((CanTalonSRX*)handle)->GetPulseWidthRiseToFallUs(*param);
}
CTR_Code c_TalonSRX_IsPulseWidthSensorPresent(void *handle, int *param)
{
  return ((CanTalonSRX*)handle)->IsPulseWidthSensorPresent(*param);
}
CTR_Code c_TalonSRX_SetModeSelect2(void *handle, int modeSelect, int demand)
{
  return ((CanTalonSRX*)handle)->SetModeSelect(modeSelect, demand);
}
CTR_Code c_TalonSRX_SetStatusFrameRate(void *handle, int frameEnum, int periodMs)
{
  return ((CanTalonSRX*)handle)->SetStatusFrameRate((unsigned)frameEnum, (unsigned)periodMs);
}
CTR_Code c_TalonSRX_ClearStickyFaults(void *handle)
{
  return ((CanTalonSRX*)handle)->ClearStickyFaults();
}
void c_TalonSRX_ChangeMotionControlFramePeriod(void *handle, int periodMs)
{
  return ((CanTalonSRX*)handle)->ChangeMotionControlFramePeriod((uint32_t)periodMs);
}
void c_TalonSRX_ClearMotionProfileTrajectories(void *handle)
{
  return ((CanTalonSRX*)handle)->ClearMotionProfileTrajectories();
}
int c_TalonSRX_GetMotionProfileTopLevelBufferCount(void *handle)
{
  return ((CanTalonSRX*)handle)->GetMotionProfileTopLevelBufferCount();
}
int c_TalonSRX_IsMotionProfileTopLevelBufferFull(void *handle)
{
  return ((CanTalonSRX*)handle)->IsMotionProfileTopLevelBufferFull();
}
CTR_Code c_TalonSRX_PushMotionProfileTrajectory(void *handle, int targPos, int targVel, int profileSlotSelect, int timeDurMs, int velOnly, int isLastPoint, int zeroPos)
{
  return ((CanTalonSRX*)handle)->PushMotionProfileTrajectory(targPos, targVel, profileSlotSelect, timeDurMs, velOnly, isLastPoint, zeroPos);
}
void c_TalonSRX_ProcessMotionProfileBuffer(void *handle)
{
  return ((CanTalonSRX*)handle)->ProcessMotionProfileBuffer();
}
CTR_Code c_TalonSRX_GetMotionProfileStatus(void *handle, int *flags, int *profileSlotSelect, int *targPos, int *targVel, int *topBufferRemaining, int *topBufferCnt, int *btmBufferCnt, int *outputEnable)
{
  uint32_t flags_val;
  uint32_t profileSlotSelect_val;
  int32_t targPos_val;
  int32_t targVel_val;
  uint32_t topBufferRemaining_val;
  uint32_t topBufferCnt_val;
  uint32_t btmBufferCnt_val;
  uint32_t outputEnable_val;
  CTR_Code retval = ((CanTalonSRX*)handle)->GetMotionProfileStatus(flags_val, profileSlotSelect_val, targPos_val, targVel_val, topBufferRemaining_val, topBufferCnt_val, btmBufferCnt_val, outputEnable_val);
  *flags = (int)flags_val;
  *profileSlotSelect = (int)profileSlotSelect_val;
  *targPos = (int)targPos_val;
  *targVel = (int)targVel_val;
  *topBufferRemaining = (int)topBufferRemaining_val;
  *topBufferCnt = (int)topBufferCnt_val;
  *btmBufferCnt = (int)btmBufferCnt_val;
  *outputEnable = (int)outputEnable_val;
  return retval;
}
CTR_Code c_TalonSRX_GetFault_OverTemp(void *handle, int *param)
{
  return ((CanTalonSRX*)handle)->GetFault_OverTemp(*param);
}
CTR_Code c_TalonSRX_GetFault_UnderVoltage(void *handle, int *param)
{
  return ((CanTalonSRX*)handle)->GetFault_UnderVoltage(*param);
}
CTR_Code c_TalonSRX_GetFault_ForLim(void *handle, int *param)
{
  return ((CanTalonSRX*)handle)->GetFault_ForLim(*param);
}
CTR_Code c_TalonSRX_GetFault_RevLim(void *handle, int *param)
{
  return ((CanTalonSRX*)handle)->GetFault_RevLim(*param);
}
CTR_Code c_TalonSRX_GetFault_HardwareFailure(void *handle, int *param)
{
  return ((CanTalonSRX*)handle)->GetFault_HardwareFailure(*param);
}
CTR_Code c_TalonSRX_GetFault_ForSoftLim(void *handle, int *param)
{
  return ((CanTalonSRX*)handle)->GetFault_ForSoftLim(*param);
}
CTR_Code c_TalonSRX_GetFault_RevSoftLim(void *handle, int *param)
{
  return ((CanTalonSRX*)handle)->GetFault_RevSoftLim(*param);
}
CTR_Code c_TalonSRX_GetStckyFault_OverTemp(void *handle, int *param)
{
  return ((CanTalonSRX*)handle)->GetStckyFault_OverTemp(*param);
}
CTR_Code c_TalonSRX_GetStckyFault_UnderVoltage(void *handle, int *param)
{
  return ((CanTalonSRX*)handle)->GetStckyFault_UnderVoltage(*param);
}
CTR_Code c_TalonSRX_GetStckyFault_ForLim(void *handle, int *param)
{
  return ((CanTalonSRX*)handle)->GetStckyFault_ForLim(*param);
}
CTR_Code c_TalonSRX_GetStckyFault_RevLim(void *handle, int *param)
{
  return ((CanTalonSRX*)handle)->GetStckyFault_RevLim(*param);
}
CTR_Code c_TalonSRX_GetStckyFault_ForSoftLim(void *handle, int *param)
{
  return ((CanTalonSRX*)handle)->GetStckyFault_ForSoftLim(*param);
}
CTR_Code c_TalonSRX_GetStckyFault_RevSoftLim(void *handle, int *param)
{
  return ((CanTalonSRX*)handle)->GetStckyFault_RevSoftLim(*param);
}
CTR_Code c_TalonSRX_GetAppliedThrottle(void *handle, int *param)
{
  return ((CanTalonSRX*)handle)->GetAppliedThrottle(*param);
}
CTR_Code c_TalonSRX_GetCloseLoopErr(void *handle, int *param)
{
  return ((CanTalonSRX*)handle)->GetCloseLoopErr(*param);
}
CTR_Code c_TalonSRX_GetFeedbackDeviceSelect(void *handle, int *param)
{
  return ((CanTalonSRX*)handle)->GetFeedbackDeviceSelect(*param);
}
CTR_Code c_TalonSRX_GetModeSelect(void *handle, int *param)
{
  return ((CanTalonSRX*)handle)->GetModeSelect(*param);
}
CTR_Code c_TalonSRX_GetLimitSwitchEn(void *handle, int *param)
{
  return ((CanTalonSRX*)handle)->GetLimitSwitchEn(*param);
}
CTR_Code c_TalonSRX_GetLimitSwitchClosedFor(void *handle, int *param)
{
  return ((CanTalonSRX*)handle)->GetLimitSwitchClosedFor(*param);
}
CTR_Code c_TalonSRX_GetLimitSwitchClosedRev(void *handle, int *param)
{
  return ((CanTalonSRX*)handle)->GetLimitSwitchClosedRev(*param);
}
CTR_Code c_TalonSRX_GetSensorPosition(void *handle, int *param)
{
  return ((CanTalonSRX*)handle)->GetSensorPosition(*param);
}
CTR_Code c_TalonSRX_GetSensorVelocity(void *handle, int *param)
{
  return ((CanTalonSRX*)handle)->GetSensorVelocity(*param);
}
CTR_Code c_TalonSRX_GetCurrent(void *handle, double *param)
{
  return ((CanTalonSRX*)handle)->GetCurrent(*param);
}
CTR_Code c_TalonSRX_GetBrakeIsEnabled(void *handle, int *param)
{
  return ((CanTalonSRX*)handle)->GetBrakeIsEnabled(*param);
}
CTR_Code c_TalonSRX_GetEncPosition(void *handle, int *param)
{
  return ((CanTalonSRX*)handle)->GetEncPosition(*param);
}
CTR_Code c_TalonSRX_GetEncVel(void *handle, int *param)
{
  return ((CanTalonSRX*)handle)->GetEncVel(*param);
}
CTR_Code c_TalonSRX_GetEncIndexRiseEvents(void *handle, int *param)
{
  return ((CanTalonSRX*)handle)->GetEncIndexRiseEvents(*param);
}
CTR_Code c_TalonSRX_GetQuadApin(void *handle, int *param)
{
  return ((CanTalonSRX*)handle)->GetQuadApin(*param);
}
CTR_Code c_TalonSRX_GetQuadBpin(void *handle, int *param)
{
  return ((CanTalonSRX*)handle)->GetQuadBpin(*param);
}
CTR_Code c_TalonSRX_GetQuadIdxpin(void *handle, int *param)
{
  return ((CanTalonSRX*)handle)->GetQuadIdxpin(*param);
}
CTR_Code c_TalonSRX_GetAnalogInWithOv(void *handle, int *param)
{
  return ((CanTalonSRX*)handle)->GetAnalogInWithOv(*param);
}
CTR_Code c_TalonSRX_GetAnalogInVel(void *handle, int *param)
{
  return ((CanTalonSRX*)handle)->GetAnalogInVel(*param);
}
CTR_Code c_TalonSRX_GetTemp(void *handle, double *param)
{
  return ((CanTalonSRX*)handle)->GetTemp(*param);
}
CTR_Code c_TalonSRX_GetBatteryV(void *handle, double *param)
{
  return ((CanTalonSRX*)handle)->GetBatteryV(*param);
}
CTR_Code c_TalonSRX_GetResetCount(void *handle, int *param)
{
  return ((CanTalonSRX*)handle)->GetResetCount(*param);
}
CTR_Code c_TalonSRX_GetResetFlags(void *handle, int *param)
{
  return ((CanTalonSRX*)handle)->GetResetFlags(*param);
}
CTR_Code c_TalonSRX_GetFirmVers(void *handle, int *param)
{
  return ((CanTalonSRX*)handle)->GetFirmVers(*param);
}
CTR_Code c_TalonSRX_GetPulseWidthPosition(void *handle, int *param)
{
  return ((CanTalonSRX*)handle)->GetPulseWidthPosition(*param);
}
CTR_Code c_TalonSRX_GetPulseWidthVelocity(void *handle, int *param)
{
  return ((CanTalonSRX*)handle)->GetPulseWidthVelocity(*param);
}
CTR_Code c_TalonSRX_GetPulseWidthRiseToRiseUs(void *handle, int *param)
{
  return ((CanTalonSRX*)handle)->GetPulseWidthRiseToRiseUs(*param);
}
CTR_Code c_TalonSRX_GetActTraj_IsValid(void *handle, int *param)
{
  return ((CanTalonSRX*)handle)->GetActTraj_IsValid(*param);
}
CTR_Code c_TalonSRX_GetActTraj_ProfileSlotSelect(void *handle, int *param)
{
  return ((CanTalonSRX*)handle)->GetActTraj_ProfileSlotSelect(*param);
}
CTR_Code c_TalonSRX_GetActTraj_VelOnly(void *handle, int *param)
{
  return ((CanTalonSRX*)handle)->GetActTraj_VelOnly(*param);
}
CTR_Code c_TalonSRX_GetActTraj_IsLast(void *handle, int *param)
{
  return ((CanTalonSRX*)handle)->GetActTraj_IsLast(*param);
}
CTR_Code c_TalonSRX_GetOutputType(void *handle, int *param)
{
  return ((CanTalonSRX*)handle)->GetOutputType(*param);
}
CTR_Code c_TalonSRX_GetHasUnderrun(void *handle, int *param)
{
  return ((CanTalonSRX*)handle)->GetHasUnderrun(*param);
}
CTR_Code c_TalonSRX_GetIsUnderrun(void *handle, int *param)
{
  return ((CanTalonSRX*)handle)->GetIsUnderrun(*param);
}
CTR_Code c_TalonSRX_GetNextID(void *handle, int *param)
{
  return ((CanTalonSRX*)handle)->GetNextID(*param);
}
CTR_Code c_TalonSRX_GetBufferIsFull(void *handle, int *param)
{
  return ((CanTalonSRX*)handle)->GetBufferIsFull(*param);
}
CTR_Code c_TalonSRX_GetCount(void *handle, int *param)
{
  return ((CanTalonSRX*)handle)->GetCount(*param);
}
CTR_Code c_TalonSRX_GetActTraj_Velocity(void *handle, int *param)
{
  return ((CanTalonSRX*)handle)->GetActTraj_Velocity(*param);
}
CTR_Code c_TalonSRX_GetActTraj_Position(void *handle, int *param)
{
  return ((CanTalonSRX*)handle)->GetActTraj_Position(*param);
}
CTR_Code c_TalonSRX_SetDemand(void *handle, int param)
{
  return ((CanTalonSRX*)handle)->SetDemand(param);
}
CTR_Code c_TalonSRX_SetOverrideLimitSwitchEn(void *handle, int param)
{
  return ((CanTalonSRX*)handle)->SetOverrideLimitSwitchEn(param);
}
CTR_Code c_TalonSRX_SetFeedbackDeviceSelect(void *handle, int param)
{
  return ((CanTalonSRX*)handle)->SetFeedbackDeviceSelect(param);
}
CTR_Code c_TalonSRX_SetRevMotDuringCloseLoopEn(void *handle, int param)
{
  return ((CanTalonSRX*)handle)->SetRevMotDuringCloseLoopEn(param);
}
CTR_Code c_TalonSRX_SetOverrideBrakeType(void *handle, int param)
{
  return ((CanTalonSRX*)handle)->SetOverrideBrakeType(param);
}
CTR_Code c_TalonSRX_SetModeSelect(void *handle, int param)
{
  return ((CanTalonSRX*)handle)->SetModeSelect(param);
}
CTR_Code c_TalonSRX_SetProfileSlotSelect(void *handle, int param)
{
  return ((CanTalonSRX*)handle)->SetProfileSlotSelect(param);
}
CTR_Code c_TalonSRX_SetRampThrottle(void *handle, int param)
{
  return ((CanTalonSRX*)handle)->SetRampThrottle(param);
}
CTR_Code c_TalonSRX_SetRevFeedbackSensor(void *handle, int param)
{
  return ((CanTalonSRX*)handle)->SetRevFeedbackSensor(param);
}
}


