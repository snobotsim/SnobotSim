
  explicit CANTalon(int deviceNumber);
  explicit CANTalon(int deviceNumber, int controlPeriodMs);
  DEFAULT_MOVE_CONSTRUCTOR(CANTalon);
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
  void GetDescription(std::ostringstream& desc) const override;

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
  int SetCurrentLimit(uint32_t amps);
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
