/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2014-2016. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

#pragma once

#ifndef CTR_EXCLUDE_WPILIB_CLASSES
#include <memory>

#include "CANSpeedController.h"
#include "CanTalonSRX.h"
#include "LiveWindow/LiveWindowSendable.h"
#include "MotorSafetyHelper.h"
#include "PIDInterface.h"
#include "PIDOutput.h"
#include "PIDSource.h"
#include "SafePWM.h"
#include "tables/ITableListener.h"
#include "GadgeteerUartClient.h"

/**
 * CTRE Talon SRX Speed Controller with CAN Control
 */
 class EXPORT_ CANTalon : public frc::MotorSafety,
                 public frc::CANSpeedController,
                 public frc::ErrorBase,
                 public frc::LiveWindowSendable,
                 public ITableListener,
                 public frc::PIDSource,
                 public frc::PIDInterface,
				 public IGadgeteerUartClient{
 public:
  enum FeedbackDevice {
    QuadEncoder = 0,
    AnalogPot = 2,
    AnalogEncoder = 3,
    EncRising = 4,
    EncFalling = 5,
    CtreMagEncoder_Relative = 6,  //!< Cross The Road Electronics Magnetic
                                  //! Encoder in Absolute/PulseWidth Mode
    CtreMagEncoder_Absolute = 7,  //!< Cross The Road Electronics Magnetic
                                  //! Encoder in Relative/Quadrature Mode
    PulseWidth = 8,
  };
  /**
   * Depending on the sensor type, Talon can determine if sensor is plugged in
   * ot not.
   */
  enum FeedbackDeviceStatus {
    FeedbackStatusUnknown = 0,  //!< Sensor status could not be determined.  Not
                                //! all sensors can do this.
    FeedbackStatusPresent = 1,  //!< Sensor is present and working okay.
    FeedbackStatusNotPresent =
        2,  //!< Sensor is not present, not plugged in, not powered, etc...
  };
  enum StatusFrameRate {
    StatusFrameRateGeneral = 0,
    StatusFrameRateFeedback = 1,
    StatusFrameRateQuadEncoder = 2,
    StatusFrameRateAnalogTempVbat = 3,
    StatusFrameRatePulseWidthMeas = 4,
  };
  /**
   * Enumerated types for Motion Control Set Values.
   * When in Motion Profile control mode, these constants are paseed
   * into set() to manipulate the motion profile executer.
   * When changing modes, be sure to read the value back using
   * getMotionProfileStatus() to ensure changes in output take effect before
   * performing buffering actions.
   * Disable will signal Talon to put motor output into neutral drive.
   *   Talon will stop processing motion profile points.  This means the buffer
   *   is effectively disconnected from the executer, allowing the robot to
   *   gracefully clear and push new traj points.  isUnderrun will get cleared.
   *   The active trajectory is also cleared.
   * Enable will signal Talon to pop a trajectory point from it's buffer and
   *   process it. If the active trajectory is empty, Talon will shift in the
   *   next point. If the active traj is empty, and so is the buffer, the motor
   *   drive is neutral and isUnderrun is set.  When active traj times out, and
   *   buffer has at least one point, Talon shifts in next one, and isUnderrun
   *   is cleared.  When active traj times out, and buffer is empty, Talon
   *   keeps processing active traj and sets IsUnderrun.
   * Hold will signal Talon keep processing the active trajectory indefinitely.
   *   If the active traj is cleared, Talon will neutral motor drive.  Otherwise
   *   Talon will keep processing the active traj but it will not shift in
   *   points from the buffer.  This means the buffer is effectively
   *   disconnected from the executer, allowing the robot to gracefully clear
   *   and push new traj points. isUnderrun is set if active traj is empty,
   *   otherwise it is cleared. isLast signal is also cleared.
   *
   * Typical workflow:
   *   set(Disable),
   *   Confirm Disable takes effect,
   *   clear buffer and push buffer points,
   *   set(Enable) when enough points have been pushed to ensure no underruns,
   *   wait for MP to finish or decide abort,
   *   If MP finished gracefully set(Hold) to hold position servo and disconnect
   *   buffer,
   *   If MP is being aborted set(Disable) to neutral the motor and disconnect
   *   buffer,
   *   Confirm mode takes effect,
   *   clear buffer and push buffer points, and rinse-repeat.
   */
  enum SetValueMotionProfile {
    SetValueMotionProfileDisable = 0,
    SetValueMotionProfileEnable = 1,
    SetValueMotionProfileHold = 2,
  };
  /**
   * Motion Profile Trajectory Point
   * This is simply a data transer object.
   */
  struct TrajectoryPoint {
    double position;  //!< The position to servo to.
    double velocity;  //!< The velocity to feed-forward.

    /**
     * Time in milliseconds to process this point.
     * Value should be between 1ms and 255ms.  If value is zero
     * then Talon will default to 1ms.  If value exceeds 255ms API will cap it.
     */
    unsigned int timeDurMs;
    /**
     * Which slot to get PIDF gains.
     * PID is used for position servo.
     * F is used as the Kv constant for velocity feed-forward.
     * Typically this is hardcoded to the a particular slot, but you are free
     * gain schedule if need be.
     */
    unsigned int profileSlotSelect;
    /**
     * Set to true to only perform the velocity feed-forward and not perform
     * position servo.  This is useful when learning how the position servo
     * changes the motor response.  The same could be accomplish by clearing the
     * PID gains, however this is synchronous the streaming, and doesn't require
     * restoing
     * gains when finished.
     *
     * Additionaly setting this basically gives you direct control of the motor
     * output
     * since motor output = targetVelocity X Kv, where Kv is our Fgain.
     * This means you can also scheduling straight-throttle curves without
     * relying on
     * a sensor.
     */
    bool velocityOnly;
    /**
     * Set to true to signal Talon that this is the final point, so do not
     * attempt to pop another trajectory point from out of the Talon buffer.
     * Instead continue processing this way point.  Typically the velocity
     * member variable should be zero so that the motor doesn't spin
     * indefinitely.
     */
    bool isLastPoint;
    /**
     * Set to true to signal Talon to zero the selected sensor.
     * When generating MPs, one simple method is to make the first target
     * position zero,
     * and the final target position the target distance from the current
     * position.
     * Then when you fire the MP, the current position gets set to zero.
     * If this is the intent, you can set zeroPos on the first trajectory
     * point.
     *
     * Otherwise you can leave this false for all points, and offset the
     * positions
     * of all trajectory points so they are correct.
     */
    bool zeroPos;
  };
  /**
   * Motion Profile Status
   * This is simply a data transer object.
   */
  struct MotionProfileStatus {
    /**
     * The available empty slots in the trajectory buffer.
     *
     * The robot API holds a "top buffer" of trajectory points, so your
     * applicaion can dump several points at once.  The API will then stream
     * them into the Talon's low-level buffer, allowing the Talon to act on
     * them.
     */
    unsigned int topBufferRem;
    /**
     * The number of points in the top trajectory buffer.
     */
    unsigned int topBufferCnt;
    /**
     * The number of points in the low level Talon buffer.
     */
    unsigned int btmBufferCnt;
    /**
     * Set if isUnderrun ever gets set.
     * Only is cleared by clearMotionProfileHasUnderrun() to ensure
     * robot logic can react or instrument it.
     * @see clearMotionProfileHasUnderrun()
     */
    bool hasUnderrun;
    /**
     * This is set if Talon needs to shift a point from its buffer into
     * the active trajectory point however the buffer is empty. This gets
     * cleared automatically when is resolved.
     */
    bool isUnderrun;
    /**
     * True if the active trajectory point has not empty, false otherwise.
     * The members in activePoint are only valid if this signal is set.
     */
    bool activePointValid;
    /**
     * The number of points in the low level Talon buffer.
     */
    TrajectoryPoint activePoint;
    /**
     * The current output mode of the motion profile executer (disabled,
     * enabled, or hold).
     * When changing the set() value in MP mode, it's important to check this
     * signal to confirm the change takes effect before interacting with the
     * top buffer.
     */
    SetValueMotionProfile outputEnable;
  };

  // CAN Talon's native control modes
  enum TalonControlMode {
    kThrottleMode = 0,
    kFollowerMode = 5,
    kVoltageMode = 4,
    kPositionMode = 1,
    kSpeedMode = 2,
    kCurrentMode = 3,
    kMotionProfileMode = 6,
    kMotionMagicMode = 7,
    kDisabled = 15
  };

  explicit CANTalon(int deviceNumber);
  explicit CANTalon(int deviceNumber, int controlPeriodMs);
  virtual ~CANTalon();

  int GetDeviceID();

  // PIDOutput interface
  void PIDWrite(double output) override;

  // PIDSource interface
  double PIDGet() override;

  // MotorSafety interface
  void SetExpiration(double timeout) override;
  double GetExpiration() const override;
  bool IsAlive() const override;
  void StopMotor() override;
  void SetSafetyEnabled(bool enabled) override;
  bool IsSafetyEnabled() const override;
  void GetDescription(llvm::raw_ostream& desc) const override;

  // CANSpeedController interface
  double Get() const override;
  void Set(double value) override;
  void Reset() override;
  void SetSetpoint(double value) override;
  void Disable() override;
  virtual void EnableControl();
  void Enable() override;
  void SetP(double p) override;
  void SetI(double i) override;
  void SetD(double d) override;
  void SetF(double f);
  void SetIzone(unsigned iz);
  void SetPID(double p, double i, double d) override;
  virtual void SetPID(double p, double i, double d, double f);
  double GetP() const override;
  double GetI() const override;
  double GetD() const override;
  virtual double GetF() const;
  bool IsModePID(CANSpeedController::ControlMode mode) const override;
  double GetBusVoltage() const override;
  double GetOutputVoltage() const override;
  double GetOutputCurrent() const override;
  double GetTemperature() const override;
  void SetPosition(double pos);
  double GetPosition() const override;
  double GetSpeed() const override;
  virtual int GetClosedLoopError() const;
  virtual void SetAllowableClosedLoopErr(uint32_t allowableCloseLoopError);
  virtual int GetAnalogIn() const;
  virtual void SetAnalogPosition(int newPosition);
  virtual int GetAnalogInRaw() const;
  virtual int GetAnalogInVel() const;
  virtual int GetEncPosition() const;
  virtual void SetEncPosition(int);
  virtual int GetEncVel() const;
  int GetPinStateQuadA() const;
  int GetPinStateQuadB() const;
  int GetPinStateQuadIdx() const;
  int IsFwdLimitSwitchClosed() const;
  int IsRevLimitSwitchClosed() const;
  int IsZeroSensorPositionOnForwardLimitEnabled() const;
  int IsZeroSensorPositionOnReverseLimitEnabled() const;
  int IsZeroSensorPositionOnIndexEnabled() const;
  int GetNumberOfQuadIdxRises() const;
  void SetNumberOfQuadIdxRises(int rises);
  virtual int GetPulseWidthPosition() const;
  virtual void SetPulseWidthPosition(int newpos);
  virtual int GetPulseWidthVelocity() const;
  virtual int GetPulseWidthRiseToFallUs() const;
  virtual int GetPulseWidthRiseToRiseUs() const;
  virtual FeedbackDeviceStatus IsSensorPresent(
      FeedbackDevice feedbackDevice) const;
  bool GetForwardLimitOK() const override;
  bool GetReverseLimitOK() const override;
  uint16_t GetFaults() const override;
  uint16_t GetStickyFaults() const;
  void ClearStickyFaults();
  void SetVoltageRampRate(double rampRate) override;
  virtual void SetVoltageCompensationRampRate(double rampRate);
  int GetFirmwareVersion() const override;
  void ConfigNeutralMode(NeutralMode mode) override;
  void ConfigEncoderCodesPerRev(uint16_t codesPerRev) override;
  void ConfigPotentiometerTurns(uint16_t turns) override;
  void ConfigSoftPositionLimits(double forwardLimitPosition,
                                double reverseLimitPosition) override;
  void DisableSoftPositionLimits() override;
  void ConfigLimitMode(LimitMode mode) override;
  void ConfigForwardLimit(double forwardLimitPosition) override;
  void ConfigReverseLimit(double reverseLimitPosition) override;
  void ConfigLimitSwitchOverrides(bool bForwardLimitSwitchEn,
                                  bool bReverseLimitSwitchEn);
  void ConfigForwardSoftLimitEnable(bool bForwardSoftLimitEn);
  void ConfigReverseSoftLimitEnable(bool bReverseSoftLimitEn);
  /**
   * Change the fwd limit switch setting to normally open or closed.
   * Talon will disable momentarilly if the Talon's current setting
   * is dissimilar to the caller's requested setting.
   *
   * Since Talon saves setting to flash this should only affect
   * a given Talon initially during robot install.
   *
   * @param normallyOpen true for normally open.  false for normally closed.
   */
  void ConfigFwdLimitSwitchNormallyOpen(bool normallyOpen);
  /**
   * Change the rev limit switch setting to normally open or closed.
   * Talon will disable momentarilly if the Talon's current setting
   * is dissimilar to the caller's requested setting.
   *
   * Since Talon saves setting to flash this should only affect
   * a given Talon initially during robot install.
   *
   * @param normallyOpen true for normally open.  false for normally closed.
   */
  void ConfigRevLimitSwitchNormallyOpen(bool normallyOpen);
  void ConfigMaxOutputVoltage(double voltage) override;
  void ConfigPeakOutputVoltage(double forwardVoltage, double reverseVoltage);
  void ConfigNominalOutputVoltage(double forwardVoltage, double reverseVoltage);
  /**
   * Enables Talon SRX to automatically zero the Sensor Position whenever an
   * edge is detected on the index signal.
   *
   * @param enable      boolean input, pass true to enable feature or false to
   *                    disable.
   * @param risingEdge  boolean input, pass true to clear the position on rising
   *                    edge, pass false to clear the position on falling edge.
   */
  void EnableZeroSensorPositionOnIndex(bool enable, bool risingEdge);
  void EnableZeroSensorPositionOnForwardLimit(bool enable);
  void EnableZeroSensorPositionOnReverseLimit(bool enable);
  /**
   * @param voltage       Motor voltage to output when closed loop features are being used (Position,
   *                      Speed, Motion Profile, Motion Magic, etc.) in volts.
   *                      Pass 0 to disable feature.  Input should be within [0.0 V,255.0 V]
   */
  void SetNominalClosedLoopVoltage(double voltage);
  /**
   * Disables the nominal closed loop voltage compensation.
   * Same as calling SetNominalClosedLoopVoltage(0).
   */
  void DisableNominalClosedLoopVoltage();
  /**
   * @return the currently selected nominal closed loop voltage. Zero (Default) means feature is disabled.
   */
  double GetNominalClosedLoopVoltage();

  enum VelocityMeasurementPeriod{
	Period_1Ms = 1,
	Period_2Ms = 2,
	Period_5Ms = 5,
	Period_10Ms = 10,
	Period_20Ms = 20,
	Period_25Ms = 25,
	Period_50Ms = 50,
	Period_100Ms = 100,
  };
  /**
   * Sets the duration of time that the Talon measures for each velocity measurement (which occures at each 1ms process loop).
   * The default value is 100, which means that every process loop (1ms), the Talon will measure the change in position
   * between now and 100ms ago, and will insert into a rolling average.
   *
   * Decreasing this from the default (100ms) will yield a less-resolute measurement since there is less time for the sensor to change.
   * This will be perceived as increased granularity in the measurement (or stair-stepping).  But doing so will also decrease the latency
   * between sensor motion and measurement.
   *
   * Regardles of this setting value, native velocity units are still in change-in-sensor-per-100ms.
   *
   * @param period      Support period enum.  Curent valid values are 1,2,5,10,20,25,50, or 100ms.
   */
  void SetVelocityMeasurementPeriod(VelocityMeasurementPeriod period);
  /**
   * Sets the window size of the rolling average used in velocity measurement.
   * The default value is 64, which means that every process loop (1ms), the Talon will insert a velocity measurement
   * into a windowed averager with a history of 64 samples.
   * Each sample is inserted every 1ms regardless of what Period is selected.
   * As a result the window is practically in ms units.
   *
   * @param windowSize    Window size of rolling average.
   */
  void SetVelocityMeasurementWindow(uint32_t windowSize);
  VelocityMeasurementPeriod GetVelocityMeasurementPeriod();
  uint32_t GetVelocityMeasurementWindow();

  int ConfigSetParameter(uint32_t paramEnum, double value);
  bool GetParameter(uint32_t paramEnum, double& dvalue) const;

  void ConfigFaultTime(double faultTime) override;
  virtual void SetControlMode(ControlMode mode);
  /**
   * @brief Provides caller a means of specifying the Talon Control mode.
   * Because CAN Talon has been pulled out of wpilib, caller may
   * need to specify Talon specific control modes not easily represented
   * by CANSpeedMotorController::ControlMode.
   * This routine is essentially the same as SetControlMode, but uses Talon's
   * native control mode definitons.
   */
  void SetTalonControlMode(TalonControlMode talonControlMode);
  TalonControlMode GetTalonControlMode()  const;
	
  void SetFeedbackDevice(FeedbackDevice device);
  void SetStatusFrameRateMs(StatusFrameRate stateFrame, int periodMs);
  virtual ControlMode GetControlMode() const;
  void SetSensorDirection(bool reverseSensor);
  void SetClosedLoopOutputDirection(bool reverseOutput);
  void SetCloseLoopRampRate(double rampRate);
  void SelectProfileSlot(int slotIdx);
  int GetIzone() const;
  int GetIaccum() const;
  void ClearIaccum();
  int GetBrakeEnableDuringNeutral() const;

  bool IsControlEnabled() const;
  bool IsEnabled() const override;
  double GetSetpoint() const override;

  /**
   * Calling application can opt to speed up the handshaking between the robot
   * API and the Talon to increase the download rate of the Talon's Motion
   * Profile.  Ideally the period should be no more than half the period of a
   * trajectory point.
   */
  void ChangeMotionControlFramePeriod(int periodMs);

  /**
   * Clear the buffered motion profile in both Talon RAM (bottom), and in the
   * API (top). Be sure to check GetMotionProfileStatus() to know when the
   * buffer is actually cleared.
   */
  void ClearMotionProfileTrajectories();

  /**
   * Retrieve just the buffer count for the api-level (top) buffer.
   * This routine performs no CAN or data structure lookups, so its fast and
   * ideal if caller needs to quickly poll the progress of trajectory points
   * being emptied into Talon's RAM. Otherwise just use GetMotionProfileStatus.
   * @return number of trajectory points in the top buffer.
   */
  int GetMotionProfileTopLevelBufferCount();

  /**
   * Push another trajectory point into the top level buffer (which is emptied
   * into the Talon's bottom buffer as room allows).
   *
   * @param trajPt the trajectory point to insert into buffer.
   * @return true if trajectory point push ok. CTR_BufferFull if buffer is full
   *         due to kMotionProfileTopBufferCapacity.
   */
  bool PushMotionProfileTrajectory(const TrajectoryPoint& trajPt);

  /**
   * @return true if api-level (top) buffer is full.
   */
  bool IsMotionProfileTopLevelBufferFull();

  /**
   * This must be called periodically to funnel the trajectory points from the
   * API's top level buffer to the Talon's bottom level buffer.  Recommendation
   * is to call this twice as fast as the executation rate of the motion
   * profile. So if MP is running with 20ms trajectory points, try calling this
   * routine every 10ms.  All motion profile functions are thread-safe through
   * the use of a mutex, so there is no harm in having the caller utilize
   * threading.
   */
  void ProcessMotionProfileBuffer();

  /**
   * Retrieve all status information.
   * Since this all comes from one CAN frame, its ideal to have one routine to
   * retrieve the frame once and decode everything.
   * @param [out] motionProfileStatus contains all progress information on the
   *                                  currently running MP.
   */
  void GetMotionProfileStatus(MotionProfileStatus& motionProfileStatus);

  /**
   * Clear the hasUnderrun flag in Talon's Motion Profile Executer when MPE is
   * ready for another point, but the low level buffer is empty.
   *
   * Once the Motion Profile Executer sets the hasUnderrun flag, it stays set
   * until Robot Application clears it with this routine, which ensures Robot
   * Application gets a chance to instrument or react.  Caller could also check
   * the isUnderrun flag which automatically clears when fault condition is
   * removed.
   */
  void ClearMotionProfileHasUnderrun();


  /**
   * Set the Cruise Velocity used in Motion Magic Control Mode.
   * @param motmagicCruiseVeloc Cruise(peak) velocity in RPM.
   */
  int SetMotionMagicCruiseVelocity(double motMagicCruiseVeloc);
  /**
   * Set the Acceleration used in Motion Magic Control Mode.
   * @param motMagicAccel Accerleration in RPM per second.
   */
  int SetMotionMagicAcceleration(double motMagicAccel);

  /**
   * @return polled motion magic cruise velocity setting from Talon.
   * RPM if units are configured, velocity native units otherwise.
   */
  double GetMotionMagicCruiseVelocity();
  /**
   * @return polled motion magic acceleration setting from Talon.
   * RPM per second if units are configured, velocity native units per second otherwise.
   */
  double GetMotionMagicAcceleration();

  /**
   * @return current Motion Magic trajectory point's target velocity.
   * RPM if units are configured, velocity native units otherwise.
   */
  double GetMotionMagicActTrajVelocity();
  /**
   * @return current Motion Magic trajectory point's target position.
   * Rotations if units are configured, position native units otherwise.
   */
  double GetMotionMagicActTrajPosition();
  /**
	 * Set the Value of the current limit.  Use {@link #EnableCurrentLimit(boolean)}
	 * to turn feature on and off.
	 *
	 * @param amps Current Limit in amps.
	*/
  int SetCurrentLimit(uint32_t amps);
  /**
	 * Enable or Disable the current limit.  Use {@link #SetCurrentLimit(int)}
	 * to set the value of the limit.
	 *
	 * @param enable True to Enable, False to Disable.
	*/
  int EnableCurrentLimit(bool enable);
  bool HasResetOccured();
  int GetCustomParam0(int32_t & value);
  int GetCustomParam1(int32_t & value);
  int IsPersStorageSaving(bool & isSaving);
  int SetCustomParam0(int32_t value);
  int SetCustomParam1(int32_t value);

  int GetGadgeteerStatus(IGadgeteerUartClient::GadgeteerUartStatus & status);

  // LiveWindow stuff.
  void ValueChanged(ITable* source, llvm::StringRef key,
                    std::shared_ptr<nt::Value> value, bool isNew) override;
  void UpdateTable() override;
  void StartLiveWindowMode() override;
  void StopLiveWindowMode() override;
  std::string GetSmartDashboardType() const override;
  void InitTable(std::shared_ptr<ITable> subTable) override;
  std::shared_ptr<ITable> GetTable() const override;

  // SpeedController overrides
  void SetInverted(bool isInverted) override;
  bool GetInverted() const override;

/**
 * @return low level object for advanced control.
 */
 CanTalonSRX & GetLowLevelObject() { return *m_impl; }
  
 private:

  enum UsageFlags{
	
	Default = 0x00000000, 
	PercentVbus = 0x00000001, 
	Position = 0x00000002, 
	Speed = 0x00000004, 
	Current = 0x00000008, 
	Voltage = 0x00000010, 
	Follower = 0x00000020, 
	MotionProfile = 0x00000040, 
	MotionMagic = 0x00000080, 
	
	VRampRate = 0x00400000, 
	CurrentLimit = 0x00800000, 
	ZeroSensorI = 0x01000000, 
	ZeroSensorF = 0x02000000, 
	ZeroSensorR = 0x04000000, 
	ForwardLimitSwitch = 0x08000000, 
	ReverseLimitSwitch = 0x10000000, 
	ForwardSoftLimit = 0x20000000, 
	ReverseSoftLimit = 0x40000000, 
	MultiProfile = 0x80000000,
};

  int m_deviceNumber;
  std::unique_ptr<CanTalonSRX> m_impl;
  std::unique_ptr<frc::MotorSafetyHelper> m_safetyHelper;
  int m_profile = 0;  // Profile from CANTalon to use. Set to zero until we can
                      // actually test this.

  bool m_controlEnabled = true;
  bool m_stopped = false;

  unsigned int m_usageHist=0;
  
  CANSpeedController::ControlMode m_controlMode = kPercentVbus;
  TalonControlMode m_sendMode = kThrottleMode;

  void ApplyUsageStats(UsageFlags Usage);
  UsageFlags ControlModeUsage(TalonControlMode mode);
  
  
  double m_setPoint = 0;
  /**
   * Encoder CPR, counts per rotations, also called codes per revoluion.
   * Default value of zero means the API behaves as it did during the 2015
   * season, each position unit is a single pulse and there are four pulses per
   * count (4X). Caller can use ConfigEncoderCodesPerRev to set the quadrature
   * encoder CPR.
   */
  uint32_t m_codesPerRev = 0;
  /**
   * Number of turns per rotation.  For example, a 10-turn pot spins ten full
   * rotations from a wiper voltage of zero to 3.3 volts.  Therefore knowing
   * the number of turns a full voltage sweep represents is necessary for
   * calculating rotations and velocity. A default value of zero means the API
   * behaves as it did during the 2015 season, there are 1024 position units
   * from zero to 3.3V.
   */
  uint32_t m_numPotTurns = 0;
  /**
   * Although the Talon handles feedback selection, caching the feedback
   * selection is helpful at the API level for scaling into rotations and RPM.
   */
  FeedbackDevice m_feedbackDevice = QuadEncoder;

  static constexpr unsigned int kDelayForSolicitedSignalsUs = 4000;
  /**
   * @param devToLookup FeedbackDevice to lookup the scalar for.  Because Talon
   *                    allows multiple sensors to be attached simultaneously,
   *                    caller must specify which sensor to lookup.
   * @return The number of native Talon units per rotation of the selected
   *         sensor. Zero if the necessary sensor information is not available.
   * @see ConfigEncoderCodesPerRev
   * @see ConfigPotentiometerTurns
   */
  double GetNativeUnitsPerRotationScalar(FeedbackDevice devToLookup) const;
  /**
   * Fixup the sendMode so Set() serializes the correct demand value.
   * Also fills the modeSelecet in the control frame to disabled.
   * @param mode Control mode to ultimately enter once user calls Set().
   * @see Set()
   */
  void ApplyControlMode(TalonControlMode mode);
  /**
   * @param fullRotations double precision value representing number of
   *                      rotations of selected feedback sensor. If user has
   *                      never called the config routine for the selected
   *                      sensor, then the caller is likely passing rotations
   *                      in engineering units already, in which case it is
   *                      returned as is.
   * @see ConfigPotentiometerTurns
   * @see ConfigEncoderCodesPerRev
   * @return fullRotations in native engineering units of the Talon SRX
   *         firmware.
   */
  int32_t ScaleRotationsToNativeUnits(FeedbackDevice devToLookup,
                                      double fullRotations) const;
  /**
   * @param rpm double precision value representing number of rotations per
   *            minute of selected feedback sensor. If user has never called
   *            the config routine for the selected sensor, then the caller is
   *            likely passing rotations in engineering units already, in which
   *            case it is returned as is.
   * @see ConfigPotentiometerTurns
   * @see ConfigEncoderCodesPerRev
   * @return sensor velocity in native engineering units of the Talon SRX
   *         firmware.
   */
  int32_t ScaleVelocityToNativeUnits(FeedbackDevice devToLookup,
                                     double rpm) const;
  /**
   * @param nativePos integral position of the feedback sensor in native
   *                  Talon SRX units. If user has never called the config
   *                  routine for the selected sensor, then the return will be
   *                  in TALON SRX units as well to match the behavior in the
   *                  2015 season.
   * @see ConfigPotentiometerTurns
   * @see ConfigEncoderCodesPerRev
   * @return double precision number of rotations, unless config was never
   *         performed.
   */
  double ScaleNativeUnitsToRotations(FeedbackDevice devToLookup,
                                     int32_t nativePos) const;
  /**
   * @param nativeVel integral velocity of the feedback sensor in native
   *                  Talon SRX units. If user has never called the config
   *                  routine for the selected sensor, then the return will be
   *                  in TALON SRX units as well to match the behavior in the
   *                  2015 season.
   * @see ConfigPotentiometerTurns
   * @see ConfigEncoderCodesPerRev
   * @return double precision of sensor velocity in RPM, unless config was never
   *         performed.
   */
  double ScaleNativeUnitsToRpm(FeedbackDevice devToLookup,
                               int32_t nativeVel) const;

							   
  CANSpeedController::ControlMode AdaptCm(TalonControlMode talonControlMode);

  TalonControlMode AdaptCm(CANSpeedController::ControlMode controlMode);
	
  // LiveWindow stuff.
  std::shared_ptr<ITable> m_table;
  /**
   * Flips the output direction during open-loop modes like percent
   * voltage, or certain closed loop modes like speed/current mode.
   */
  bool m_isInverted = false;

  frc::HasBeenMoved m_hasBeenMoved;
};
#endif // CTR_EXCLUDE_WPILIB_CLASSES
